// External packages
import * as React from 'react';

// Utilis
import { hasBookingOverlap } from '../utilis/bookingLogic';

// Types
import type { Filters } from '../types';
import type { BookingWithRelations } from '../types';

export function useBookingAvailability({
  assetStatus,
  filters,
  bookings,
}: {
  assetStatus?: string;
  filters: Filters;
  bookings: BookingWithRelations[];
}) {
  return React.useMemo(() => {
    if (
      assetStatus !== 'ACTIVE' ||
      !filters.fromDate ||
      !filters.toDate ||
      !filters.fromHour ||
      !filters.toHour
    ) {
      return true;
    }

    return hasBookingOverlap({
      bookings,
      fromDate: filters.fromDate,
      toDate: filters.toDate,
      fromHour: filters.fromHour,
      toHour: filters.toHour,
    });
  }, [assetStatus, bookings, filters]);
}
