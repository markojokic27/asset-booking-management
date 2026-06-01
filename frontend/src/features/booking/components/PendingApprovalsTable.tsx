// external packages
import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';

// components
import { Table, type TableColumn } from '../../../components/ui/Table';

// utils
import { formatBookingTime } from '../utilis/bookingLogic';
import { getFullName } from '../../user/utilis/users';

// types
import type { BookingWithRelations } from '../types';

// props of the component
type Props = {
  bookings: BookingWithRelations[];
  isLoading?: boolean;
  error?: string | null;
};

// Pending Approvals Table component
export function PendingApprovalsTable({ bookings, isLoading, error }: Props) {
  const { t } = useTranslation();

  const columns: TableColumn<BookingWithRelations>[] = useMemo(
    () => [
      // booking id column
      {
        key: 'id',
        header: t('approvals.table.bookingId'),
        accessor: 'id',
        cellClassName: 'font-medium',
      },
      // user column
      {
        key: 'user',
        header: t('approvals.table.user'),
        render: (booking) => getFullName(booking.user),
      },
      // asset column
      {
        key: 'asset',
        header: t('approvals.table.asset'),
        render: (booking) => booking.asset.name,
      },
      // time column
      {
        key: 'time',
        header: t('approvals.table.time'),
        render: (booking) =>
          formatBookingTime(booking.bookingStart, booking.bookingEnd),
      },
    ],
    [t] // if the translation changes, the columns will be recalculated
  );

  return (
    // table component
    <Table
      data={bookings}
      columns={columns}
      getRowKey={(booking) => String(booking.id)}
      className="w-full"
      emptyMessage={
        isLoading
          ? t('approvals.loading')
          : error
            ? error
            : t('approvals.empty')
      }
    />
  );
}
