import { useState, useMemo } from 'react';
import AddIcon from '@mui/icons-material/Add';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { SearchInput } from '../components/ui/SearchBar';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { AssetEditModal } from '../features/asset/components/AssetEditModal';
import { AssetModal } from '../features/asset/components/AssetModal';
import { AssetBookingsModal } from '../features/asset/components/AssetBookingsModal';
import { categories, type AssetDto } from '../features/asset/types';
import { AssetAddModal } from '../features/asset/components/AssetAddModal';
import { AssetsTable } from '../features/asset/components/AssetTable';

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
type ModalState =
  | { type: 'none' }
  | { type: 'view'; asset: AssetDto }
  | { type: 'edit'; asset: AssetDto }
  | { type: 'bookings'; asset: AssetDto }
  | { type: 'add' };

export default function Assets() {
  const [selectedCategory, setSelectedCategory] = useState<string>('Assets');
  const [assets, setAssets] = useState<AssetDto[]>(initialAssets);
  const [search, setSearch] = useState('');
  const [modal, setModal] = useState<ModalState>({ type: 'none' });

  const filteredAssets = useMemo(() => {
    const byCategory =
      selectedCategory === 'Assets'
        ? assets
        : assets.filter((a) => a.categoryName === selectedCategory);

    return byCategory.filter((a) =>
      a.name.toLowerCase().includes(search.trim().toLowerCase())
    );
  }, [assets, selectedCategory, search]);

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
        categories={categories}
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
        <AssetsTable
          assets={filteredAssets}
          onView={(asset) => setModal({ type: 'view', asset })}
          onEdit={(asset) => setModal({ type: 'edit', asset })}
          onBookings={(asset) => setModal({ type: 'bookings', asset })}
          onDelete={handleDelete}
        />
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
          setAssets((current) =>
            current.map((a) => (a.id === updatedAsset.id ? updatedAsset : a))
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
          setAssets((current) => [newAsset, ...current]);
          closeModal();
        }}
      />
    </LayoutColumn>
  );
}
