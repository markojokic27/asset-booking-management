// External packages
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';

// Components
import { Table, type TableColumn } from '../../../components/ui/Table';
import { IconButton } from '../../../components/ui/IconButton';
import { Button } from '../../../components/ui/Button';

// Types
import { type AssetDto } from '../../../features/asset/types';

type Props = {
  assets: AssetDto[];
  categoryMap: Record<number, string>;
  onView: (asset: AssetDto) => void;
  onEdit: (asset: AssetDto) => void;
  onDelete: (asset: AssetDto) => void;
  onBookings: (asset: AssetDto) => void;
};

export function AssetsTable({
  assets,
  categoryMap,
  onView,
  onEdit,
  onDelete,
  onBookings,
}: Props) {
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
      render: (asset) =>
        asset.categoryName ?? categoryMap[asset.categoryId] ?? '-',
    },
    {
      key: 'status',
      header: 'Status',
      accessor: 'status',
    },
    {
      key: 'bookings',
      header: <span className="sr-only">Bookings</span>,
      headerClassName: 'w-px whitespace-nowrap',
      cellClassName: 'w-px whitespace-nowrap',
      render: (asset) => (
        <Button
          size="sm"
          variant="solid"
          iconLeft={<CalendarTodaySharpIcon fontSize="small" />}
          className="shadow-none"
          onClick={() => onBookings(asset)}
        >
          Bookings
        </Button>
      ),
    },
    {
      key: 'actions', //TODO: puka gap, mora ce se style popravit"
      header: <span className="sr-only">Actions</span>,
      cellClassName: 'w-px whitespace-nowrap',
      render: (asset) => (
        <div className="flex items-center gap-1">
          <IconButton
            type="button"
            data-testid="view-asset-button"
            aria-label="View asset"
            onClick={() => onView(asset)}
          >
            <VisibilityOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </IconButton>
          <IconButton
            data-testid="edit-asset-button"
            type="button"
            aria-label="Edit user"
            disabled={asset.status === 'DELETED'}
            onClick={() => onEdit(asset)}
          >
            <EditOutlinedIcon
              fontSize="small"
              className="pointer-events-none"
            />
          </IconButton>
          <IconButton
            data-testid="delete-asset-button"
            type="button"
            variant="danger"
            aria-label="Delete user"
            disabled={asset.status === 'DELETED'}
          >
            <DeleteOutlineIcon
              fontSize="small"
              onClick={() => onDelete?.(asset)}
            />
          </IconButton>
        </div>
      ),
    },
  ];

  return (
    <Table
      data={assets}
      columns={columns}
      getRowKey={(asset) => asset.id}
      className="w-full"
      rowClassName={(asset) =>
      asset.status === 'DELETED'
      ? 'bg-red-200 opacity-60 hover:bg-red-300'
      : undefined
  }
    />
  );
}
