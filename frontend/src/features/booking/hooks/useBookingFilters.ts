// External packages
import * as React from 'react';

// Types
import type { Filters } from '../types';

const defaultFilters: Filters = {
  search: '',
  fromDate: '',
  toDate: '',
  fromHour: '',
  toHour: '',
};

export function useBookingFilters() {
  const [filters, setFilters] = React.useState<Filters>(defaultFilters);

  const handleCalendarDateClick = React.useCallback((date: string) => {
    setFilters((prev) => ({
      ...prev,
      fromDate: date,
      toDate: date,
      fromHour: '06:00',
      toHour: '22:00',
    }));
  }, []);

  return {
    filters,
    setFilters,
    handleCalendarDateClick,
  };
}
