// external packages
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

// api
import { getPendingBookings } from '../api/bookingApi';

// types
import type { BookingWithRelations } from '../types';

export function usePendingBookings(enabled = true) {
  const { t } = useTranslation();
  const [bookings, setBookings] = useState<BookingWithRelations[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    // check if the component is mounted
    if (!enabled) return;

    const fetchBookings = async () => {
      try {
        // set the loading state to true
        setLoading(true);
        setError('');

        // wait for the bookings to be fetched
        const data = await getPendingBookings();
        setBookings(data.content);
      } catch {
        // set the error state to the error message
        setError(t('approvals.error'));
      } finally {
        // set the loading state to false
        setLoading(false);
      }
    };

    // fetch the bookings
    void fetchBookings();
  }, [enabled, t]);

  return { bookings, loading, error };
}
