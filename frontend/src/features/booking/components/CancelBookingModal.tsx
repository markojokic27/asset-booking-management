// external imports
import CloseIcon from '@mui/icons-material/Close';
import { useTranslation } from 'react-i18next';

// components
import { Button } from '../../../components/ui/Button';
import { IconButton } from '../../../components/ui/IconButton';
import { BookingStatusBadge } from './BookingStatusBadge';

// utils
import { formatBookingTime } from '../utils/bookingLogic';

// types
import type { BookingWithRelations } from '../types';

type Props = {
  booking: BookingWithRelations | null;
  onClose: () => void;
  onConfirm: () => void;
  isProcessing?: boolean;
  actionError?: string | null;
};

export function CancelBookingModal({
  booking,
  onClose,
  onConfirm,
  isProcessing = false,
  actionError = null,
}: Props) {
  const { t } = useTranslation();

  if (!booking) return null;

  return (
    <div
      className="fixed inset-0 z-50 overflow-y-auto p-4 sm:p-6"
      role="dialog"
      aria-modal="true"
      aria-label={t('myBookings.cancelModal.ariaLabel', { id: booking.id })}
    >
      <button
        type="button"
        className="fixed inset-0 cursor-default bg-(--color-modal-overlay)"
        aria-label={t('myBookings.cancelModal.closeAria')}
        onClick={onClose}
      />
      <div className="relative z-10 flex min-h-full items-center justify-center">
        <div className="my-auto flex max-h-[calc(100vh-2rem)] w-full max-w-lg flex-col overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
          <div className="flex shrink-0 items-center justify-between gap-4 px-6 pt-5 pb-3 sm:px-8 sm:pt-6 sm:pb-4">
            <h2 className="text-2xl font-bold">
              {t('myBookings.cancelModal.title')}
            </h2>
            <IconButton
              onClick={onClose}
              aria-label={t('myBookings.cancelModal.closeAria')}
            >
              <CloseIcon className="pointer-events-none" />
            </IconButton>
          </div>

          <div className="mx-6 h-px shrink-0 bg-(--color-table-border) sm:mx-8" />

          <div className="min-h-0 flex-1 overflow-y-auto px-6 py-5 sm:px-8 sm:py-6">
            <p className="mb-5 text-(--color-modal-label)">
              {t('myBookings.cancelModal.description')}
            </p>

            <div className="space-y-3 rounded-lg border border-(--color-table-border) p-4">
              <div>
                <p className="text-sm text-(--color-modal-label)">
                  {t('myBookings.cancelModal.fields.asset')}
                </p>
                <p className="font-medium">{booking.asset.name}</p>
              </div>

              <div>
                <p className="text-sm text-(--color-modal-label)">
                  {t('myBookings.cancelModal.fields.time')}
                </p>
                <p className="font-medium">
                  {formatBookingTime(booking.bookingStart, booking.bookingEnd)}
                </p>
              </div>

              <div>
                <p className="text-sm text-(--color-modal-label)">
                  {t('myBookings.cancelModal.fields.status')}
                </p>
                <BookingStatusBadge status={booking.status} />
              </div>

              {booking.notes && (
                <div>
                  <p className="text-sm text-(--color-modal-label)">
                    {t('myBookings.cancelModal.fields.notes')}
                  </p>
                  <p className="whitespace-pre-wrap">{booking.notes}</p>
                </div>
              )}
            </div>
          </div>

          <div className="mx-6 h-px shrink-0 bg-(--color-table-border) sm:mx-8" />

          <div className="shrink-0 px-6 py-4 sm:px-8 sm:py-5">
            <div className="flex flex-col gap-4">
              {actionError && (
                <p
                  className="text-sm text-red-600 dark:text-red-400"
                  role="alert"
                >
                  {actionError}
                </p>
              )}
              <div className="flex flex-wrap items-center justify-between gap-3">
                <Button
                  data-testid="keep-booking-button"
                  variant="outline"
                  size="md"
                  onClick={onClose}
                  disabled={isProcessing}
                >
                  {t('myBookings.cancelModal.keepBooking')}
                </Button>
                <Button
                  data-testid="confirm-cancel-booking-button"
                  variant="danger"
                  size="md"
                  onClick={onConfirm}
                  disabled={isProcessing}
                >
                  {isProcessing
                    ? t('myBookings.cancelModal.processing')
                    : t('myBookings.cancelModal.confirmCancel')}
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
