// External imports
import { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';

// Components
import { Button } from '../../../components/ui/Button';
import { Table, type TableColumn } from '../../../components/ui/Table';
import { BookingStatusBadge } from './BookingStatusBadge';
import { CancelBookingModal } from './CancelBookingModal';
import { Toast } from '../../../components/ui/Toast';

// Utils
import {
  canCancelBooking,
  formatBookingTime,
  isBookingPastEnd,
} from '../utils/bookingLogic';
import { getFullName } from '../../user/utils/users';

// Types
import type { BookingWithRelations } from '../types';

// props of the component
type Props = {
  bookings: BookingWithRelations[];
  isLoading?: boolean;
  error?: string | null;
  onCancelBooking: (bookingId: number) => Promise<boolean>;
  isCancelling?: boolean;
  cancelError?: string | null;
  onClearCancelError?: () => void;
};

export function MyBookingsTable({
  bookings,
  isLoading,
  error,
  onCancelBooking,
  isCancelling = false,
  cancelError = null,
  onClearCancelError,
}: Readonly<Props>) {
  const { t } = useTranslation();
  const [bookingToCancel, setBookingToCancel] =
    useState<BookingWithRelations | null>(null);

  const openCancelModal = (booking: BookingWithRelations) => {
    onClearCancelError?.();
    setBookingToCancel(booking);
  };

  const closeCancelModal = () => {
    onClearCancelError?.();
    setBookingToCancel(null);
  };

  const handleConfirmCancel = async () => {
    if (!bookingToCancel) return;

    const success = await onCancelBooking(Number(bookingToCancel.id));
    if (success) {
      setBookingToCancel(null);
      Toast.success(t('layout.toast.bookingCancelled'));
    }
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
              onClick={() => openCancelModal(booking)}
              disabled={isCancelling}
            >
              {t('myBookings.actions.cancel')}
            </Button>
          ) : null,
      },
    ],
    [isCancelling, t]
  );

  let emptyMessage: string;
  if (isLoading) {
    emptyMessage = t('myBookings.loading');
  } else if (error) {
    emptyMessage = error;
  } else {
    emptyMessage = t('myBookings.empty');
  }

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
        emptyMessage={emptyMessage}
      />

      <CancelBookingModal
        booking={bookingToCancel}
        onClose={closeCancelModal}
        onConfirm={() => void handleConfirmCancel()}
        isProcessing={isCancelling}
        actionError={cancelError}
      />
    </>
  );
}
