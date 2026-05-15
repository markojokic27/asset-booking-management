// External packages
import * as React from 'react';

// API
import { createBooking } from '../api/bookingApi';

// Types
import type { Filters } from '../types';

export function useCreateBooking({
  assetId,
  filters,
  refetch,
}: {
  assetId: number;
  filters: Filters;
  refetch: () => Promise<unknown>;
}) {
  const [isCreating, setIsCreating] = React.useState(false);

  const handleCreateBooking = React.useCallback(async () => {
    if (
      !filters.fromDate ||
      !filters.toDate ||
      !filters.fromHour ||
      !filters.toHour
    ) {
      return;
    }

    try {
      setIsCreating(true);

      const bookingStart = new Date(
        `${filters.fromDate}T${filters.fromHour}:00`
      );

      const bookingEnd = new Date(
        `${filters.toDate}T${filters.toHour}:00`
      );

      await createBooking({
        userId: 1,
        assetId,
        status: 'PENDING',
        bookingStart: bookingStart.toISOString(),
        bookingEnd: bookingEnd.toISOString(),
        notes: '',
      });

      await refetch();
    } catch (error) {
      console.error('Failed to create booking', error);
    } finally {
      setIsCreating(false);
    }
  }, [assetId, filters, refetch]);

  return {
    isCreating,
    handleCreateBooking,
  };
}