import { useEffect, useRef } from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { useTranslation } from 'react-i18next';

import { Button } from '../../../components/ui/Button';
import { IconButton } from '../../../components/ui/IconButton';
import { getFullName } from '../../user/utils/users';
import { formatBookingTime } from '../utils/bookingLogic';
import type { BookingWithRelations } from '../types';
import { ApprovalActionButtons } from './ApprovalActionButtons';

type Props = {
  booking: BookingWithRelations | null;
  onClose: () => void;
  onApprove: (bookingId: number) => void;
  onReject: (bookingId: number) => void;
  processingId?: number | null;
  actionError?: string | null;
};

const STATUS_COLORS: Record<string, string> = {
  APPROVED: '#22c55e',
  PENDING: '#f59e0b',
  REJECTED: '#ef4444',
  CANCELLED: '#6b7280',
};

export function PendingApprovalDetailsModal({
  booking,
  onClose,
  onApprove,
  onReject,
  processingId = null,
  actionError = null,
}: Readonly<Props>) {
  const { t } = useTranslation();
  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;

    if (booking) {
      if (!dialog.open) dialog.showModal();
    } else if (dialog.open) {
      dialog.close();
    }
  }, [booking]);

  if (!booking) return null;

  const statusKey = `bookings.status.${booking.status.toLowerCase()}` as
    | 'bookings.status.pending'
    | 'bookings.status.approved'
    | 'bookings.status.rejected'
    | 'bookings.status.cancelled'
    | 'bookings.status.completed';

  return (
    <dialog
      ref={dialogRef}
      className="fixed inset-0 z-50 m-0 h-full max-h-full w-full max-w-full overflow-y-auto border-0 bg-transparent p-4 backdrop:bg-transparent sm:p-6"
      aria-label={t('approvals.modal.ariaLabel', { id: booking.id })}
      onCancel={(e) => {
        e.preventDefault();
        onClose();
      }}
    >
      <button
        type="button"
        className="fixed inset-0 cursor-default bg-(--color-modal-overlay)"
        aria-label={t('approvals.modal.closeAria')}
        onClick={onClose}
      />
      <div className="relative z-10 flex min-h-full items-center justify-center">
        <div className="my-auto flex max-h-[calc(100vh-2rem)] w-full max-w-2xl flex-col overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
          <div className="flex shrink-0 items-center justify-between gap-4 px-6 pt-5 pb-3 sm:px-8 sm:pt-6 sm:pb-4">
            <h2 className="text-2xl font-bold">
              {t('approvals.modal.title', { id: booking.id })}
            </h2>
            <IconButton
              onClick={onClose}
              aria-label={t('approvals.modal.closeAria')}
            >
              <CloseIcon className="pointer-events-none" />
            </IconButton>
          </div>

          <div className="mx-6 h-px shrink-0 bg-(--color-table-border) sm:mx-8" />

          <div className="min-h-0 flex-1 overflow-y-auto px-6 py-5 sm:px-8 sm:py-6">
            <div className="space-y-4">
              <div className="rounded-lg border border-(--color-table-border) p-3 sm:p-4">
                <h3 className="mb-2 text-sm font-semibold tracking-wide text-(--color-modal-label) uppercase">
                  {t('approvals.modal.sections.user')}
                </h3>
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.name')}
                    </p>
                    <p className="font-medium">{getFullName(booking.user)}</p>
                  </div>
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.role')}
                    </p>
                    <p className="font-medium">{booking.user.role}</p>
                  </div>
                  <div className="col-span-2">
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.email')}
                    </p>
                    <p className="font-medium">{booking.user.email}</p>
                  </div>
                </div>
              </div>

              <div className="rounded-lg border border-(--color-table-border) p-3 sm:p-4">
                <h3 className="mb-2 text-sm font-semibold tracking-wide text-(--color-modal-label) uppercase">
                  {t('approvals.modal.sections.asset')}
                </h3>
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.name')}
                    </p>
                    <p className="font-medium">{booking.asset.name}</p>
                  </div>
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.status')}
                    </p>
                    <p className="font-medium">{booking.asset.status}</p>
                  </div>
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.category')}
                    </p>
                    <p className="font-medium">{booking.asset.category.name}</p>
                  </div>
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.location')}
                    </p>
                    <p className="font-medium">{booking.asset.location}</p>
                  </div>
                  {booking.asset.description && (
                    <div className="col-span-2">
                      <p className="text-sm text-(--color-modal-label)">
                        {t('approvals.modal.fields.description')}
                      </p>
                      <p className="font-medium">{booking.asset.description}</p>
                    </div>
                  )}
                </div>
              </div>

              <div className="rounded-lg border border-(--color-table-border) p-3 sm:p-4">
                <h3 className="mb-2 text-sm font-semibold tracking-wide text-(--color-modal-label) uppercase">
                  {t('approvals.modal.sections.booking')}
                </h3>
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.status')}
                    </p>
                    <span
                      className="inline-flex rounded px-2 py-1 text-sm font-medium text-white"
                      style={{
                        backgroundColor:
                          STATUS_COLORS[booking.status] ?? '#6b7280',
                      }}
                    >
                      {t(statusKey, { defaultValue: booking.status })}
                    </span>
                  </div>
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.bookingId')}
                    </p>
                    <p className="font-medium">#{booking.id}</p>
                  </div>
                  <div>
                    <p className="text-sm text-(--color-modal-label)">
                      {t('approvals.modal.fields.time')}
                    </p>
                    <p className="font-medium">
                      {formatBookingTime(
                        booking.bookingStart,
                        booking.bookingEnd
                      )}
                    </p>
                  </div>
                </div>
              </div>

              {booking.notes && (
                <div className="rounded-lg border border-(--color-table-border) p-3 sm:p-4">
                  <h3 className="mb-2 text-sm font-semibold tracking-wide text-(--color-modal-label) uppercase">
                    {t('approvals.modal.sections.notes')}
                  </h3>
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
                <Button variant="outline" size="md" onClick={onClose}>
                  {t('approvals.modal.cancel')}
                </Button>
                <ApprovalActionButtons
                  bookingId={Number(booking.id)}
                  onApprove={onApprove}
                  onReject={onReject}
                  processingId={processingId}
                  size="md"
                  showLabels
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </dialog>
  );
}
