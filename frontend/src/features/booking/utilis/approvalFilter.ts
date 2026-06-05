// external imports
import { getFullName, isAdmin } from '../../user/utilis/users';

// types
import type { UserDto } from '../../user/types';
import type { BookingWithRelations } from '../types';

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
