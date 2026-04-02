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
    <LayoutColumn span={12} mdSpan={9} mdOffset={3} className="flex pt-35">
      <div className="w-full">
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
