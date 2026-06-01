// Types
import type { BookingWithRelations } from '../../booking/types';

const STATUS_COLORS: Record<string, string> = {
  APPROVED: '#22c55e',
  PENDING: '#f59e0b',
  REJECTED: '#ef4444',
  CANCELLED: '#6b7280',
};

export const mapBookingsToCalendarEvents = (
  bookings: BookingWithRelations[]
) => {
  return bookings.map((booking) => ({
    id: booking.id.toString(),
    title: booking.user.surname,
    start: new Date(booking.bookingStart).toISOString(),
    end: new Date(booking.bookingEnd).toISOString() ,
    backgroundColor: STATUS_COLORS[booking.status] || '#3b82f6',
    borderColor: STATUS_COLORS[booking.status] || '#3b82f6',
    extendedProps: {
      booking,
    },
  }));
};

export const hasBookingOverlap = ({
  bookings,
  fromDate,
  toDate,
  fromHour,
  toHour,
}: {
  bookings: BookingWithRelations[];
  fromDate: string;
  toDate: string;
  fromHour: string;
  toHour: string;
}) => {
  const selectedStart = new Date(`${fromDate}T${fromHour}:00`);

  const selectedEnd = new Date(`${toDate}T${toHour}:00`);

  return bookings.some((booking) => {
    if (booking.status !== 'APPROVED' && booking.status !== 'PENDING') {
      return false;
    }

    const bookingStart = new Date(booking.bookingStart);
    const bookingEnd = new Date(booking.bookingEnd);

    return selectedStart < bookingEnd && selectedEnd > bookingStart;
  });
};

// format the booking time
export const formatBookingTime = (start: string | Date, end: string | Date) => {
  const startDate = new Date(start);
  const endDate = new Date(end);

  return `${startDate.toLocaleString()} – ${endDate.toLocaleString()}`;
};

// function to check if a booking is past its end date
export const isBookingPastEnd = (booking: Pick<BookingWithRelations, 'bookingEnd'>) =>
  new Date(booking.bookingEnd).getTime() < Date.now();

// function to sort bookings by start date newest first
export const sortBookingsNewestFirst = (
  bookings: BookingWithRelations[]
): BookingWithRelations[] =>
  [...bookings].sort(
    (a, b) =>
      new Date(b.bookingStart).getTime() - new Date(a.bookingStart).getTime()
  );
