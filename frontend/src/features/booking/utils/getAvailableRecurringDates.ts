// Types
import type { BookingWithRelations } from '../types';

export const getAvailableRecurringDates = (
  recurringDates: string[],
  bookings: BookingWithRelations[]
) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);

  return recurringDates.filter((date) => {
    const dayStart = new Date(`${date}T00:00:00`);

    if (dayStart < today) {
      return false;
    }

    const dayEnd = new Date(`${date}T23:59:59`);

    return !bookings.some((booking) => {
      if (booking.status !== 'APPROVED' && booking.status !== 'PENDING') {
        return false;
      }

      const bookingStart = new Date(booking.bookingStart);
      const bookingEnd = new Date(booking.bookingEnd);

      return dayStart < bookingEnd && dayEnd > bookingStart;
    });
  });
};
