// Components
import { Modal } from '../../../components/ui/Modal';
import { Button } from '../../../components/ui/Button';
import { IconButton } from '../../../components/ui/IconButton';
import CloseIcon from '@mui/icons-material/Close';

// Types
import type { BookingWithRelations } from '../types';

// Hooks
import { useTranslation } from 'react-i18next';

type Props = {
  booking: BookingWithRelations | null;
  onClose: () => void;
  currentUserId: number | undefined;
  refetch: () => void | Promise<void>;
  openCancelModal: (booking: BookingWithRelations) => void;
};

const STATUS_COLORS: Record<string, string> = {
  APPROVED: '#22c55e',
  PENDING: '#f59e0b',
  REJECTED: '#ef4444',
  CANCELLED: '#6b7280',
};

export function BookingDetailsModal({
  booking,
  onClose,
  currentUserId,
  openCancelModal,
}: Props) {
  const { t } = useTranslation();

  if (!booking) return null;

  const formatDateTime = (value: string | Date) => {
    const d = new Date(value);
    const day = d.getDate();
    const month = d.getMonth() + 1;
    const year = d.getFullYear();
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');
    return `${day}.${month}.${year}. ${hours}:${minutes}`;
  };

  const canCancel = () => {
    const isValidStatus =
      booking.status === 'APPROVED' || booking.status === 'PENDING';
    const isOwner = currentUserId === booking.user.id;
    const isPastBooking = new Date() > new Date(booking.bookingEnd);

    return isValidStatus && isOwner && !isPastBooking;
  };

  return (
    <Modal
      isOpen={true}
      onClose={onClose}
      title={
        <div className="flex w-full items-center justify-between">
          {' '}
          <div className="flex items-center gap-8">
            <h2 className="text-2xl font-bold">
              {t('bookings.bookingDetailsModal.booking')} #{booking.id}
            </h2>
            {canCancel() && (
              <Button
                variant="danger"
                data-testid={`cancel-booking-${booking.id}`}
                type="button"
                size="sm"
                onClick={() => openCancelModal(booking)}
              >
                {t('myBookings.actions.cancel')}
              </Button>
            )}
          </div>{' '}
          <IconButton
            onClick={onClose}
            aria-label={t('myBookings.cancelModal.closeAria')}
          >
            <CloseIcon className="pointer-events-none" />
          </IconButton>
        </div>
      }
    >
      <div className="space-y-6">
        <div className="rounded-lg border border-(--color-table-border) p-4">
          <h3 className="mb-3 text-lg font-semibold tracking-wide text-gray-500 uppercase">
            {t('bookings.bookingDetailsModal.user')}
          </h3>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.name')}
              </p>
              <p className="text-lg font-medium">
                {booking.user.name} {booking.user.surname}
              </p>
            </div>

            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.role')}
              </p>
              <p className="text-lg font-medium">{booking.user.role}</p>
            </div>

            <div className="col-span-2">
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.email')}
              </p>
              <p className="text-lg font-medium">{booking.user.email}</p>
            </div>
          </div>
        </div>

        <div className="rounded-lg border border-(--color-table-border) p-4">
          <h3 className="mb-3 text-lg font-semibold tracking-wide text-gray-500 uppercase">
            {t('bookings.bookingDetailsModal.asset')}
          </h3>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.name')}
              </p>
              <p className="text-lg font-medium">{booking.asset.name}</p>
            </div>

            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.status')}
              </p>
              <p className="text-lg font-medium">{booking.asset.status}</p>
            </div>

            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.category')}
              </p>
              <p className="text-lg font-medium">
                {booking.asset.category.name}
              </p>
            </div>

            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.location')}
              </p>
              <p className="text-lg font-medium">{booking.asset.location}</p>
            </div>
          </div>
        </div>

        <div className="rounded-lg border border-(--color-table-border) p-4">
          <h3 className="mb-3 text-lg font-semibold tracking-wide text-gray-500 uppercase">
            {t('bookings.bookingDetailsModal.booking')}
          </h3>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.status')}
              </p>

              <span
                className="inline-flex rounded px-2 py-1 font-medium text-white"
                style={{
                  backgroundColor: STATUS_COLORS[booking.status] ?? '#6b7280',
                }}
              >
                {booking.status}
              </span>
            </div>

            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.bookingId')}
              </p>
              <p className="text-lg font-medium">#{booking.id}</p>
            </div>

            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.start')}
              </p>
              <p className="text-lg font-medium">
                {formatDateTime(booking.bookingStart)}
              </p>
            </div>

            <div>
              <p className="text-gray-500">
                {t('bookings.bookingDetailsModal.end')}
              </p>
              <p className="text-lg font-medium">
                {formatDateTime(booking.bookingEnd)}
              </p>
            </div>
          </div>
        </div>

        {booking.notes && (
          <div className="rounded-lg border border-(--color-table-border) p-4">
            <h3 className="mb-3 text-lg font-semibold tracking-wide text-gray-500 uppercase">
              {t('bookings.bookingDetailsModal.notes')}
            </h3>

            <p className="whitespace-pre-wrap">{booking.notes}</p>
          </div>
        )}
      </div>
    </Modal>
  );
}
