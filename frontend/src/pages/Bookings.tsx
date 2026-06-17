// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { FiltersBar } from '../features/booking/components/FilterBar';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { BookingTable } from '../features/booking/components/BookingTable';
import { ParkingMap } from '../features/booking/components/ParkingMap';
import { OfficeMap } from '../features/booking/components/OfficeMap';
import { useBookingData } from '../features/booking/hooks/useBookingData';

// Types
import type { Filters } from '../features/booking/types';

const defaultFilters: Filters = {
  search: '',
  fromDate: '',
  toDate: '',
  fromHour: '',
  toHour: '',
  selectedWeekdays: [],
};

export default function Bookings() {
  const { t } = useTranslation();
  const [filters, setFilters] = React.useState<Filters>(defaultFilters);

  const {
    assets,
    categories,
    selectedCategory,
    bookings,
    selectCategoryByName,
    loading,
    refetchBookings,
  } = useBookingData({ filters });

  const variant = (selectedCategory?.bookingPeriod ?? 'HOUR') as 'HOUR' | 'DAY';

  const handleResetFilters = () => {
    setFilters(defaultFilters);
  };

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <AssetCategoryGrid
        categories={categories.map((c) => c.name)}
        selectedCategory={selectedCategory?.name ?? ''}
        onSelectCategory={selectCategoryByName}
      />

      <div className="mt-12 flex w-full items-center justify-between gap-4">
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em]">
          {selectedCategory?.name ?? ''}
        </h1>

        <div className="flex items-center gap-3">
          {selectedCategory?.name === 'Parking' && (
            <ParkingMap
              bookings={bookings}
              assets={assets}
              filters={filters}
              refetchBookings={refetchBookings}
              setFilters={setFilters}
            />
          )}
          {selectedCategory?.name === 'Meeting room' && <OfficeMap />}
          <Button
            data-testid="reset-filters-button"
            className="border-gray-400 bg-gray-400 hover:border-gray-300 hover:bg-gray-300"
            onClick={handleResetFilters}
          >
            {t('bookings.resetFilters')}
          </Button>
        </div>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />
      <FiltersBar filters={filters} setFilters={setFilters} variant={variant} />

      {loading ? (
        <div className="mt-6">{t('bookings.loading')}</div>
      ) : (
        <BookingTable assets={assets} className="mt-6" />
      )}
    </LayoutColumn>
  );
}
