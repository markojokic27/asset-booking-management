// external imports
import { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';

// components
import { LayoutColumn } from '../components/layout/Layout';
import { FormDropdown } from '../components/ui/FormDropdown';
import { Pagination } from '../components/ui/Pagination';
import { SearchInput } from '../components/ui/SearchBar';
import { MyBookingsTable } from '../features/booking/components/MyBookingsTable';

// hooks
import { useMyBookings } from '../features/booking/hooks/useMyBookings';
import { useCurrentUser } from '../features/user/hooks/useCurrentUser';
import { usePagination } from '../features/user/hooks/usePagination';

// utils
import {
  filterBookingsByAsset,
  filterPendingBookingsBySearch,
} from '../features/booking/utilis/approvalFilter';
import { isAdmin } from '../features/user/utilis/users';

export default function MyBookings() {
  const { t } = useTranslation();
  const { user, isLoading: isUserLoading } = useCurrentUser();
  const { bookings, loading, error } = useMyBookings(
    user,
    !isUserLoading && user != null
  );
  const [search, setSearch] = useState('');
  const [selectedAssetId, setSelectedAssetId] = useState('');

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

    return filterPendingBookingsBySearch(byAsset, search);
  }, [bookings, search, selectedAssetId]);

  const pagination = usePagination(filteredBookings, 10);

  useEffect(() => {
    pagination.setPage(1);
  }, [search, selectedAssetId]);

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

        <div className="flex w-full flex-col items-stretch gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="relative w-full pt-1 sm:w-40">
            <FormDropdown
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
              className="border-2 py-2.5 text-(--color-table-text) shadow-none"
            />
          </div>
          <SearchInput
            value={search}
            onChange={setSearch}
            placeholder={t('myBookings.search.placeholder')}
            className="w-full sm:w-70"
          />
        </div>

        {/* my bookings table */}
        <MyBookingsTable
          bookings={pagination.paged}
          isLoading={loading || isUserLoading}
          error={error}
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
