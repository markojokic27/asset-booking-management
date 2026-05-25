// External packages
import * as React from 'react';

// API
import { createBooking } from '../api/bookingApi';

// Types
import type { Filters } from '../types';
import { useCurrentUser } from '../../user/hooks/useCurrentUser';

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
  const { user } = useCurrentUser();

  const handleCreateBooking = React.useCallback(async () => {
    if (
      !filters.fromDate ||
      !filters.toDate ||
      !filters.fromHour ||
      !filters.toHour ||
      !user?.id
    ) {
      return;
    }

    try {
      setIsCreating(true);

      const bookingStart = new Date(
        `${filters.fromDate}T${filters.fromHour}:00`
      );

      const bookingEnd = new Date(`${filters.toDate}T${filters.toHour}:00`);

      await createBooking({
        userId: user.id,
        assetId,
        status: 'PENDING', // this depends on backend logic
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
