// external imports
import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';

// components
import { Table, type TableColumn } from '../../../components/ui/Table';
import { BookingStatusBadge } from './BookingStatusBadge';

// utils
import { formatBookingTime, isBookingPastEnd } from '../utilis/bookingLogic';
import { getFullName } from '../../user/utilis/users';

// types
import type { BookingWithRelations } from '../types';

// props of the component
type Props = {
  bookings: BookingWithRelations[];
  isLoading?: boolean;
  error?: string | null;
};

export function MyBookingsTable({ bookings, isLoading, error }: Props) {
  const { t } = useTranslation();

  const columns: TableColumn<BookingWithRelations>[] = useMemo(
    () => [
      {
        key: 'user',
        header: t('myBookings.table.user'),
        render: (booking) => getFullName(booking.user),
      },
      {
        key: 'asset',
        header: t('myBookings.table.asset'),
        render: (booking) => booking.asset.name,
      },
      {
        key: 'time',
        header: t('myBookings.table.time'),
        render: (booking) =>
          formatBookingTime(booking.bookingStart, booking.bookingEnd),
      },
      {
        key: 'status',
        header: t('myBookings.table.status'),
        render: (booking) => <BookingStatusBadge status={booking.status} />,
      },
    ],
    [t]
  );

  return (
    <Table
      data={bookings}
      columns={columns}
      getRowKey={(booking) => String(booking.id)}
      rowClassName={(booking) =>
        isBookingPastEnd(booking) ? 'opacity-55' : undefined
      }
      className="w-full"
      emptyMessage={
        isLoading
          ? t('myBookings.loading')
          : error
            ? error
            : t('myBookings.empty')
      }
    />
  );
}
