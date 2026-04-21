import { LayoutColumn } from '../components/layout/Layout';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { categories, type AssetDto } from '../features/asset/types';
import * as React from 'react';
import { DateTimeInput } from '../components/ui/DateTimeInput';
import { SearchInput } from '../components/ui/SearchBar';
import { AssetEditModal } from '../features/asset/components/AssetEditModal';
import { AssetModal } from '../features/asset/components/AssetModal';
import { AssetBookingsModal } from '../features/asset/components/AssetBookingsModal';
import { AssetAddModal } from '../features/asset/components/AssetAddModal';

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

type State = {
  selectedCategory: string;
  modals: {
    assetAdd: boolean;
    assetView: boolean;
    assetEdit: boolean;
    bookings: boolean;
  };
  assets: AssetDto[];
  activeAsset: AssetDto | null;
  filters: {
    search: string;
    fromDate: string;
    toDate: string;
    fromHour: string;
    toHour: string;
  };
};

const initialState: State = {
  selectedCategory: 'Assets',
  modals: {
    assetAdd: false,
    assetView: false,
    assetEdit: false,
    bookings: false,
  },
  assets: initialAssets,
  activeAsset: null,
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

  const updateState = (partial: Partial<State>) =>
    setState((prev) => ({ ...prev, ...partial }));

  const updateFilters = (partial: Partial<State['filters']>) =>
    setState((prev) => ({
      ...prev,
      filters: { ...prev.filters, ...partial },
    }));

  const updateModals = (partial: Partial<State['modals']>) =>
    setState((prev) => ({
      ...prev,
      modals: { ...prev.modals, ...partial },
    }));

  const filteredAssetsByCategory =
    state.selectedCategory === 'Assets'
      ? state.assets
      : state.assets.filter(
          (asset) => asset.categoryName === state.selectedCategory
        );

  const filteredAssets = filteredAssetsByCategory.filter((asset) =>
    asset.name
      .toLowerCase()
      .includes(state.filters.search.trim().toLowerCase())
  );

  const openBookingsModal = (asset: AssetDto) => {
    setState((prev) => ({
      ...prev,
      activeAsset: asset,
      modals: { ...prev.modals, bookings: true },
    }));
  };

  const closeBookingsModal = () => {
    setState((prev) => ({
      ...prev,
      activeAsset: null,
      modals: { ...prev.modals, bookings: false },
    }));
  };

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
          updateState({ selectedCategory: cat })
        }
      />

      <div className="mt-12 flex w-full items-center justify-between gap-4">
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
          {state.selectedCategory}
        </h1>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      <div className="mt-6 flex w-full flex-wrap items-end gap-3">
        <DateTimeInput
          id="from-date"
          label="From time"
          value={state.filters.fromDate}
          onChange={(v) => updateFilters({ fromDate: v })}
          hourValue={state.filters.fromHour}
          onHourChange={(v) => updateFilters({ fromHour: v })}
          className="w-full sm:w-70"
        />

        <DateTimeInput
          id="to-date"
          label="To time"
          value={state.filters.toDate}
          onChange={(v) => updateFilters({ toDate: v })}
          hourValue={state.filters.toHour}
          onHourChange={(v) => updateFilters({ toHour: v })}
          className="w-full sm:w-70"
        />

        <SearchInput
          value={state.filters.search}
          onChange={(v) => updateFilters({ search: v })}
          placeholder="Search assets..."
          className="mb-0 w-full sm:ml-auto sm:w-70"
        />
      </div>

      {/* Table (ako ga vratiš)
      <div className="mt-6 w-full">
        <Table
          data={filteredAssets}
          columns={columns}
          getRowKey={(asset) => asset.id}
          className="w-full"
        />
      </div>
      */}

      <AssetModal
        isOpen={state.modals.assetView}
        onClose={() => {
          updateModals({ assetView: false });
          updateState({ activeAsset: null });
        }}
        asset={state.activeAsset}
      />

      <AssetEditModal
        isOpen={state.modals.assetEdit}
        onClose={() => {
          updateModals({ assetEdit: false });
          updateState({ activeAsset: null });
        }}
        asset={state.activeAsset}
        onSave={(updatedAsset) => {
          setState((prev) => ({
            ...prev,
            assets: prev.assets.map((asset) =>
              asset.id === updatedAsset.id ? updatedAsset : asset
            ),
          }));
        }}
      />

      <AssetBookingsModal
        isOpen={state.modals.bookings}
        onClose={closeBookingsModal}
        asset={state.activeAsset}
      />

      <AssetAddModal
        isOpen={state.modals.assetAdd}
        onClose={() => updateModals({ assetAdd: false })}
        onSave={(newAsset) => {
          setState((prev) => ({
            ...prev,
            assets: [newAsset, ...prev.assets],
            modals: { ...prev.modals, assetAdd: false },
          }));
        }}
      />
    </LayoutColumn>
  );
}