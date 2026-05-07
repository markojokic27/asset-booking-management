// External packages
import * as React from 'react';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { FiltersBar } from '../features/booking/components/FilterBar';
import { Button } from '../components/ui/Button';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { BookingTable } from '../features/booking/components/BookingTable';
import { BookingModal } from '../features/booking/components/BookingModal';

// Hooks
import { useBookingData } from '../features/booking/hooks/useBookingData';

// Types
import type { AssetDto } from '../features/asset/types';
import type { Filters } from '../features/booking/types';

const defaultFilters: Filters = {
  search: '',
  fromDate: '',
  toDate: '',
  fromHour: '',
  toHour: '',
};

export default function Bookings() {
  const {
    assets,
    categories,
    selectedCategory,
    selectCategoryByName,
    loading,
  } = useBookingData();

  const [filters, setFilters] = React.useState<Filters>(defaultFilters);
  const [selectedAsset, setSelectedAsset] = React.useState<AssetDto | null>(
    null
  );
  const [openBookingModal, setOpenBookingModal] = React.useState(false);

  const filteredAssets = assets.filter((a) => {
    const matchCategory = selectedCategory
      ? a.categoryId === selectedCategory.id
      : true;

    const matchSearch = a.name
      .toLowerCase()
      .includes(filters.search.trim().toLowerCase());

    return matchCategory && matchSearch;
  });

  const variant = (selectedCategory?.bookingPeriod ?? 'HOUR') as 'HOUR' | 'DAY';

  const handleOpenBookingModal = (asset: AssetDto) => {
    setSelectedAsset(asset);
    setOpenBookingModal(true);
  };

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

        <Button
          className="border-gray-400 bg-gray-400 hover:border-gray-300 hover:bg-gray-300"
          onClick={handleResetFilters}
        >
          Reset filters
        </Button>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      <FiltersBar filters={filters} setFilters={setFilters} variant={variant} />

      {loading ? (
        <div className="mt-6">Loading...</div>
      ) : (
        <BookingTable
          assets={filteredAssets}
          onBook={handleOpenBookingModal}
          className="mt-6"
        />
      )}

      <BookingModal
        open={openBookingModal}
        onClose={() => setOpenBookingModal(false)}
        asset={selectedAsset}
        filters={filters}
        setFilters={setFilters}
      />
    </LayoutColumn>
  );
}
