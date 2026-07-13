// external imports
import { useCallback, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

// api
import { getAllBookings, getAllUserBookings } from '../api/bookingApi';

// types
import type { BookingWithRelations } from '../types';
import type { UserDto } from '../../user/types';

// utils
import { isAdmin } from '../../user/utils/users';

// hook for the my bookings
export function useMyBookings(user: UserDto | null, enabled: boolean) {
  const { t } = useTranslation();
  const [bookings, setBookings] = useState<BookingWithRelations[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchBookings = useCallback(async () => {
    if (!user) return;

    try {
      setLoading(true);
      setError(null);

      // if the user is an admin, get all bookings
      // if the user is not an admin, get all bookings for the user
      const data = isAdmin(user)
        ? await getAllBookings(0, 100)
        : await getAllUserBookings(0, 100, user.id);

      setBookings(data.content);
    } catch {
      setError(t('myBookings.error'));
    } finally {
      setLoading(false);
    }
  }, [user, t]);

  useEffect(() => {
    if (!enabled || !user) return;

    void fetchBookings();
  }, [enabled, user, fetchBookings]);

  return { bookings, loading, error, refetch: fetchBookings };
}
