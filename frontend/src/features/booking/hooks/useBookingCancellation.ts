// external imports
import { useCallback, useState } from 'react';
import { useTranslation } from 'react-i18next';

// api
import { cancelBooking } from '../api/bookingApi';

export function useBookingCancellation(onSuccess: () => void | Promise<void>) {
  const { t } = useTranslation();
  const [isCancelling, setIsCancelling] = useState(false);
  const [cancelError, setCancelError] = useState<string | null>(null);

  // function to cancel a booking
  const cancel = useCallback(
    async (bookingId: number) => {
      try {
        setIsCancelling(true);
        setCancelError(null);
        // cancel the booking
        await cancelBooking(bookingId);
        await onSuccess();
        return true;
      } catch {
        setCancelError(t('myBookings.cancelError'));
        return false;
      } finally {
        setIsCancelling(false);
      }
    },
    [onSuccess, t]
  );

  // function to clear the cancel error
  const clearCancelError = useCallback(() => setCancelError(null), []);

  return { cancel, isCancelling, cancelError, clearCancelError };
}
