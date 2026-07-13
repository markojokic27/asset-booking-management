// External packages
import * as React from 'react';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

// Hooks
import { useBookingsByAsset } from '../features/booking/hooks/useBookingByAsset';
import { useBookingFilters } from '../features/booking/hooks/useBookingFilters';
import { useBookingAvailability } from '../features/booking/hooks/useBookingAvailability';
import { useCreateBooking } from '../features/booking/hooks/useCreateBooking';
import { useAuth } from '../features/auth/context/AuthContext';
import { useBookingCancellation } from '../features/booking/hooks/useBookingCancellation';

// Utils
import { mapBookingsToCalendarEvents } from '../features/booking/utils/bookingLogic';
import { getDatesForWeekdays } from '../features/booking/utils/getDatesForWeekdays';
import { getAvailableRecurringDates } from '../features/booking/utils/getAvailableRecurringDates';
import { getAssetById } from '../features/asset/api/assetApi';
import { getCategoryById } from '../features/asset-category/api/categoryApi';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { FiltersBar } from '../features/booking/components/FilterBar';
import { AvailabilityCalendar } from '../features/booking/components/AvailabilityCalendar';
import { RecurringDaysSelector } from '../features/booking/components/RecurringDaysSelector';
import { CancelBookingModal } from '../features/booking/components/CancelBookingModal';
import { BookingDetailsModal } from '../features/booking/components/BookingDetailsModal';
import { BookingModal } from '../features/booking/components/BookingModal';
import { Toast } from '../components/ui/Toast';

// Types
import type { BookingWithRelations } from '../features/booking/types';
import type { AssetCategoryDto } from '../features/asset-category/types';
import { getBookingLimit } from '../features/booking/utils/getBookingLimit';

