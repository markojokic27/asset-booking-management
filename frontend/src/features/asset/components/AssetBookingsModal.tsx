import * as React from 'react';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';
import { Table, type TableColumn } from '../../../components/ui/Table';
import type { AssetDto } from '../types';
import type { BookingDto } from '../../booking/types';
import { getAllAssetBookings } from '../../booking/api/bookingApi';

export type BookingsModalProps = {
  isOpen: boolean;
  onClose: () => void;
  asset: AssetDto | null;
};

export const AssetBookingsModal: React.FC<BookingsModalProps> = ({
  isOpen,
  onClose,
  asset,
}) => {
  const [bookings, setBookings] = React.useState<BookingDto[]>([]);
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');

  React.useEffect(() => {
    if (!isOpen || !asset) return;

    const fetchBookings = async () => {
      try {
        setLoading(true);
        setError('');

        const data = await getAllAssetBookings(0, 10, asset.id);
        setBookings(data.content);
      } catch (error) {
        console.error(error);
        setError('Failed to load bookings.');
      } finally {
        setLoading(false);
      }
    };

    fetchBookings();
  }, [isOpen, asset?.id]);

  if (!isOpen || !asset) return null;

  const bookingColumns: TableColumn<BookingDto>[] = [
    {
      key: 'id',
      header: 'Booking ID',
      accessor: 'id',
    },
    // booking treba mapirati username preko id-a ili da BE vrati i username
    {
      key: 'user',
      header: 'User',
      render: (booking) => booking.userId,
    },
    {
      key: 'dates',
      header: 'Date',
      render: (booking) =>
        `${new Date(booking.bookingStart).toLocaleDateString()} - ${new Date(
          booking.bookingEnd
        ).toLocaleDateString()}`,
    },
    {
      key: 'notes',
      header: 'Note',
      accessor: 'notes',
    },
    {
      key: 'status',
      header: 'Status',
      accessor: 'status',
    },
  ];


  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="max-h-[90vh] w-full max-w-4xl overflow-hidden rounded-3xl border border-(--color-table-border) bg-(--color-table-surface) p-6 text-(--color-table-text) shadow-xl">
        <div className="mb-6 flex items-start justify-between gap-4">
          <div>
            <h2 className="text-[10px] font-semibold tracking-[0.22em] text-(--color-table-head-text) uppercase opacity-50">
              Bookings
            </h2>
            <p className="block text-base font-black tracking-[0.06em]">
              {asset.name}
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

        {loading && (
          <p className="py-6 text-sm text-(--color-table-head-text)">
            Loading bookings...
          </p>
        )}

        {error && !loading && (
          <p className="py-6 text-sm text-red-500">{error}</p>
        )}

        {!loading && !error && (
          <Table
            data={bookings}
            columns={bookingColumns}
            getRowKey={(booking) => booking.id}
            className="w-full"
            emptyMessage="No bookings for this asset."
          />
        )}
      </div>
    </div>
  );
};