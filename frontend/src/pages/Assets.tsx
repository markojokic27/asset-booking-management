import { useState, useEffect, useMemo } from 'react';
import AddIcon from '@mui/icons-material/Add';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { SearchInput } from '../components/ui/SearchBar';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { AssetEditModal } from '../features/asset/components/AssetEditModal';
import { AssetModal } from '../features/asset/components/AssetModal';
import { AssetBookingsModal } from '../features/asset/components/AssetBookingsModal';
import type { AssetDto } from '../features/asset/types';
import { AssetAddModal } from '../features/asset/components/AssetAddModal';
import { AssetsTable } from '../features/asset/components/AssetTable';
import { getAllAssets } from '../features/asset/api/assetApi';
import type { AssetCategoryDto } from '../features/asset-category/types';
import { getAllCategories } from '../features/asset-category/api/categoryApi';

type ModalState =
  | { type: 'none' }
  | { type: 'view'; asset: AssetDto }
  | { type: 'edit'; asset: AssetDto }
  | { type: 'bookings'; asset: AssetDto }
  | { type: 'add' };

export default function Assets() {
  const [selectedCategory, setSelectedCategory] = useState<string>('Assets');
  const [assets, setAssets] = useState<AssetDto[]>([]);
  const [modal, setModal] = useState<ModalState>({ type: 'none' });
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [serverError, setServerError] = useState('');
  const [assetCategories, setAssetCategories] = useState<AssetCategoryDto[]>([]);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        setServerError('');

        const [categoriesRes, assetsRes] = await Promise.all([
          getAllCategories(),
          getAllAssets(),
        ]);

        const categoriesData = categoriesRes.content;
        setAssetCategories(categoriesData);

        const categoryMap = Object.fromEntries(
          categoriesData.map((c) => [c.id, c.name])
        );

        const assetsWithCategoryName: AssetDto[] = assetsRes.content.map((asset) => ({
          ...asset,
          categoryName: categoryMap[asset.categoryId] ?? '-',
        }));

        setAssets(assetsWithCategoryName);
      } catch (err) {
        setServerError('Failed to load assets');
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  const categoryMap = useMemo(
    () => Object.fromEntries(assetCategories.map((c) => [c.id, c.name])),
    [assetCategories]
  );

  const categoryNames = useMemo(
    () => [...assetCategories.map((c) => c.name)],
    [assetCategories]
  );

  const filteredAssets = assets.filter((asset) => {
    const matchesSearch = asset.name
      .toLowerCase()
      .includes(search.trim().toLowerCase());

    const matchesCategory =
      selectedCategory === 'Assets'
        ? true
        : (asset.categoryName ?? categoryMap[asset.categoryId] ?? '-') === selectedCategory;

    return matchesSearch && matchesCategory;
  });

  const handleView = (asset: AssetDto) => {
    setModal({ type: 'view', asset });
  };

  const handleEdit = (asset: AssetDto) => {
    setModal({ type: 'edit', asset });
  };

  const handleBookings = (asset: AssetDto) => {
    setModal({ type: 'bookings', asset });
  };

  const closeModal = () => {
    setModal({ type: 'none' });
  };

  const handleDelete = (asset: AssetDto) => {
    setAssets((current) => current.filter((a) => a.id !== asset.id));
  };

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <AssetCategoryGrid
        categories={categoryNames}
        selectedCategory={selectedCategory}
        onSelectCategory={setSelectedCategory}
      />

      <div className="mt-12 flex w-full items-center justify-between gap-4">
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
          {selectedCategory}
        </h1>

        <Button
          type="button"
          size="sm"
          iconLeft={<AddIcon fontSize="small" />}
          onClick={() => setModal({ type: 'add' })}
        >
          New asset
        </Button>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      <div className="mt-6 flex w-full flex-wrap items-end gap-3">
        <SearchInput
          value={search}
          onChange={setSearch}
          placeholder="Search assets..."
          className="mb-0 w-full sm:ml-auto sm:w-70"
        />
      </div>

      <div className="mt-6 w-full">
        {loading ? (
          <div>Loading assets...</div>
        ) : serverError ? (
          <div className="text-red-600">{serverError}</div>
        ) : (
          <AssetsTable
            assets={filteredAssets}
            categoryMap={categoryMap}
            onView={handleView}
            onEdit={handleEdit}
            onBookings={handleBookings}
            onDelete={handleDelete}
            //className="w-full"
          />
        )}
      </div>

      <AssetModal
        isOpen={modal.type === 'view'}
        onClose={closeModal}
        asset={modal.type === 'view' ? modal.asset : null}
      />

      <AssetEditModal
        isOpen={modal.type === 'edit'}
        onClose={closeModal}
        asset={modal.type === 'edit' ? modal.asset : null}
        onSave={(updatedAsset) => {
          setAssets((currentAssets) =>
            currentAssets.map((asset) =>
              asset.id === updatedAsset.id
                ? {
                    ...updatedAsset,
                    categoryName:
                      updatedAsset.categoryName ??
                      categoryMap[updatedAsset.categoryId] ??
                      '-',
                  }
                : asset
            )
          );
          closeModal();
        }}
      />

      <AssetBookingsModal
        isOpen={modal.type === 'bookings'}
        onClose={closeModal}
        asset={modal.type === 'bookings' ? modal.asset : null}
      />
      <AssetAddModal
        isOpen={modal.type === 'add'}
        onClose={closeModal}
        onSave={(newAsset) => {
          setAssets((current) => [
            {
              ...newAsset,
              categoryName:
                newAsset.categoryName ?? categoryMap[newAsset.categoryId] ?? '-',
            },
            ...current,
          ]);
          setModal({ type: 'add' });
        }}
      />
    </LayoutColumn>
  );
}