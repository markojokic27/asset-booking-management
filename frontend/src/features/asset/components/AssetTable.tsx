import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { Table, type TableColumn } from '../../../components/ui/Table';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';

import { type AssetDto } from '../../../features/asset/types';
import { Button } from '../../../components/ui/Button';

type Props = {
  assets: AssetDto[];
  onView: (asset: AssetDto) => void;
  onEdit: (asset: AssetDto) => void;
  onDelete?: (asset: AssetDto) => void;
  onBookings: (asset: AssetDto) => void;
};

export function AssetsTable({
  assets,
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
      render: (asset) => asset.categoryName ?? '-',
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
          <button
            type="button"
            className="table-action-btn"
            aria-label="View asset"
            onClick={() => onView(asset)}
          >
            <VisibilityOutlinedIcon fontSize="small" />
          </button>

          <button
            type="button"
            className="table-action-btn"
            aria-label="Edit asset"
            onClick={() => onEdit(asset)}
          >
            <EditOutlinedIcon fontSize="small" />
          </button>

          <button
            type="button"
            className="table-action-btn text-red-600 dark:text-red-400"
            aria-label="Delete asset"
            onClick={() => onDelete?.(asset)}
          >
            <DeleteOutlineIcon fontSize="small" />
          </button>
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
    />
  );
}
