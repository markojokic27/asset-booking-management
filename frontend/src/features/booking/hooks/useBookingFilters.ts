// External packages
import * as React from 'react';

// Types
import type { Filters } from '../types';
import { hourOptions } from '../components/HourSelect';

const defaultFilters: Filters = {
  search: '',
  fromDate: '',
  toDate: '',
  fromHour: '',
  toHour: '',
  selectedWeekdays: [],
};

export function useBookingFilters() {
  const [filters, setFilters] = React.useState<Filters>(defaultFilters);

  const handleCalendarDateClick = React.useCallback((date: string) => {
    setFilters((prev) => ({
      ...prev,
      fromDate: date,
      toDate: date,
      fromHour: hourOptions[0],
      toHour: hourOptions[hourOptions.length - 1],
      selectedWeekdays: [],
    }));
  }, []);

  return {
    filters,
    setFilters,
    handleCalendarDateClick,
  };
}
