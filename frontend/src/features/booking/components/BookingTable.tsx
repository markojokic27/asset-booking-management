// External packages
import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';

// Components
import { Table, type TableColumn } from '../../../components/ui/Table';
import { Button } from '../../../components/ui/Button';

// Types
import { type AssetDto, type AssetStatus } from '../../asset/types';

type Props = {
  assets: AssetDto[];
  isLoading?: boolean;
  error?: string | null;
  className?: string;
};

export function BookingTable({ assets, isLoading, error, className }: Props) {
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
        key: 'status',
        header: t('assets.table.status'),
        render: (asset) =>
          t(`assets.status.${asset.status}` as `assets.status.${AssetStatus}`),
      },
      {
        key: 'approval',
        header: t('bookings.table.approval'),
        render: () => '-',
      },
      {
        key: 'book',
        header: (
          <span className="sr-only">{t('bookings.table.bookSr')}</span>
        ),
        headerClassName: 'w-px whitespace-nowrap',
        cellClassName: 'w-px whitespace-nowrap',
        render: (asset) => (
          <Link to={`/assets/${asset.id}/bookings`}>
            <Button data-testid="book-button" size="sm">{t('bookings.table.book')}</Button>
          </Link>
        ),
      },
    ],
    [t]
  );

  return (
    <Table
      data={assets}
      columns={columns}
      getRowKey={(asset) => asset.id}
      className={`w-full ${className}`}
      emptyMessage={
        isLoading
          ? t('bookings.empty.loading')
          : error
            ? error
            : t('bookings.empty.noAssets')
      }
    />
  );
}
