import { LayoutColumn } from '../components/layout/Layout';
import { Table, type TableColumn } from '../components/ui/Table';
import type { AssetDto } from '../features/asset/types';

const assets: AssetDto[] = [];

export default function Assets() {
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
      <h1 className="text-3xl font-black leading-11 tracking-[0.2em] text-black dark:text-white">
        Assets
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
