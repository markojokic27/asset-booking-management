// hooks/useBookingsByAsset.ts
import { useEffect, useState } from 'react';
import api from '../../../shared/api';

export const useBookingsByAsset = (assetId: string) => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<any>(null);

  useEffect(() => {
    if (!assetId) return;

    setLoading(true);

    api
      .get(`/bookings`, {
        params: { assetId, size: 100 },
      })
      .then((res) => setBookings(res.data.content))
      .catch(setError)
      .finally(() => setLoading(false));
  }, [assetId]);

  return { bookings, loading, error };
};
