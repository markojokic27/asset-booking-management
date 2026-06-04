import { describe, expect, it } from 'vitest';

import { filterPendingBookingsForApprover } from '../../features/booking/utilis/approvalFilter';
import type { BookingWithRelations } from '../../features/booking/types';

const baseBooking = (managerEmail: string): BookingWithRelations =>
  ({
    id: '1',
    userId: 2,
    assetId: 1,
    bookingStart: new Date(),
    bookingEnd: new Date(),
    status: 'PENDING',
    createdAt: new Date(),
    lastModifiedAt: new Date(),
    user: {
      id: 2,
      name: 'Jane',
      surname: 'Smith',
      email: 'jane@example.com',
      role: 'EMPLOYEE',
      managerEmail,
    },
    asset: {
      id: 1,
      name: 'Laptop',
      category: { id: 1, name: 'IT', bookingPeriod: 'DAY', approval: true },
      status: 'ACTIVE',
      description: '',
      location: 'Office',
    },
  }) as BookingWithRelations;

describe('filterPendingBookingsForApprover', () => {
  it('returns bookings where manager email matches approver', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      baseBooking('other@example.com'),
    ];

    const result = filterPendingBookingsForApprover(bookings, {
      email: 'mark.jones@example.com',
      role: 'MANAGER',
    });

    expect(result).toHaveLength(1);
    expect(result[0].user.email).toBe('jane@example.com');
  });

  it('returns all bookings for admin', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      baseBooking('other@example.com'),
    ];

    const result = filterPendingBookingsForApprover(bookings, {
      email: 'admin@example.com',
      role: 'ADMIN',
    });

    expect(result).toHaveLength(2);
  });
});
