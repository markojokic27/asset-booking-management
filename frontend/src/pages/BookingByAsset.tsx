// External packages
import * as React from 'react';
import { useParams } from 'react-router-dom';

// Hooks
import { useBookingsByAsset } from '../features/booking/hooks/useBookingByAsset';
import { useBookingFilters } from '../features/booking/hooks/useBookingFilters';
import { useBookingAvailability } from '../features/booking/hooks/useBookingAvailability';
import { useCreateBooking } from '../features/booking/hooks/useCreateBooking';

// Utils
import { mapBookingsToCalendarEvents } from '../features/booking/utilis/bookingLogic';
import { getDatesForWeekdays } from '../features/booking/utilis/getDatesForWeekdays';
import { getAvailableRecurringDates } from '../features/booking/utilis/getAvailableRecurringDates';
import { useTranslation } from 'react-i18next';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { FiltersBar } from '../features/booking/components/FilterBar';
import { AvailabilityCalendar } from '../features/booking/components/AvailabilityCalendar';
import { BookingDetailsModal } from '../features/booking/components/BookingDetailsModal';
import { RecurringDaysSelector } from '../features/booking/components/RecurringDaysSelector';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';

// Types
import type { BookingWithRelations } from '../features/booking/types';

// TODO: internationalization

export default function BookingsByAsset() {
  const { assetId } = useParams();
  const { t } = useTranslation();

  const { filters, setFilters, handleCalendarDateClick } = useBookingFilters();

  const { bookings, loading, error, refetch } = useBookingsByAsset(assetId!);

  const asset = bookings?.[0]?.asset;

  const [notes, setNotes] = React.useState('');
  const [selectedBooking, setSelectedBooking] =
    React.useState<BookingWithRelations | null>(null);
  const [visibleMonth, setVisibleMonth] = React.useState(new Date());

  const recurringDates = getDatesForWeekdays(
    visibleMonth,
    filters.selectedWeekdays
  );
  const availableRecurringDates = React.useMemo(
    () => getAvailableRecurringDates(recurringDates, bookings),
    [recurringDates, bookings]
  );

  console.log('Available recurring dates:', availableRecurringDates);
  const calendarEvents = React.useMemo(
    () => mapBookingsToCalendarEvents(bookings),
    [bookings]
  );

  const isButtonDisabled = useBookingAvailability({
    assetStatus: asset?.status,
    filters: filters,
    bookings,
    bookingPeriod: asset?.category.bookingPeriod === 'HOUR' ? 'HOUR' : 'DAY',
    reccuringDates: filters.selectedWeekdays,
    avaibleRecurringDates: availableRecurringDates,
  });

  const { isCreating, handleCreateBooking } = useCreateBooking({
    assetId: Number(assetId),
    notes,
    setNotes,
    filters: filters,
    refetch,
    bookingPeriod: asset?.category.bookingPeriod === 'HOUR' ? 'HOUR' : 'DAY',
    availableRecurringDates: availableRecurringDates,
  });

  if (loading) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35">Loading...</div>
      </LayoutColumn>
    );
  }
  if (error) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35 text-red-500">
          Error loading bookings. Please try again later.
        </div>
      </LayoutColumn>
    );
  }
  if (!asset) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35">Asset doesnt have booking history.</div>
      </LayoutColumn>
    );
  }
  console.log('FFFF', filters);
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
        <p>{t('assets.location')}: {asset.location}</p> 
      </div>

      <div className="mb-6 h-px w-full bg-(--color-table-border)" />

      <div className="mb-2 flex w-full items-end justify-between gap-4">
        <FiltersBar
          variant={asset.category.bookingPeriod === 'HOUR' ? 'HOUR' : 'DAYS'}
          filters={filters}
          setFilters={setFilters}
          showSearch={false}
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
          onClick={handleCreateBooking}
        >
          {isCreating ? 'Booking...' : 'Book'}
        </Button>
      </div>
      {asset.category.name === 'Parking' && ( //TODO - allow only to privileged users
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
        variant={asset.category.bookingPeriod === 'HOUR' ? 'HOUR' : 'DAY'}
        onMonthChange={setVisibleMonth}
        availableRecurringDates={availableRecurringDates}
      />

      <BookingDetailsModal
        booking={selectedBooking}
        onClose={() => setSelectedBooking(null)}
      />
    </LayoutColumn>
  );
}
