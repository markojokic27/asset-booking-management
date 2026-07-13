// external imports
import { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';

// components
import { LayoutColumn } from '../components/layout/Layout';
import { FormDropdown } from '../components/ui/FormDropdown';
import { Pagination } from '../components/ui/Pagination';
import { SearchInput } from '../components/ui/SearchBar';
import { FilterDateInput } from '../features/booking/components/FilterDateInput';
import { MyBookingsTable } from '../features/booking/components/MyBookingsTable';

// hooks
import { useBookingCancellation } from '../features/booking/hooks/useBookingCancellation';
import { useMyBookings } from '../features/booking/hooks/useMyBookings';
import { useAuth } from '../features/auth/context/AuthContext';
import { usePagination } from '../features/user/hooks/usePagination';

// utils
import {
  filterBookingsByAsset,
  filterBookingsByDateRange,
  filterBookingsByStatus,
  filterPendingBookingsBySearch,
} from '../features/booking/utils/approvalFilter';
import { bookingStatuses } from '../features/booking/types';
import type { BookingStatus } from '../features/booking/types';
import { isAdmin } from '../features/user/utils/users';

export default function MyBookings() {
  const { t } = useTranslation();
  const { user, isLoading: isUserLoading } = useAuth();
  const { bookings, loading, error, refetch } = useMyBookings(
    user,
    !isUserLoading && user != null
  );
  const { cancel, isCancelling, cancelError, clearCancelError } =
    useBookingCancellation(refetch);
  const isUserAdmin = isAdmin(user);
  const [search, setSearch] = useState('');
  const [selectedAssetId, setSelectedAssetId] = useState('');
  const [selectedStatus, setSelectedStatus] = useState<BookingStatus | ''>('');
  const [fromDate, setFromDate] = useState('');
  const [toDate, setToDate] = useState('');

  const statusFilterOptions = useMemo(
    () => [
      { value: '', label: t('myBookings.filter.allStatuses') },
      ...bookingStatuses.map((status) => ({
        value: status,
        label: t(`bookings.status.${status.toLowerCase()}`, {
          defaultValue: status,
        }),
      })),
    ],
    [t]
  );

  const assetOptions = useMemo(() => {
    const assets = new Map<number, string>();

    for (const booking of bookings) {
      assets.set(booking.asset.id, booking.asset.name);
    }

    return Array.from(assets.entries())
      .map(([id, name]) => ({ id, name }))
      .sort((a, b) => a.name.localeCompare(b.name));
  }, [bookings]);

  const filteredBookings = useMemo(() => {
    const assetId = selectedAssetId ? Number(selectedAssetId) : null;
    const byAsset = filterBookingsByAsset(bookings, assetId);
    const byStatus = isUserAdmin
      ? filterBookingsByStatus(byAsset, selectedStatus)
      : byAsset;
    const byDate = filterBookingsByDateRange(byStatus, fromDate, toDate);

    return filterPendingBookingsBySearch(byDate, search);
  }, [bookings, fromDate, isUserAdmin, search, selectedAssetId, selectedStatus, toDate]);

  const pagination = usePagination(filteredBookings, 10);

  useEffect(() => {
    pagination.setPage(1);
  }, [fromDate, isUserAdmin, search, selectedAssetId, selectedStatus, toDate]);

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex min-h-screen flex-col pt-35 pb-10"
    >
      <div className="flex w-full flex-col gap-4">
        <div className="flex flex-col gap-2">
          {/* title for the my or all bookings page */}
          <h1 className="text-3xl font-black tracking-widest text-black dark:text-white">
            {isAdmin(user) ? t('myBookings.titleAdmin') : t('myBookings.title')}
          </h1>
        </div>
        {/* divider for the my bookings page */}
        <div className="h-px w-full bg-(--color-table-border)" />

        <div className="flex w-full flex-col gap-3">
          {/* top row: from/to date filters + search */}
          <div className="flex w-full flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
            <div className="flex w-full flex-col gap-3 sm:flex-row sm:items-end">
              <FilterDateInput 
                id="my-bookings-from-date"
                label={t('myBookings.filter.fromDate')}
                value={fromDate}
                onChange={setFromDate}
                max={toDate || undefined}
                className="w-full sm:w-40"
              />
              <FilterDateInput 
                id="my-bookings-to-date"
                label={t('myBookings.filter.toDate')}
                value={toDate}
                onChange={setToDate}
                min={fromDate || undefined}
                className="w-full sm:w-40"
              />
            </div>
            <SearchInput data-testid="search-input"
              value={search}
              onChange={setSearch}
              placeholder={t('myBookings.search.placeholder')}
              className="w-full lg:w-70"
            />
          </div>

          {/* bottom row: dropdown filters */}
          <div className="flex w-full flex-col gap-3 sm:flex-row sm:flex-wrap sm:items-end">
            <div className="relative w-full pt-1 sm:w-40">
              <FormDropdown data-testid="my-booking-asset-filter"
                id="my-bookings-asset-filter"
                aria-label={t('myBookings.filter.asset')}
                value={selectedAssetId}
                onChange={(event) => setSelectedAssetId(event.target.value)}
                options={[
                  { value: '', label: t('myBookings.filter.allAssets') },
                  ...assetOptions.map((asset) => ({
                    value: asset.id,
                    label: asset.name,
                  })),
                ]}
                className="cursor-pointer border-2 py-2.5 text-(--color-table-text) shadow-none"
              />
            </div>
            {isUserAdmin && (
              <div className="relative w-full pt-1 sm:w-44">
                <FormDropdown
                  data-testid="my-booking-status-filter"
                  id="my-bookings-status-filter"
                  aria-label={t('myBookings.filter.status')}
                  value={selectedStatus}
                  onChange={(event) =>
                    setSelectedStatus(event.target.value as BookingStatus | '')
                  }
                  options={statusFilterOptions}
                  className="h-10 cursor-pointer border-2 py-0 text-(--color-table-text) shadow-none"
                />
              </div>
            )}
          </div>
        </div>

        {/* my bookings table */}
        <MyBookingsTable
          bookings={pagination.paged}
          isLoading={loading || isUserLoading}
          error={error}
          onCancelBooking={cancel}
          isCancelling={isCancelling}
          cancelError={cancelError}
          onClearCancelError={clearCancelError}
        />

        {/* pagination for the bookings table */}
        {filteredBookings.length > 0 && !loading && !error && (
          <Pagination
            page={pagination.page}
            totalPages={pagination.totalPages}
            items={pagination.items}
            onPageChange={pagination.setPage}
          />
        )}
      </div>
    </LayoutColumn>
  );
}
