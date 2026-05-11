// Components
import { Table, type TableColumn } from '../../../components/ui/Table';
import { Button } from '../../../components/ui/Button';
import { Link } from 'react-router-dom';

// Types
import { type AssetDto } from '../../asset/types';

type Props = {
  assets: AssetDto[];
  isLoading?: boolean;
  error?: string | null;
  className?: string;
};

export function BookingTable({ assets, isLoading, error, className }: Props) {
  const columns: TableColumn<AssetDto>[] = [
    {
      key: 'id',
      header: 'ID',
      accessor: 'id',
      cellClassName: 'font-medium',
    },
    {
      key: 'name',
      header: 'Name',
      accessor: 'name',
    },
    {
      key: 'status',
      header: 'Status',
      accessor: 'status',
    },
    {
      key: 'approval',
      header: 'Approval',
      // ako nemaš approval u AssetDto → moraš render
      render: () => '-',
    },
    {
      key: 'book',
      header: <span className="sr-only">Book</span>,
      headerClassName: 'w-px whitespace-nowrap',
      cellClassName: 'w-px whitespace-nowrap',
      render: (asset) => (
        <Link to={`/assets/${asset.id}/bookings`}>
          <Button size="sm">Book</Button>
        </Link>
      ),
    },
  ];

  return (
    <Table
      data={assets}
      columns={columns}
      getRowKey={(asset) => asset.id}
      className={`w-full ${className}`}
      emptyMessage={
        isLoading ? 'Loading assets...' : error ? error : 'No assets available.'
      }
    />
  );
}
