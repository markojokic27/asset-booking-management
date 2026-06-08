import { describe, expect, it } from 'vitest';

import {
  filterBookingsByAsset,
  filterPendingBookingsBySearch,
  filterPendingBookingsForApprover,
} from '../../features/booking/utilis/approvalFilter';
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

describe('filterBookingsByAsset', () => {
  it('returns all bookings when asset id is null', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      {
        ...baseBooking('mark.jones@example.com'),
        asset: {
          ...baseBooking('mark.jones@example.com').asset,
          id: 2,
          name: 'Projector',
        },
      },
    ] as BookingWithRelations[];

    expect(filterBookingsByAsset(bookings, null)).toHaveLength(2);
  });

  it('filters bookings by asset id', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      {
        ...baseBooking('mark.jones@example.com'),
        asset: {
          ...baseBooking('mark.jones@example.com').asset,
          id: 2,
          name: 'Projector',
        },
      },
    ] as BookingWithRelations[];

    const result = filterBookingsByAsset(bookings, 2);

    expect(result).toHaveLength(1);
    expect(result[0].asset.name).toBe('Projector');
  });
});

describe('filterPendingBookingsBySearch', () => {
  it('returns all bookings when search is empty', () => {
    const bookings = [baseBooking('mark.jones@example.com')];

    expect(filterPendingBookingsBySearch(bookings, '')).toHaveLength(1);
    expect(filterPendingBookingsBySearch(bookings, '   ')).toHaveLength(1);
  });

  it('filters by booking id', () => {
    const bookings = [
      { ...baseBooking('mark.jones@example.com'), id: '12' },
      { ...baseBooking('mark.jones@example.com'), id: '99' },
    ] as BookingWithRelations[];

    const result = filterPendingBookingsBySearch(bookings, '12');

    expect(result).toHaveLength(1);
    expect(result[0].id).toBe('12');
  });

  it('filters by user name and email', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      {
        ...baseBooking('mark.jones@example.com'),
        user: {
          ...baseBooking('mark.jones@example.com').user,
          name: 'Marko',
          surname: 'Babic',
          email: 'marko@example.com',
        },
      },
    ] as BookingWithRelations[];

    expect(filterPendingBookingsBySearch(bookings, 'jane')).toHaveLength(1);
    expect(filterPendingBookingsBySearch(bookings, 'marko@')).toHaveLength(1);
    expect(filterPendingBookingsBySearch(bookings, 'babic')).toHaveLength(1);
  });

  it('filters by asset name', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      {
        ...baseBooking('mark.jones@example.com'),
        asset: {
          ...baseBooking('mark.jones@example.com').asset,
          name: 'Projector',
        },
      },
    ] as BookingWithRelations[];

    const result = filterPendingBookingsBySearch(bookings, 'projector');

    expect(result).toHaveLength(1);
    expect(result[0].asset.name).toBe('Projector');
  });
});
