// External packages
import * as React from 'react';
import type { TFunction } from 'i18next';

// Components
import { Toast } from '../../../components/ui/Toast';

// API
import { createBooking } from '../api/bookingApi';
import { useAuth } from '../../auth/context/AuthContext';

// Types
import type { Filters } from '../types';

// Utilis
import { reccuringBooking } from '../utils/reccuringBooking';
import { prepareDayBooking } from '../utils/prepareDayBooking';

export function useCreateBooking({
  assetId,
  filters,
  notes,
  setNotes,
  refetch,
  bookingPeriod,
  availableRecurringDates,
  t,
}: {
  assetId: number;
  filters: Filters;
  notes: string;
  setNotes: React.Dispatch<React.SetStateAction<string>>;
  refetch: () => Promise<unknown>;
  bookingPeriod: 'HOUR' | 'DAY';
  availableRecurringDates: string[];
  t: TFunction;
}) {
  const [isCreating, setIsCreating] = React.useState(false);
  const { user } = useAuth();

  const handleCreateBooking = React.useCallback(async (): Promise<boolean> => {
    const userId = user?.id;

    if (!userId) {
      console.warn('Missing required user id for booking creation');
      Toast.error(t('layout.toast.missingUser'));
      return false;
    }

    try {
      setIsCreating(true);

      if (availableRecurringDates.length > 0) {
        reccuringBooking(
          userId,
          assetId,
          notes,
          availableRecurringDates,
          setNotes,
          refetch,
          t
        );
        return true;
      }

      if (bookingPeriod === 'DAY' && !prepareDayBooking(filters, t)) {
        return false;
      }

      if (
        !filters.fromDate ||
        !filters.toDate ||
        !filters.fromHour ||
        !filters.toHour
      ) {
        console.warn('Missing required fields for booking creation');
        Toast.error(t('layout.toast.invalidBookingPeriod'));
        return false;
      }

      const bookingStart = new Date(
        `${filters.fromDate}T${filters.fromHour}:00`
      );

      const bookingEnd = new Date(`${filters.toDate}T${filters.toHour}:00`);

      await createBooking({
        userId,
        assetId,
        status: 'PENDING',
        bookingStart: bookingStart.toISOString(),
        bookingEnd: bookingEnd.toISOString(),
        notes,
      });

      setNotes('');
      await refetch();

      Toast.success(t('layout.toast.bookingCreated'));
      return true;
    } catch (error) {
      console.error('Failed to create booking', error);
      Toast.error(t('layout.toast.bookingCreateFailed'));
      return false;
    } finally {
      setIsCreating(false);
    }
  }, [
    assetId,
    availableRecurringDates,
    bookingPeriod,
    filters,
    notes,
    refetch,
    setNotes,
    user?.id,
    t,
  ]);

  return {
    isCreating,
    handleCreateBooking,
  };
}
