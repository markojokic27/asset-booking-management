import * as React from 'react';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import { Table, type TableColumn } from '../../../components/ui/Table';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import type { UserDto } from '../types';

type UserBooking = {
  id: string;
  userId: string;
  asset: string;
  dateFrom: Date;
  dateTo: Date;
};

export type UserBookingsModalUser = Pick<UserDto, 'id' | 'name' | 'surname'>;

export type UserBookingsModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserBookingsModalUser | null;
};

function getFullName(user: UserBookingsModalUser) {
  return `${user.name} ${user.surname}`.trim();
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
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel="Bookings"
      size="lg"
      title={
        <div>
          <h2 className="text-xs font-semibold uppercase tracking-widest text-(--color-table-head-text) opacity-50">
            Bookings
          </h2>
          <p className="block text-base font-black tracking-wider">{getFullName(user)}</p>
        </div>
      }
      headerRight={
        <IconButton onClick={onClose} aria-label="Close bookings modal">
          <CloseOutlinedIcon fontSize="small" />
        </IconButton>
      }
    >
      <Table
        data={[]}
        columns={bookingColumns}
        getRowKey={(booking) => booking.id}
        className="w-full"
        emptyMessage="No bookings for this user."
      />
    </Modal>
  );
};

