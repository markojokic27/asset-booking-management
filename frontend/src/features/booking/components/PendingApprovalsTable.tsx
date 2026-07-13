// external packages
import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';

// components
import { Table, type TableColumn } from '../../../components/ui/Table';
import { ApprovalActionButtons } from './ApprovalActionButtons';
import { PendingApprovalDetailsModal } from './PendingApprovalDetailsModal';

// utils
import { formatBookingTime } from '../utils/bookingLogic';
import { getFullName } from '../../user/utils/users';

// types
import type { BookingWithRelations } from '../types';

// props of the component
type Props = {
  bookings: BookingWithRelations[];
  isLoading?: boolean;
  error?: string | null;
  selectedBooking: BookingWithRelations | null;
  onOpenBooking: (bookingId: number | string) => void;
  onCloseBooking: () => void;
  onApprove: (bookingId: number) => void;
  onReject: (bookingId: number) => void;
  processingId?: number | null;
  actionError?: string | null;
};

// pending approvals component
export function PendingApprovalsTable({
  bookings,
  isLoading,
  error,
  selectedBooking,
  onOpenBooking,
  onCloseBooking,
  onApprove,
  onReject,
  processingId = null,
  actionError = null,
}: Readonly<Props>) {
  const { t } = useTranslation();

  const columns: TableColumn<BookingWithRelations>[] = useMemo(
    () => [
      {
        key: 'id',
        header: t('approvals.table.bookingId'),
        accessor: 'id',
        cellClassName: 'font-medium',
      },
      {
        key: 'user',
        header: t('approvals.table.user'),
        render: (booking) => getFullName(booking.user),
      },
      {
        key: 'asset',
        header: t('approvals.table.asset'),
        render: (booking) => booking.asset.name,
      },
      {
        key: 'time',
        header: t('approvals.table.time'),
        render: (booking) =>
          formatBookingTime(booking.bookingStart, booking.bookingEnd),
      },
      {
        key: 'actions',
        header: (
          <span className="sr-only">{t('approvals.table.actionsSr')}</span>
        ),
        headerClassName: 'w-px whitespace-nowrap',
        cellClassName: 'w-px whitespace-nowrap',
        render: (booking) => (
          <ApprovalActionButtons
            bookingId={Number(booking.id)}
            onApprove={onApprove}
            onReject={onReject}
            processingId={processingId}
            size="sm"
          />
        ),
      },
    ],
    [t, onApprove, onReject, processingId]
  );

  let emptyMessage: string;
  if (isLoading) {
    emptyMessage = t('approvals.loading');
  } else if (error) {
    emptyMessage = error;
  } else {
    emptyMessage = t('approvals.empty');
  }

  return (
    <>
      <Table
        data={bookings}
        columns={columns}
        getRowKey={(booking) => String(booking.id)}
        className="w-full"
        onRowClick={(booking) => onOpenBooking(booking.id)}
        emptyMessage={emptyMessage}
      />

      <PendingApprovalDetailsModal
        booking={selectedBooking}
        onClose={onCloseBooking}
        onApprove={onApprove}
        onReject={onReject}
        processingId={processingId}
        actionError={actionError}
      />
    </>
  );
}