export default function BookingsByAsset() {
  const { assetId } = useParams();
  const { t } = useTranslation();
  const { user } = useAuth();

  const { filters, setFilters, handleCalendarDateClick } = useBookingFilters();

  const { bookings, loading, error, refetch } = useBookingsByAsset(assetId!);

  const assetFromBookings = bookings?.[0]?.asset;

  const [fetchedAsset, setFetchedAsset] = React.useState<any>(null);
  const [category, setCategory] = React.useState<AssetCategoryDto | null>(null);

  const asset = (assetFromBookings ?? fetchedAsset) as any;

  React.useEffect(() => {
    if (assetFromBookings || !assetId) return;

    getAssetById(assetId)
      .then(setFetchedAsset)
      .catch(() => setFetchedAsset(null));
  }, [assetFromBookings, assetId]);

  React.useEffect(() => {
    if (!asset?.category && asset?.categoryId) {
      getCategoryById(asset.categoryId)
        .then(setCategory)
        .catch(() => setCategory(null));
    }
  }, [asset?.categoryId]);

  const resolvedCategory = React.useMemo(() => {
    if (asset?.category) return asset.category;
    return category;
  }, [asset?.category, category]);

  const bookingPeriod: 'HOUR' | 'DAY' =
    resolvedCategory?.bookingPeriod === 'HOUR' ? 'HOUR' : 'DAY';

  React.useEffect(() => {
    if (assetFromBookings || !assetId) return;

    getAssetById(assetId)
      .then(setFetchedAsset)
      .catch(() => setFetchedAsset(null));
  }, [assetFromBookings, assetId]);

  const [notes, setNotes] = React.useState('');
  const [selectedBooking, setSelectedBooking] =
    React.useState<BookingWithRelations | null>(null);
  const [bookingToCancel, setBookingToCancel] =
    React.useState<BookingWithRelations | null>(null);
  const [visibleMonth, setVisibleMonth] = React.useState(new Date());
  const [isBookingModalOpen, setIsBookingModalOpen] = React.useState(false);

  const recurringDates = getDatesForWeekdays(
    visibleMonth,
    filters.selectedWeekdays
  );

  const { cancel, isCancelling, cancelError, clearCancelError } =
    useBookingCancellation(refetch);

  const openCancelModal = (booking: BookingWithRelations) => {
    clearCancelError?.();

    setSelectedBooking(null);
    setBookingToCancel(booking);
  };

  const closeCancelModal = () => {
    clearCancelError?.();
    setBookingToCancel(null);
  };

  const handleConfirmCancel = async () => {
    if (!bookingToCancel) return;

    const success = await cancel(Number(bookingToCancel.id));

    if (success) {
      Toast.success(t('layout.toast.bookingCancelled'));
      setBookingToCancel(null);
    }
  };

  const availableRecurringDates = React.useMemo(
    () => getAvailableRecurringDates(recurringDates, bookings),
    [recurringDates, bookings]
  );

  const calendarEvents = React.useMemo(
    () => mapBookingsToCalendarEvents(bookings),
    [bookings]
  );

  const isButtonDisabled = useBookingAvailability({
    assetStatus: asset?.status,
    filters: filters,
    bookings,
    bookingPeriod,
    reccuringDates: filters.selectedWeekdays,
    availableRecurringDates,
  });

  const { isCreating, handleCreateBooking } = useCreateBooking({
    assetId: Number(assetId),
    notes,
    setNotes,
    filters: filters,
    refetch,
    bookingPeriod,
    availableRecurringDates,
    t,
  });

  const maxBookingDate = getBookingLimit(user?.role, resolvedCategory?.name);

  if (!asset || !resolvedCategory) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35">{t('bookings.buttons.loadingAsset')}</div>
      </LayoutColumn>
    );
  }

  if (loading) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35">{t('bookings.buttons.loading')}</div>
      </LayoutColumn>
    );
  }
  if (error) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35 text-red-500">
          {t('bookings.buttons.errorLoadingBookings')}
        </div>
      </LayoutColumn>
    );
  }
  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div className="mb-6 flex flex-col items-center justify-between gap-4 sm:flex-row">
        <div className="flex flex-col gap-6 sm:flex-row sm:items-center">
          <h1 className="text-3xl font-black text-black dark:text-white">
            {asset.name}
          </h1>

          <span
            className={`rounded px-3 py-1 text-center text-sm font-medium ${
              asset.status === 'ACTIVE'
                ? 'bg-green-100 text-green-700'
                : 'bg-gray-200 text-gray-700'
            }`}
          >
            {asset.status}
          </span>
        </div>
        <p>
          {t('assets.location')}: {asset.location}
        </p>
      </div>

      <div className="mb-6 h-px w-full bg-(--color-table-border)" />

      <div className="mb-2 flex w-full items-end justify-between gap-4">
        <FiltersBar
          variant={bookingPeriod === 'DAY' ? 'DAYS' : 'HOUR'}
          filters={filters}
          setFilters={setFilters}
          showSearch={false}
          bookingLimit={maxBookingDate}
          setVisibleMonth={setVisibleMonth}
          className="mt-0 grid-cols-1 sm:grid-cols-2 lg:grid-cols-2"
        />
      </div>
      <div className="mb-6 flex items-end gap-4">
        <div className="flex w-full flex-col">
          <p className="mb-1 text-sm font-medium text-(--color-table-text)">
            {t('ui.notes.label')}
          </p>
          <Input
            placeholder={t('ui.notes.placeholder')}
            className="w-full border shadow-none"
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
          />
        </div>
        <Button
          data-testid="book-asset-button"
          variant="solid"
          className="h-11 min-w-40"
          size="md"
          disabled={isButtonDisabled || isCreating}
          onClick={() => setIsBookingModalOpen(true)}
        >
          {isCreating
            ? t('bookings.buttons.booking')
            : t('bookings.buttons.book')}
        </Button>
      </div>
      {resolvedCategory.name === 'Parking' &&
        user &&
        user.role !== 'EMPLOYEE' && (
          <RecurringDaysSelector
            selectedDays={filters.selectedWeekdays}
            onChange={(days) =>
              setFilters((prev) => ({
                ...prev,
                selectedWeekdays: days,
                fromDate: '',
                toDate: '',
                fromHour: '',
                toHour: '',
              }))
            }
          />
        )}
      <AvailabilityCalendar
        events={calendarEvents}
        selectedFromDate={filters.fromDate}
        selectedToDate={filters.toDate}
        onDateClick={handleCalendarDateClick}
        setSelectedBooking={setSelectedBooking}
        onRangeSelect={(fromDate, toDate) =>
          setFilters((prev) => ({
            ...prev,
            fromDate,
            toDate,
            selectedWeekdays: [],
          }))
        }
        variant={bookingPeriod}
        onMonthChange={setVisibleMonth}
        visibleMonth={visibleMonth}
        availableRecurringDates={availableRecurringDates}
        maxBookingDate={maxBookingDate}
      />

      <BookingModal
        open={isBookingModalOpen}
        onClose={() => setIsBookingModalOpen(false)}
        filters={filters}
        asset={asset}
        user={user}
        handleCreateBooking={handleCreateBooking}
        needApproval={resolvedCategory?.approval === true}
        availableRecurringDates={availableRecurringDates}
        variant={bookingPeriod}
      />

      <BookingDetailsModal
        booking={selectedBooking}
        onClose={() => setSelectedBooking(null)}
        currentUserId={user?.id}
        refetch={refetch}
        openCancelModal={openCancelModal}
      />

      <CancelBookingModal
        booking={bookingToCancel}
        onClose={closeCancelModal}
        onConfirm={() => void handleConfirmCancel()}
        isProcessing={isCancelling}
        actionError={cancelError}
      />
    </LayoutColumn>
  );
}
