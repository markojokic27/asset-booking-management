import { useState } from 'react';
import { LayoutColumn } from '../components/layout/Layout';
import { Table, type TableColumn } from '../components/ui/Table';
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
      <h1 className="mt-8 text-3xl font-black leading-11 tracking-[0.2em] text-black dark:text-white">
        {selectedCategory}
      </h1>
      <div className="mt-6 h-px w-full bg-[var(--color-table-border)]" />
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
