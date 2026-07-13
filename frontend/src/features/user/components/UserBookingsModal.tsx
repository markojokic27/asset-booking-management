// external imports
import { useEffect, useMemo, useState, type FC } from 'react';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import { useTranslation } from 'react-i18next';

// components
import { Button } from '../../../components/ui/Button';
import { Table, type TableColumn } from '../../../components/ui/Table';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import { BookingStatusBadge } from '../../booking/components/BookingStatusBadge';

// api
import { getAllUserBookings } from '../../booking/api/bookingApi';

// utils
import { isBookingPastEnd } from '../../booking/utils/bookingLogic';
import { exportUserBookingsCsv } from '../utils/csv';

// types
import type { BookingWithRelations } from '../../booking/types';
import type { UserBookingsModalUser } from '../types';

// props for the UserBookingsModal component
export type UserBookingsModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserBookingsModalUser | null;
};

export const UserBookingsModal: FC<UserBookingsModalProps> = ({
  isOpen,
  onClose,
  user,
}) => {
  // translation function
  const { t } = useTranslation();

  // list of bookings
  const [bookings, setBookings] = useState<BookingWithRelations[]>([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isOpen || !user) return;

    const fetchBookings = async () => {
      try {
        setLoading(true);
        setError('');

        // fetch bookings from API
        const data = await getAllUserBookings(0, 100, user.id);
        setBookings(data.content);
      } catch {
        setError(t('users.errors.loadBookings'));
      } finally {
        setLoading(false);
      }
    };

    void fetchBookings();
  }, [isOpen, user?.id, t]);

  // columns for the bookings table
  const bookingColumns: TableColumn<BookingWithRelations>[] = useMemo(
    () => [
      {
        key: 'id',
        header: t('users.modals.bookings.table.columns.bookingId'),
        accessor: 'id',
      },
      {
        key: 'asset',
        header: t('users.modals.bookings.table.columns.asset'),
        render: (booking) => booking.asset.name,
      },
      {
        key: 'dates',
        header: t('users.modals.bookings.table.columns.date'),
        render: (booking) =>
          `${new Date(booking.bookingStart).toLocaleDateString()} - ${new Date(
            booking.bookingEnd
          ).toLocaleDateString()}`,
      },
      {
        key: 'status',
        header: t('users.modals.bookings.table.columns.status'),
        render: (booking) => <BookingStatusBadge status={booking.status} />,
      },
    ],
    [t]
  );

  // if the modal is not open or the user is not set, return null
  if (!isOpen || !user) return null;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('users.modals.bookings.ariaLabel')}
      size="lg"
      // title for the modal
      title={
        <div>
          <h2 className="text-xs font-semibold uppercase tracking-widest text-(--color-table-head-text) opacity-50">
            {t('users.modals.bookings.title')}
          </h2>
          <p className="block text-base font-black tracking-wider">{user.fullName}</p>
        </div>
      }
      headerRight={
        <div className="flex items-center gap-10">
          <Button
            data-testid="user-bookings-export-button"
            size="sm"
            variant="outline"
            iconLeft={<FileDownloadOutlinedIcon fontSize="small" />}
            disabled={loading || !!error || bookings.length === 0}
            onClick={() => exportUserBookingsCsv(bookings, user, t)}
          >
            {t('users.modals.bookings.export')}
          </Button>
          <IconButton
            data-testid="user-booking-close-button"
            onClick={onClose}
            aria-label={t('users.modals.bookings.closeAria')}
          >
            <CloseOutlinedIcon fontSize="small" />
          </IconButton>
        </div>
      }
    >
      {/* loading state */}
      {loading && (
        <p className="py-6 text-sm text-(--color-table-head-text)">
          {t('users.modals.bookings.loading')}
        </p>
      )}

      {/* error state */}
      {error && !loading && <p className="py-6 text-sm text-red-500">{error}</p>}

      {/* success state — scroll after ~8 rows */}
      {!loading && !error && (
        <div className="max-h-[min(29rem,calc(100vh-12rem))] overflow-y-auto [&_thead]:sticky [&_thead]:top-0 [&_thead]:z-10">
          <Table
            data={bookings}
            columns={bookingColumns}
            // key for the bookings
            getRowKey={(booking) => String(booking.id)}
            // style for past bookings
            rowClassName={(booking) =>
              isBookingPastEnd(booking) ? 'opacity-55' : undefined
            }
            className="w-full"
            // empty message for the bookings
            emptyMessage={t('users.modals.bookings.empty')}
          />
        </div>
      )}
    </Modal>
  );
};
