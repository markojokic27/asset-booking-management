// Components
import { Toast } from '../../../components/ui/Toast';

// Types
import type { Filters } from '../types';
import type { TFunction } from 'i18next';

export const prepareDayBooking = (filters: Filters, t: TFunction): boolean => {
  const today = new Date();
  const todayString = today.toISOString().split('T')[0];

  if (filters.fromDate === todayString) {
    const nextHour = today.getHours() + 1;

    if (nextHour > 21) {
      Toast.error(t('layout.toast.invalidBookingPeriod'));
      return false;
    }

    filters.fromHour = `${nextHour.toString().padStart(2, '0')}:00`;
  } else {
    filters.fromHour = '06:00';
  }

  filters.toHour = '22:00';

  return true;
};
