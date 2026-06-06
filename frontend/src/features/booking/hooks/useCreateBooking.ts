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
  notes,
  setNotes,
  refetch,
  bookingPeriod,
}: {
  assetId: number;
  filters: Filters;
  notes: string;
  setNotes: React.Dispatch<React.SetStateAction<string>>;
  refetch: () => Promise<unknown>;
  bookingPeriod: 'HOUR' | 'DAY';
}) {
  const [isCreating, setIsCreating] = React.useState(false);
  const { user } = useCurrentUser();

  const handleCreateBooking = React.useCallback(async () => {
    if (bookingPeriod === 'DAY') {
      filters.fromHour = '06:00';
      filters.toHour = '22:00';
    }
    if (
      !filters.fromDate ||
      !filters.toDate ||
      !filters.fromHour ||
      !filters.toHour ||
      !user?.id
    ) {
      console.warn('Missing required fields for booking creation');
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
        status: 'PENDING', //TODO this depends on backend logic - za svaku kategoriju vidi jel triba req, i vidi privilegije
        bookingStart: bookingStart.toISOString(),
        bookingEnd: bookingEnd.toISOString(),
        notes,
      });
      setNotes('');
      await refetch();
    } catch (error) {
      console.error('Failed to create booking', error);
    } finally {
      setIsCreating(false);
    }
  }, [assetId, filters, notes, refetch]);

  return {
    isCreating,
    handleCreateBooking,
  };
}
