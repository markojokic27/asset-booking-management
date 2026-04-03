import { useState } from 'react';
import AddIcon from '@mui/icons-material/Add';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { Table, type TableColumn } from '../components/ui/Table';
import { Input } from '../components/ui/Input';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import type { AssetDto } from '../features/asset/types';

const assets: AssetDto[] = [];
const categories = [
  'Laptops',
  'Parking',
  'Desks',
  'Books',
  'Meeting room',
  'IT equipment',
] as const;

export default function Assets() {
  const [selectedCategory, setSelectedCategory] = useState<string>('Assets');

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
      key: 'model',
      header: 'Model',
      accessor: 'code',
    },
    {
      key: 'status',
      header: 'Status',
      accessor: 'status',
    },
    {
      key: 'edit',
      header: 'Edit',
      render: () => 'Edit',
    },
    {
      key: 'delete',
      header: 'Delete',
      render: () => 'Delete',
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
        <h1 className="text-3xl font-black leading-11 tracking-[0.2em] text-black dark:text-white">
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
          data={assets}
          columns={columns}
          getRowKey={(asset) => asset.id}
          className="w-full"
        />
      </div>
    </LayoutColumn>
  );
}
