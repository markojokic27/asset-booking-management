// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';
import CloseOutlinedIcon from '@mui/icons-material/CloseOutlined';

// Components
import { Table, type TableColumn } from '../../../components/ui/Table';

// Types
import type { AssetDto } from '../types';
import type { BookingDto } from '../../booking/types';

// API
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
  const { t } = useTranslation();
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
        setError(t('assets.errors.loadBookings'));
      } finally {
        setLoading(false);
      }
    };

    fetchBookings();
  }, [isOpen, asset?.id, t]);

  const bookingColumns: TableColumn<BookingDto>[] = React.useMemo(
    () => [
      {
        key: 'id',
        header: t('assets.modals.bookings.colId'),
        accessor: 'id',
      },
      // booking treba mapirati username preko id-a ili da BE vrati i username
      {
        key: 'user',
        header: t('assets.modals.bookings.colUser'),
        render: (booking) => booking.userId,
      },
      {
        key: 'dates',
        header: t('assets.modals.bookings.colDate'),
        render: (booking) =>
          `${new Date(booking.bookingStart).toLocaleDateString()} - ${new Date(
            booking.bookingEnd
          ).toLocaleDateString()}`,
      },
      {
        key: 'notes',
        header: t('assets.modals.bookings.colNote'),
        accessor: 'notes',
      },
      {
        key: 'status',
        header: t('assets.modals.fields.status'),
        accessor: 'status',
      },
    ],
    [t]
  );

  if (!isOpen || !asset) return null;


  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <div className="max-h-[90vh] w-full max-w-4xl overflow-hidden rounded-3xl border border-(--color-table-border) bg-(--color-table-surface) p-6 text-(--color-table-text) shadow-xl">
        <div className="mb-6 flex items-start justify-between gap-4">
          <div>
            <h2 className="text-[10px] font-semibold tracking-[0.22em] text-(--color-table-head-text) uppercase opacity-50">
              {t('assets.table.bookings')}
            </h2>
            <p className="block text-base font-black tracking-[0.06em]">
              {asset.name}
            </p>
          </div>

          <button data-testid="close-asset-bookings-modal"
            type="button"
            onClick={onClose}
            className="inline-flex h-10 w-10 items-center justify-center rounded-full text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover)"
            aria-label={t('assets.modals.bookings.closeAria')}
          >
            <CloseOutlinedIcon fontSize="small" />
          </button>
        </div>

        {loading && (
          <p className="py-6 text-sm text-(--color-table-head-text)">
            {t('assets.modals.bookings.loading')}
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
            emptyMessage={t('assets.modals.bookings.empty')}
          />
        )}
      </div>
    </div>
  );
};