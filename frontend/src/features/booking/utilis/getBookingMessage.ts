import type { UserDto } from '../../user/types';
import type { Filters } from '../types';

export function getBookingMessage({
  filters,
  availableRecurringDates,
  needApproval,
  user,
  variant,
}: {
  filters: Filters;
  availableRecurringDates: string[];
  needApproval: boolean;
  user: UserDto | null;
  variant: string;
}) {
  let message = '';

  const formatDate = (date: string) =>
    new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });

  const weekDayNames: Record<number, string> = {
    1: 'Monday',
    2: 'Tuesday',
    3: 'Wednesday',
    4: 'Thursday',
    5: 'Friday',
    6: 'Saturday',
    7: 'Sunday',
  };

  // Recurring booking
  if (filters.selectedWeekdays.length > 0) {
    const firstRecurringDate = new Date(availableRecurringDates[0]);

    const monthYear = firstRecurringDate.toLocaleDateString('en-US', {
      month: 'long',
      year: 'numeric',
    });

    const days = filters.selectedWeekdays
      .map((day) => weekDayNames[day])
      .join(' and ');

    message = `Do you want to book this asset every ${days} in ${monthYear}?`;
  }
  // Single day booking
  else if (filters.fromDate === filters.toDate) {
    const date = formatDate(filters.fromDate);

    if (variant === 'HOUR') {
      message = `Do you want to book this asset on ${date}, from ${filters.fromHour} to ${filters.toHour}?`;
    } else {
      message = `Do you want to book this asset for ${date}?`;
    }
  }
  // Multi-day booking
  else {
    message = `Do you want to book this asset from ${formatDate(
      filters.fromDate
    )} to ${formatDate(filters.toDate)}?`;
  }

  if (needApproval && user?.role === 'EMPLOYEE') {
    message +=
      ' This booking requires manager approval. An email notification will be sent to your manager for review.';
  }

  return message;
}
