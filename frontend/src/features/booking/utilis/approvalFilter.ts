// external imports
import { isAdmin } from '../../user/utilis/users';

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
