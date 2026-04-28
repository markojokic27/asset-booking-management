import * as React from 'react';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import { useTranslation } from 'react-i18next';
import { Table, type TableColumn } from '../../../components/ui/Table';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';

type UserBooking = {
  id: string;
  userId: string;
  asset: string;
  dateFrom: Date;
  dateTo: Date;
};

export type UserBookingsModalUser = {
  id: number;
  fullName: string;
};

export type UserBookingsModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserBookingsModalUser | null;
};

export const UserBookingsModal: React.FC<UserBookingsModalProps> = ({
  isOpen,
  onClose,
  user,
}) => {
  const { t } = useTranslation();
  if (!isOpen || !user) return null;

  const bookingColumns: TableColumn<UserBooking>[] = [
    {
      key: 'id',
      header: t('users.modals.bookings.table.columns.bookingId'),
      accessor: 'id',
    },
    {
      key: 'asset',
      header: t('users.modals.bookings.table.columns.asset'),
      accessor: 'asset',
    },
    {
      key: 'dates',
      header: t('users.modals.bookings.table.columns.date'),
      render: (booking) =>
        `${booking.dateFrom.toLocaleDateString()} - ${booking.dateTo.toLocaleDateString()}`,
    },
  ];

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('users.modals.bookings.ariaLabel')}
      size="lg"
      title={
        <div>
          <h2 className="text-xs font-semibold uppercase tracking-widest text-(--color-table-head-text) opacity-50">
            {t('users.modals.bookings.title')}
          </h2>
          <p className="block text-base font-black tracking-wider">{user.fullName}</p>
        </div>
      }
      headerRight={
        <IconButton onClick={onClose} aria-label={t('users.modals.bookings.closeAria')}>
          <CloseOutlinedIcon fontSize="small" />
        </IconButton>
      }
    >
      <Table
        data={[]}
        columns={bookingColumns}
        getRowKey={(booking) => booking.id}
        className="w-full"
        emptyMessage={t('users.modals.bookings.empty')}
      />
    </Modal>
  );
};
