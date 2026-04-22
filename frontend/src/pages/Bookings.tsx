import { LayoutColumn } from '../components/layout/Layout';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { categories, type AssetDto } from '../features/asset/types';
import * as React from 'react';
import { FiltersBar } from '../components/ui/FilterBar';

const initialAssets: AssetDto[] = [
  {
    id: '1',
    name: 'Dell Latitude 5440',
    categoryId: 1,
    categoryName: 'Laptops',
    imageUrl: undefined,
    code: 'DL-5440',
    status: 'ACTIVE',
    location: 'Split',
    createdAt: new Date(),
    lastModifiedAt: new Date(),
  },
];

type Filters = {
  search: string;
  fromDate: string;
  toDate: string;
  fromHour: string;
  toHour: string;
};

type State = {
  selectedCategory: string;
  assets: AssetDto[];
  filters: Filters;
};

const initialState: State = {
  selectedCategory: 'Laptops',
  assets: initialAssets,
  filters: {
    search: '',
    fromDate: '',
    toDate: '',
    fromHour: '',
    toHour: '',
  },
};

export default function Bookings() {
  const [state, setState] = React.useState<State>(initialState);

  const setFilters: React.Dispatch<React.SetStateAction<Filters>> = (
    updater
  ) => {
    setState((prev) => ({
      ...prev,
      filters: typeof updater === 'function' ? updater(prev.filters) : updater,
    }));
  };

  const filteredAssetsByCategory =
    state.selectedCategory === 'Assets'
      ? state.assets
      : state.assets.filter((a) => a.categoryName === state.selectedCategory);

  const _filteredAssets = filteredAssetsByCategory.filter((asset) =>
    asset.name.toLowerCase().includes(state.filters.search.trim().toLowerCase())
  );

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
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
          {state.selectedCategory}
        </h1>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      <FiltersBar filters={state.filters} setFilters={setFilters} />

      {/* Primjer gdje koristiš filtrirane podatke */}
      {/* <Table data={filteredAssets} ... /> */}
    </LayoutColumn>
  );
}
