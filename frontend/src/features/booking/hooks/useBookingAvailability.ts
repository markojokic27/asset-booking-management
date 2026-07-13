// External packages
import * as React from 'react';

// Utilis
import { hasBookingOverlap } from '../utils/bookingLogic';

// Types
import type { BookingWithRelations, Filters } from '../types';

export function useBookingAvailability({
  assetStatus,
  filters,
  bookings,
  bookingPeriod,
  reccuringDates,
  availableRecurringDates,
}: {
  assetStatus?: string;
  filters: Filters;
  bookings: BookingWithRelations[];
  bookingPeriod: 'HOUR' | 'DAY';
  reccuringDates: number[];
  availableRecurringDates: string[];
}) {
  return React.useMemo(() => {
    if (assetStatus !== 'ACTIVE') {
      return true;
    }

    if (reccuringDates.length > 0) {
      if (availableRecurringDates.length === 0) {
        return true;
      }
      return false;
    }

    if (!filters.fromDate || !filters.toDate) {
      return true;
    }

    if (bookingPeriod === 'HOUR' && (!filters.fromHour || !filters.toHour)) {
      return true;
    }

    return hasBookingOverlap({
      bookings,
      fromDate: filters.fromDate,
      toDate: filters.toDate,
      fromHour: filters.fromHour,
      toHour: filters.toHour,
      bookingPeriod,
    });
  }, [assetStatus, bookings, filters, reccuringDates, availableRecurringDates]);
}
