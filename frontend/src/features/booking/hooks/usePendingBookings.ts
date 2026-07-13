// external packages
import { useCallback, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

// api
import { getPendingBookings } from '../api/bookingApi';

// utils
import { filterPendingBookingsForApprover } from '../utils/approvalFilter';

// types
import type { BookingWithRelations } from '../types';
import type { UserDto } from '../../user/types';

export function usePendingBookings(
  approver: Pick<UserDto, 'email' | 'role'> | null,
  enabled = true
) {
  const { t } = useTranslation();
  const [bookings, setBookings] = useState<BookingWithRelations[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const refetch = useCallback(async () => {
    if (!enabled || !approver) return;

    try {
      setLoading(true);
      setError('');
      const data = await getPendingBookings();
      setBookings(filterPendingBookingsForApprover(data.content, approver));
    } catch {
      setError(t('approvals.error'));
    } finally {
      setLoading(false);
    }
  }, [enabled, approver, t]);

  useEffect(() => {
    void refetch();
  }, [refetch]);

  return { bookings, loading, error, refetch };
}
