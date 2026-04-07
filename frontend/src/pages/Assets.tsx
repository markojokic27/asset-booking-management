import { useState } from 'react';
import AddIcon from '@mui/icons-material/Add';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { Table, type TableColumn } from '../components/ui/Table';
import { Input } from '../components/ui/Input';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import { AssetModal } from '../features/asset/components/AssetModal';
import type { AssetDto } from '../features/asset/types';

const categories = [
  'Laptops',
  'Parking',
  'Desks',
  'Books',
  'Meeting room',
  'IT equipment',
] as const;

const assets: AssetDto[] = [
  {
    id: '1',
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

export default function Assets() {
  const [selectedCategory, setSelectedCategory] = useState<string>('Assets');
  const [isAssetModalOpen, setIsAssetModalOpen] = useState(false);
  const [activeAsset, setActiveAsset] = useState<AssetDto | null>(null);

  const filteredAssets =
    selectedCategory === 'Assets'
      ? assets
      : assets.filter((asset) => asset.categoryName === selectedCategory);

  const columns: TableColumn<AssetDto>[] = [
    {
      key: 'id',
      header: 'ID',
      accessor: 'id',
      cellClassName: 'font-medium',
    },
    {
      key: 'name',
      header: 'Asset name',
      accessor: 'name',
    },
    {
      key: 'category',
      header: 'Category',
      render: (asset) => asset.categoryName ?? '-',
    },
    {
      key: 'status',
      header: 'Status',
      accessor: 'status',
    },
    {
      key: 'actions',
      header: <span className="sr-only">Actions</span>,
      cellClassName: 'w-px whitespace-nowrap',
      render: (asset) => (
        <div className="flex items-center gap-1">
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
            aria-label="View asset"
            onClick={() => {
              setActiveAsset(asset);
              setIsAssetModalOpen(true);
            }}
          >
            <VisibilityOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </button>
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
            aria-label="Edit asset"
          >
            <EditOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </button>
          <button
            type="button"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-red-600 transition-colors hover:bg-(--color-table-row-hover) hover:text-red-700 active:scale-95 dark:text-red-400 dark:hover:text-red-300"
            aria-label="Delete asset"
          >
            <DeleteOutlineIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </button>
        </div>
      ),
    },
  ];

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
        <Button type="button" size="sm" iconLeft={<AddIcon fontSize="small" />}>
          Add asset
        </Button>
      </div>
      <div className="mt-6 h-px w-full bg-(--color-table-border)" />
      <div className="mt-6 flex w-full justify-end">
        <div className="w-50">
          <Input placeholder="Search assets..." />
        </div>
      </div>
      <div className="mt-6 w-full">
        <Table
          data={filteredAssets}
          columns={columns}
          getRowKey={(asset) => asset.id}
          className="w-full"
        />
      </div>

      <AssetModal
        isOpen={isAssetModalOpen}
        onClose={() => {
          setIsAssetModalOpen(false);
          setActiveAsset(null);
        }}
        asset={activeAsset}
      />
    </LayoutColumn>
  );
}
