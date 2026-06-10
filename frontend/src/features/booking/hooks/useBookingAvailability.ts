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
  bookingPeriod,
  visibleMonth,
}: {
  assetStatus?: string;
  filters: Filters;
  bookings: BookingWithRelations[];
  bookingPeriod: 'HOUR' | 'DAY';
  visibleMonth: Date;
}) {
  return React.useMemo(() => {
    if (assetStatus !== 'ACTIVE') {
      return true;
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
      selectedWeekdays: filters.selectedWeekdays,
      bookingPeriod,
      visibleMonth,
    });
  }, [assetStatus, bookings, filters, visibleMonth]);
}
