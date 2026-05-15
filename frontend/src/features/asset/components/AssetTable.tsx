// External packages
import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import  BarChartIcon from '@mui/icons-material/BarChart';

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
  onReport: (asset: AssetDto) => void;

};

export function AssetsTable({
  assets,
  categoryMap,
  onView,
  onEdit,
  onDelete,
  onBookings,
  onReport,
}: Props) {
  const { t } = useTranslation();

  const columns: TableColumn<AssetDto>[] = useMemo(
    () => [
      {
        key: 'id',
        header: t('assets.table.id'),
        accessor: 'id',
        cellClassName: 'font-medium',
      },
      {
        key: 'name',
        header: t('assets.table.assetName'),
        accessor: 'name',
      },
      {
        key: 'category',
        header: t('assets.table.category'),
        render: (asset) =>
          asset.categoryName ?? categoryMap[asset.categoryId] ?? '-',
      },
      {
        key: 'status',
        header: t('assets.table.status'),
        accessor: 'status',
      },
      {
        key: 'bookings',
        header: (
          <span className="sr-only">{t('assets.table.bookings')}</span>
        ),
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
            {t('assets.table.bookings')}
          </Button>
        ),
      },
      {
        key: 'actions', //TODO: puka gap, mora ce se style popravit"
        header: (
          <span className="sr-only">{t('assets.table.actionsSr')}</span>
        ),
        cellClassName: 'w-px whitespace-nowrap',
        render: (asset) => (
          <div className="flex items-center gap-1">
            <IconButton
              type="button"
              data-testid="view-asset-button"
              aria-label={t('assets.table.ariaView')}
              onClick={() => onView(asset)}
            >
              <VisibilityOutlinedIcon
                fontSize="small"
                className="pointer-events-none"
              />
            </IconButton>
            <IconButton
              type="button"
              aria-label={t('assets.table.ariaReport')}
              onClick={() => onReport(asset)}
            >
              <BarChartIcon fontSize="small" className="pointer-events-none" />
            </IconButton>

            <IconButton
              data-testid="edit-asset-button"
              type="button"
              aria-label={t('assets.table.ariaEdit')}
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
              aria-label={t('assets.table.ariaDelete')}
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
    ],
    [t, categoryMap, onView, onEdit, onDelete, onBookings, onReport]
  );

  return (
    <Table
      data={assets}
      columns={columns}
      getRowKey={(asset) => asset.id}
      className="w-full"
      rowClassName={(asset) =>
        asset.status === 'DELETED'
          ? 'bg-slate-100 opacity-60 hover:bg-slate-200'
          : undefined
      }
    />
  );
}
