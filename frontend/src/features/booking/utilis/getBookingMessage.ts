// External packages
import type { TFunction } from 'i18next';

// Types
import type { UserDto } from '../../user/types';
import type { Filters } from '../types';

export function getBookingMessage({
  filters,
  availableRecurringDates,
  needApproval,
  user,
  variant,
  t,
  language,
}: {
  filters: Filters;
  availableRecurringDates: string[];
  needApproval: boolean;
  user: UserDto | null;
  variant: string;
  t: TFunction;
  language: string;
}) {
  let message = '';

  const formatDate = (date: string) =>
    new Date(date).toLocaleDateString(language, {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });

  const weekDayNames: Record<number, string> = {
    1: t('bookings.recurringDays.monday'),
    2: t('bookings.recurringDays.tuesday'),
    3: t('bookings.recurringDays.wednesday'),
    4: t('bookings.recurringDays.thursday'),
    5: t('bookings.recurringDays.friday'),
    6: t('bookings.recurringDays.saturday'),
    7: t('bookings.recurringDays.sunday'),
  };

  // Recurring booking
  if (filters.selectedWeekdays.length > 0) {
    const firstRecurringDate = new Date(availableRecurringDates[0]);

    const monthYear = firstRecurringDate.toLocaleDateString(language, {
      month: 'long',
      year: 'numeric',
    });

    const weekdayNames = filters.selectedWeekdays.map(
      (day) => weekDayNames[day]
    );

    let days = '';
    const andWord = t('bookings.confirmation.and');

    if (weekdayNames.length === 1) {
      days = weekdayNames[0];
    } else if (weekdayNames.length === 2) {
      days = weekdayNames.join(` ${andWord} `);
    } else {
      days = `${weekdayNames.slice(0, -1).join(', ')} ${andWord} ${
        weekdayNames[weekdayNames.length - 1]
      }`;
    }

    message = t('bookings.confirmation.recurring', {
      days,
      month: monthYear,
    });
  }

  // Single day booking
  else if (filters.fromDate === filters.toDate) {
    const date = formatDate(filters.fromDate);

    if (variant === 'HOUR') {
      message = t('bookings.confirmation.singleHour', {
        date,
        from: filters.fromHour,
        to: filters.toHour,
      });
    } else {
      message = t('bookings.confirmation.singleDay', {
        date,
      });
    }
  }
  // Multi-day booking
  else {
    message = t('bookings.confirmation.multiDay', {
      from: formatDate(filters.fromDate),
      to: formatDate(filters.toDate),
    });
  }
  if (needApproval && user?.role === 'EMPLOYEE') {
    message += ` ${t('bookings.confirmation.needApproval')}`;
  }

  return message;
}
