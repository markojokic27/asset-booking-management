import * as React from 'react';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import { Table, type TableColumn } from '../../../components/ui/Table';

type UserBooking = {
  id: string;
  userId: string;
  asset: string;
  dateFrom: Date;
  dateTo: Date;
};

export type UserBookingsModalUser = {
  id: string;
  firstName: string;
  lastName: string;
};

export type UserBookingsModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserBookingsModalUser | null;
};

function getFullName(user: UserBookingsModalUser) {
  return `${user.firstName} ${user.lastName}`.trim();
}

export const UserBookingsModal: React.FC<UserBookingsModalProps> = ({
  isOpen,
  onClose,
  user,
}) => {
  if (!isOpen || !user) return null;

  const bookingColumns: TableColumn<UserBooking>[] = [
    {
      key: 'id',
      header: 'Booking ID',
      accessor: 'id',
    },
    {
      key: 'asset',
      header: 'Asset',
      accessor: 'asset',
    },
    {
      key: 'dates',
      header: 'Date',
      render: (booking) =>
        `${booking.dateFrom.toLocaleDateString()} - ${booking.dateTo.toLocaleDateString()}`,
    },
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="max-h-[90vh] w-full max-w-4xl overflow-hidden rounded-3xl bg-white p-6 shadow-xl bg-(--color-modal-overlay)">
        <div className="mb-6 flex items-start justify-between gap-4">
          <div>
            <h2 className="text-[10px] font-semibold uppercase tracking-[0.22em] text-(--color-table-head-text) opacity-50">
              Bookings
            </h2>
            <p className="block text-base font-black tracking-[0.06em]">
              {getFullName(user)}
            </p>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="inline-flex h-10 w-10 items-center justify-center rounded-full text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover)"
            aria-label="Close bookings modal"
          >
            <CloseOutlinedIcon fontSize="small" />
          </button>
        </div>

        <Table
          data={[]}
          columns={bookingColumns}
          getRowKey={(booking) => booking.id}
          className="w-full"
          emptyMessage="No bookings for this user."
        />
      </div>
    </div>
  );
};

