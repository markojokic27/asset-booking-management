// external imports
import { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';

// components
import { Button } from '../../../components/ui/Button';
import { Table, type TableColumn } from '../../../components/ui/Table';
import { BookingStatusBadge } from './BookingStatusBadge';
import { CancelBookingModal } from './CancelBookingModal';

// utils
import {
  canCancelBooking,
  formatBookingTime,
  isBookingPastEnd,
} from '../utilis/bookingLogic';
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
  const [bookingToCancel, setBookingToCancel] =
    useState<BookingWithRelations | null>(null);

  const handleConfirmCancel = () => {
    // TODO: call cancel booking API when backend is ready
    setBookingToCancel(null);
  };

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
      {
        key: 'actions',
        header: (
          <span className="sr-only">{t('myBookings.table.actionsSr')}</span>
        ),
        headerClassName: 'w-px whitespace-nowrap',
        cellClassName: 'w-px whitespace-nowrap',
        render: (booking) =>
          canCancelBooking(booking) ? (
            <Button
              data-testid={`cancel-booking-${booking.id}`}
              type="button"
              size="sm"
              variant="outline"
              className="border-red-600 text-red-600 hover:border-red-700 hover:bg-red-50 hover:text-red-700 dark:border-red-500 dark:text-red-400 dark:hover:bg-red-950/40 dark:hover:text-red-300"
              onClick={() => setBookingToCancel(booking)}
            >
              {t('myBookings.actions.cancel')}
            </Button>
          ) : null,
      },
    ],
    [t]
  );

  return (
    <>
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

      <CancelBookingModal
        booking={bookingToCancel}
        onClose={() => setBookingToCancel(null)}
        onConfirm={handleConfirmCancel}
      />
    </>
  );
}
