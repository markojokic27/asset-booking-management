// external imports
import { useCallback, useState } from 'react';
import { useTranslation } from 'react-i18next';

// api
import { approveBooking, rejectBooking } from '../api/bookingApi';

export function useBookingApproval(onSuccess: () => void | Promise<void>) {
  const { t } = useTranslation();
  const [processingId, setProcessingId] = useState<number | null>(null);
  const [actionError, setActionError] = useState('');

  // approve booking hook
  const approve = useCallback(
    async (bookingId: number) => {
      try {
        setProcessingId(bookingId);
        setActionError('');
        await approveBooking(bookingId);
        await onSuccess();
      } catch {
        setActionError(t('approvals.actionError'));
      } finally {
        setProcessingId(null);
      }
    },
    [onSuccess, t]
  );

  // reject booking hook
  const reject = useCallback(
    async (bookingId: number) => {
      try {
        setProcessingId(bookingId);
        setActionError('');
        await rejectBooking(bookingId);
        await onSuccess();
      } catch {
        setActionError(t('approvals.actionError'));
      } finally {
        setProcessingId(null);
      }
    },
    [onSuccess, t]
  );

  const clearActionError = useCallback(() => setActionError(''), []);

  return {
    approve,
    reject,
    processingId,
    actionError,
    clearActionError,
  };
}
