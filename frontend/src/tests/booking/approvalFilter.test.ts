import { describe, expect, it } from 'vitest';

import {
  filterBookingsByAsset,
  filterBookingsByDateRange,
  filterBookingsByStatus,
  filterPendingBookingsBySearch,
  filterPendingBookingsForApprover,
} from '../../features/booking/utils/approvalFilter';
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

describe('filterBookingsByDateRange', () => {
  const bookingWithDates = (
    start: string,
    end: string
  ): BookingWithRelations =>
    ({
      ...baseBooking('mark.jones@example.com'),
      bookingStart: new Date(start),
      bookingEnd: new Date(end),
    }) as BookingWithRelations;

  it('returns all bookings when both dates are empty', () => {
    const bookings = [
      bookingWithDates('2026-03-01T09:00:00', '2026-03-01T17:00:00'),
      bookingWithDates('2026-04-10T09:00:00', '2026-04-10T17:00:00'),
    ];

    expect(filterBookingsByDateRange(bookings, '', '')).toHaveLength(2);
    expect(filterBookingsByDateRange(bookings, '   ', '   ')).toHaveLength(2);
  });

  it('filters bookings that end before the from date', () => {
    const bookings = [
      bookingWithDates('2026-03-01T09:00:00', '2026-03-01T17:00:00'),
      bookingWithDates('2026-04-10T09:00:00', '2026-04-10T17:00:00'),
    ];

    const result = filterBookingsByDateRange(bookings, '2026-04-01', '');

    expect(result).toHaveLength(1);
    expect(new Date(result[0].bookingStart).getMonth()).toBe(3);
  });

  it('filters bookings that start after the to date', () => {
    const bookings = [
      bookingWithDates('2026-03-01T09:00:00', '2026-03-01T17:00:00'),
      bookingWithDates('2026-04-10T09:00:00', '2026-04-10T17:00:00'),
    ];

    const result = filterBookingsByDateRange(bookings, '', '2026-03-31');

    expect(result).toHaveLength(1);
    expect(new Date(result[0].bookingStart).getMonth()).toBe(2);
  });

  it('filters bookings outside the selected date range', () => {
    const bookings = [
      bookingWithDates('2026-03-01T09:00:00', '2026-03-01T17:00:00'),
      bookingWithDates('2026-03-15T09:00:00', '2026-03-15T17:00:00'),
      bookingWithDates('2026-04-10T09:00:00', '2026-04-10T17:00:00'),
    ];

    const result = filterBookingsByDateRange(
      bookings,
      '2026-03-10',
      '2026-03-20'
    );

    expect(result).toHaveLength(1);
    expect(new Date(result[0].bookingStart).getDate()).toBe(15);
  });

  it('includes bookings that overlap the selected range', () => {
    const bookings = [
      bookingWithDates('2026-03-09T09:00:00', '2026-03-12T17:00:00'),
    ];

    const result = filterBookingsByDateRange(
      bookings,
      '2026-03-10',
      '2026-03-11'
    );

    expect(result).toHaveLength(1);
  });
});

describe('filterBookingsByStatus', () => {
  it('returns all bookings when status is empty', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      { ...baseBooking('mark.jones@example.com'), status: 'APPROVED' },
    ] as BookingWithRelations[];

    expect(filterBookingsByStatus(bookings, '')).toHaveLength(2);
  });

  it('filters bookings by status', () => {
    const bookings = [
      baseBooking('mark.jones@example.com'),
      { ...baseBooking('mark.jones@example.com'), status: 'APPROVED' },
    ] as BookingWithRelations[];

    const result = filterBookingsByStatus(bookings, 'PENDING');

    expect(result).toHaveLength(1);
    expect(result[0].status).toBe('PENDING');
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
