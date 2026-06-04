// external packages
import { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';

// components
import { Table, type TableColumn } from '../../../components/ui/Table';
import { ApprovalActionButtons } from './ApprovalActionButtons';
import { PendingApprovalDetailsModal } from './PendingApprovalDetailsModal';

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
  onApprove,
  onReject,
  processingId = null,
  actionError = null,
}: Props) {
  const { t } = useTranslation();
  const [selectedBooking, setSelectedBooking] =
    useState<BookingWithRelations | null>(null);

  useEffect(() => {
    if (
      selectedBooking &&
      !bookings.some((booking) => booking.id === selectedBooking.id)
    ) {
      setSelectedBooking(null);
    }
  }, [bookings, selectedBooking]);

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
      // actions column
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

  return (
    <>
      {/* pending approvals table */}
      <Table
        data={bookings}
        columns={columns}
        getRowKey={(booking) => String(booking.id)}
        className="w-full"
        onRowClick={(booking) => setSelectedBooking(booking)}
        emptyMessage={
          isLoading
            ? t('approvals.loading')
            : error
              ? error
              : t('approvals.empty')
        }
      />

      {/* pending approval details modal */}
      <PendingApprovalDetailsModal
        booking={selectedBooking}
        onClose={() => setSelectedBooking(null)}
        onApprove={onApprove}
        onReject={onReject}
        processingId={processingId}
        actionError={actionError}
      />
    </>
  );
}
