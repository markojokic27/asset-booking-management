// External packages
import { useEffect, useState } from 'react';

// Types
import type { BookingDto } from '../types';

// API
import { getAllAssetBookings } from '../api/bookingApi';

export const useBookingsByAsset = (assetId: string) => {
  const [bookings, setBookings] = useState<BookingDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  useEffect(() => {
    if (!assetId) return;

    const fetchBookings = async () => {
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
    };

    fetchBookings();
  }, [assetId]);

  return {
    bookings,
    loading,
    error,
  };
};
