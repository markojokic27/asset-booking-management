// external imports
import { getFullName, isAdmin } from '../../user/utils/users';

// types
import type { UserDto } from '../../user/types';
import type { BookingStatus, BookingWithRelations } from '../types';

export function filterPendingBookingsForApprover(
  bookings: BookingWithRelations[],
  approver: Pick<UserDto, 'email' | 'role'> | null
): BookingWithRelations[] {
  if (!approver) {
    return [];
  }

  // admin can see all bookings
  if (isAdmin(approver)) {
    return bookings;
  }

  const approverEmail = approver.email.trim().toLowerCase();

  // manager can see bookings for their department
  return bookings.filter(
    (booking) =>
      booking.user.managerEmail?.trim().toLowerCase() === approverEmail
  );
}

export function filterBookingsByStatus(
  bookings: BookingWithRelations[],
  status: BookingStatus | ''
): BookingWithRelations[] {
  if (!status) {
    return bookings;
  }

  return bookings.filter((booking) => booking.status === status);
}

export function filterBookingsByAsset(
  bookings: BookingWithRelations[],
  assetId: number | null
): BookingWithRelations[] {
  if (assetId == null) {
    return bookings;
  }

  return bookings.filter((booking) => booking.asset.id === assetId);
}

// filter bookings by date range
export function filterBookingsByDateRange(
  bookings: BookingWithRelations[],
  fromDate: string,
  toDate: string
): BookingWithRelations[] {
  // remove whitespace from the dates
  const from = fromDate.trim();
  const to = toDate.trim();

  // if no dates are provided, return all bookings
  if (!from && !to) {
    return bookings;
  }

  // convert the dates to Date objects
  const filterStart = from ? new Date(`${from}T00:00:00`) : null;
  const filterEnd = to ? new Date(`${to}T23:59:59.999`) : null;

  return bookings.filter((booking) => {
    // convert the booking start and end dates to Date objects
    const bookingStart = new Date(booking.bookingStart);
    const bookingEnd = new Date(booking.bookingEnd);

    // if the booking end date is before the filter start date, return false
    if (filterStart && bookingEnd < filterStart) {
      return false;
    }

    // if the booking start date is after the filter end date, return false
    if (filterEnd && bookingStart > filterEnd) {
      return false;
    }

    return true;
  });
}

// filter pending bookings by search
export function filterPendingBookingsBySearch(
  bookings: BookingWithRelations[],
  search: string
): BookingWithRelations[] {
  // get the search query
  const q = search.trim().toLowerCase();
  if (!q) {
    return bookings;
  }

  return bookings.filter(
    (booking) =>
      // filter by booking id, user name, user email, and asset name
      String(booking.id).includes(q) ||
      getFullName(booking.user).toLowerCase().includes(q) ||
      booking.user.email.toLowerCase().includes(q) ||
      booking.asset.name.toLowerCase().includes(q)
  );
}
