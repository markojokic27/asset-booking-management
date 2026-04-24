import { LayoutColumn } from '../components/layout/Layout';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { categories, type AssetDto } from '../features/asset/types';
import * as React from 'react';
import { FiltersBar } from '../components/ui/FilterBar';
import { Button } from '../components/ui/Button';
import type { Filters, BookingsState } from '../features/booking/types';
import { BookingTable } from '../features/booking/components/BookingTable';

const initialAssets: AssetDto[] = [
  {
    id: 1,
    name: 'Dell Latitude 5440',
    categoryId: 1,
    categoryName: 'Laptops',
    code: 'DL-5440',
    status: 'ACTIVE',
    location: 'Split',
    createdAt: new Date(),
    lastModifiedAt: new Date(),
  },
];

const initialFilters: Filters = {
  search: '',
  fromDate: '',
  toDate: '',
  fromHour: '',
  toHour: '',
};

const initialState: BookingsState = {
  selectedCategory: 'Laptops',
  assets: initialAssets,
  filters: initialFilters,
};

export default function Bookings() {
  const [state, setState] = React.useState<BookingsState>(initialState);

  const setFilters: React.Dispatch<React.SetStateAction<Filters>> = (
    updater
  ) => {
    setState((prev) => ({
      ...prev,
      filters: typeof updater === 'function' ? updater(prev.filters) : updater,
    }));
  };

  const filteredAssets = React.useMemo(() => {
    const byCategory =
      state.selectedCategory === 'Assets'
        ? state.assets
        : state.assets.filter((a) => a.categoryName === state.selectedCategory);

    return byCategory.filter((asset) =>
      asset.name
        .toLowerCase()
        .includes(state.filters.search.trim().toLowerCase())
    );
  }, [state.assets, state.selectedCategory, state.filters.search]);

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <AssetCategoryGrid
        categories={categories}
        selectedCategory={state.selectedCategory}
        onSelectCategory={(cat) =>
          setState((prev) => ({ ...prev, selectedCategory: cat }))
        }
      />

      <div className="mt-12 flex w-full items-center justify-between gap-4">
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em]">
          {state.selectedCategory}
        </h1>

        <Button
          className="border-gray-400 bg-gray-400 hover:border-gray-300 hover:bg-gray-300"
          onClick={() =>
            setState((prev) => ({
              ...prev,
              filters: initialFilters,
            }))
          }
        >
          Reset filters
        </Button>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      <FiltersBar filters={state.filters} setFilters={setFilters} />

      <BookingTable
        assets={filteredAssets}
        onBook={(asset) => {
          console.log('Booking asset:', asset);
        }}
        className="mt-6"
      />
    </LayoutColumn>
  );
}
