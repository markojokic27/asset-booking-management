// Components
import { Toast } from '../../../components/ui/Toast';

// API
import { createRecurringBooking } from '../api/bookingApi';

// Utils
import { useTranslation } from 'react-i18next';

export const reccuringBooking = async (
  userId: number,
  assetId: number,
  notes: string,
  availableRecurringDates: string[],
  setNotes: React.Dispatch<React.SetStateAction<string>>,
  refetch: () => Promise<unknown>,
  t: ReturnType<typeof useTranslation>['t']
): Promise<boolean> => {
  const today = new Date();
  const todayString = today.toISOString().split('T')[0];

  let firstDayStartHour = '06:00';

  if (availableRecurringDates[0] === todayString) {
    const nextHour = today.getHours() + 1;

    if (nextHour > 21) {
      Toast.error(t('layout.toast.invalidBookingPeriod'));
      return false;
    }

    firstDayStartHour = `${nextHour.toString().padStart(2, '0')}:00`;
  }

  const timeSlots = availableRecurringDates.map((date, index) => ({
    bookingStart: new Date(
      `${date}T${index === 0 ? firstDayStartHour : '06:00'}:00`
    ).toISOString(),
    bookingEnd: new Date(`${date}T22:00:00`).toISOString(),
  }));

  await createRecurringBooking({
    userId,
    assetId,
    notes,
    timeSlots,
  });

  setNotes('');
  await refetch();
  Toast.success(t('layout.toast.bookingCreated'));

  return true;
};
