// External packages
import { useEffect, useState, useCallback } from 'react';

// Types
import type { BookingWithRelations } from '../types';

// API
import { getAllAssetBookings } from '../api/bookingApi';

export const useBookingsByAsset = (assetId: string) => {
  const [bookings, setBookings] = useState<BookingWithRelations[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  const fetchBookings = useCallback(async () => {
    if (!assetId) {
      return;
    }

    try {
      setLoading(true);
      setError(null);

      const data = await getAllAssetBookings(0, 100, Number(assetId));

      setBookings(data.content);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  }, [assetId]);

  useEffect(() => {
    fetchBookings();
  }, [fetchBookings]);

  return {
    bookings,
    loading,
    error,
    refetch: fetchBookings,
  };
};
