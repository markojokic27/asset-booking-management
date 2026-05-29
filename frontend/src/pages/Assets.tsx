// External packages
import { useState, useEffect, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import AddIcon from '@mui/icons-material/Add';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { DeleteModal } from '../components/ui/DeleteModal';
import { SearchInput } from '../components/ui/SearchBar';
import { Pagination } from '../components/ui/Pagination';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { AssetModal } from '../features/asset/components/AssetModal';
import { AssetBookingsModal } from '../features/asset/components/AssetBookingsModal';
import { AssetsTable } from '../features/asset/components/AssetTable';
import { AssetReportModal } from '../features/asset/components/AssetReportModal';
import { AssetFormModal } from '../features/asset/components/AssetFormModal';
import { ShowDeletedFilter } from '../features/user/components/ShowDeletedFilter';

// API
import {
  createAsset,
  deleteAsset,
  getAllAssets,
  updateAsset,
} from '../features/asset/api/assetApi';
import { getAllCategories } from '../features/asset-category/api/categoryApi';

// Hooks
import { usePagination } from '../features/user/hooks/usePagination';
import { useCurrentUser } from '../features/user/hooks/useCurrentUser';
import { isAdmin } from '../features/user/utilis/users';

// Types
import type { AssetDto } from '../features/asset/types';
import type { AssetCategoryDto } from '../features/asset-category/types';

type ModalState =
  | { type: 'none' }
  | { type: 'view'; asset: AssetDto }
  | { type: 'edit'; asset: AssetDto }
  | { type: 'bookings'; asset: AssetDto }
  | { type: 'add' }
  | { type: 'delete'; asset: AssetDto }
  | { type: 'report'; asset: AssetDto };


export default function Assets() {
  const { t } = useTranslation();
  const { user } = useCurrentUser();
  const [selectedCategory, setSelectedCategory] = useState<string>('Assets');
  const [assets, setAssets] = useState<AssetDto[]>([]);
  const [modal, setModal] = useState<ModalState>({ type: 'none' });
  const [search, setSearch] = useState('');
  const [showDeleted, setShowDeleted] = useState(false);
  const [nameSortDir, setNameSortDir] = useState<'asc' | 'desc'>('asc');
  const [loading, setLoading] = useState(true);
  const [serverError, setServerError] = useState('');
  const [assetCategories, setAssetCategories] = useState<AssetCategoryDto[]>(
    []
  );

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

        const assetsWithCategoryName: AssetDto[] = assetsRes.content.map(
          (asset) => ({
            ...asset,
            categoryName: categoryMap[asset.categoryId] ?? '-',
          })
        );

        setAssets(assetsWithCategoryName);
      } catch (err) {
        setServerError(t('assets.errors.loadAssets'));
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

  const pageTitle =
    selectedCategory === 'Assets'
      ? t('assets.categories.all')
      : selectedCategory;

  const collator = useMemo(
    () => new Intl.Collator('hr', { sensitivity: 'base' }),
    []
  );

  const filteredAssets = useMemo(
    () =>
      assets.filter((asset) => {
        const matchesSearch = asset.name
          .toLowerCase()
          .includes(search.trim().toLowerCase());

        const matchesCategory =
          selectedCategory === 'Assets'
            ? true
            : (asset.categoryName ?? categoryMap[asset.categoryId] ?? '-') ===
              selectedCategory;

        const matchesDeleted = showDeleted || asset.status !== 'DELETED';

        return matchesSearch && matchesCategory && matchesDeleted;
      }),
    [assets, search, selectedCategory, categoryMap, showDeleted]
  );

  const sortedAssets = useMemo(() => {
    const dir = nameSortDir === 'asc' ? 1 : -1;

    return [...filteredAssets].sort(
      (a, b) => collator.compare(a.name, b.name) * dir
    );
  }, [filteredAssets, nameSortDir, collator]);

  const pagination = usePagination(sortedAssets, 10);

  const closeModal = () => {
    setModal({ type: 'none' });
  };

  const handleDelete = async (asset: AssetDto) => {
    try {
      await deleteAsset(asset.id);

      setAssets((current) =>
        current.map((a) =>
          a.id === asset.id ? { ...a, status: 'DELETED' as const } : a
        )
      );
    } catch (err) {
      console.error('Failed to delete asset:', err);
    }
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

      <div className="mt-12 flex w-full flex-col sm:items-center sm:justify-between gap-4 sm:flex-row">
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em] text-black dark:text-white">
          {pageTitle}
        </h1>

        {isAdmin(user) && (
          <Button
            data-testid="add-asset-button"
            type="button"
            size="sm"
            iconLeft={<AddIcon fontSize="small" />}
            onClick={() => setModal({ type: 'add' })}
            className="w-full sm:w-fit"
          >
            {t('assets.actions.new')}
          </Button>
        )}
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      <div className="mt-6 flex w-full items-center justify-between">
        <div className="flex items-center">
          <ShowDeletedFilter
            checked={showDeleted}
            onToggle={() => setShowDeleted((v) => !v)}
            labelKey="assets.filters.showDeleted"
          />
        </div>

        <SearchInput
          value={search}
          onChange={setSearch}
          placeholder={t('assets.search.placeholder')}
          className="w-70"
        />
      </div>

      <div className="mt-6 w-full">
        {loading ? (
          <div>{t('assets.empty.loading')}</div>
        ) : serverError ? (
          <div className="text-red-600">{serverError}</div>
        ) : (
          <AssetsTable
            assets={pagination.paged}
            categoryMap={categoryMap}
            nameSortDir={nameSortDir}
            onToggleNameSort={() =>
              setNameSortDir((d) => (d === 'asc' ? 'desc' : 'asc'))
            }
            onView={(asset) => setModal({ type: 'view', asset })}
            {...(isAdmin(user)
              ? {
                  onEdit: (asset) => setModal({ type: 'edit', asset }),
                  onDelete: (asset) => setModal({ type: 'delete', asset }),
                }
              : {})}
            onBookings={(asset) => setModal({ type: 'bookings', asset })}
            onReport={(asset) => setModal({type: 'report', asset})}
          />
        )}
      </div>

      {filteredAssets.length > 0 && !loading && !serverError && (
        <Pagination
          page={pagination.page}
          totalPages={pagination.totalPages}
          items={pagination.items}
          onPageChange={pagination.setPage}
        />
      )}

      <AssetModal
        isOpen={modal.type === 'view'}
        onClose={closeModal}
        asset={modal.type === 'view' ? modal.asset : null}
      />

      <AssetReportModal
        isOpen={modal.type === 'report'}
        onClose={closeModal}
        asset={modal.type === 'report' ? modal.asset : null}
      />

      {isAdmin(user) && (
        <AssetFormModal
          isOpen={modal.type === 'add' || modal.type === 'edit'}
          mode={modal.type === 'add' ? 'create' : 'edit'}
          asset={modal.type === 'edit' ? modal.asset : null}
          onClose={closeModal}
          onCreate={async (payload) => {
            const newAsset = await createAsset(payload);
            setAssets((current) => [
              {
                ...newAsset,
                categoryName:
                  newAsset.categoryName ?? categoryMap[newAsset.categoryId] ?? '-',
              },
              ...current,
            ]);
          }}
          onSave={async (assetToSave) => {
            const updatedAsset = await updateAsset(assetToSave.id, {
              name: assetToSave.name,
              categoryId: assetToSave.categoryId,
              status: assetToSave.status,
              location: assetToSave.location,
              description: assetToSave.description,
            });

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
          }}
        />
      )}

      <AssetBookingsModal
        isOpen={modal.type === 'bookings'}
        onClose={closeModal}
        asset={modal.type === 'bookings' ? modal.asset : null}
      />
      {isAdmin(user) && (
        <DeleteModal
          isOpen={modal.type === 'delete'}
          onClose={closeModal}
          item={modal.type === 'delete' ? modal.asset : null}
          getItemName={(asset) => asset.name}
          title={t('assets.delete.title')}
          description={t('assets.delete.description', {
            name: modal.type === 'delete' ? modal.asset.name : '',
          })}
          onConfirm={async () => {
            if (modal.type === 'delete') {
              await handleDelete(modal.asset);
              closeModal();
            }
          }}
        />
      )}
    </LayoutColumn>
  );
}
