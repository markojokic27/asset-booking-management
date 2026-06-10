import type { TFunction } from 'i18next';

import type { BookingWithRelations } from '../../booking/types';

function csvEscape(value: unknown) {
  const s = value == null ? '' : String(value);
  const needsQuotes = /[",\r\n]/.test(s);
  // eslint-disable-next-line unicorn/prefer-string-replace-all
  const escaped = s.replace(/"/g, '""');
  return needsQuotes ? `"${escaped}"` : escaped;
}

export function exportUsersCsv(users: any[]) {
  const headers = [
    'id',
    'name',
    'surname',
    'email',
    'username',
    'role',
    'status',
    'departmentId',
    'managerEmail',
    'notes',
  ];

  const rows = users.map((u) => headers.map((h) => csvEscape(u[h])).join(','));

  const csv = [headers.join(','), ...rows].join('\r\n');

  const blob = new Blob([`\uFEFF${csv}`], {
    type: 'text/csv;charset=utf-8',
  });

  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `users-${new Date().toISOString().slice(0, 10)}.csv`;
  a.click();
  URL.revokeObjectURL(url);
}

// format the booking date range
function formatBookingDateRange(booking: BookingWithRelations) {
  return `${new Date(booking.bookingStart).toLocaleDateString()} - ${new Date(
    booking.bookingEnd
  ).toLocaleDateString()}`;
}

// get the booking status label
function bookingStatusLabel(status: BookingWithRelations['status'], t: TFunction) {
  return t(`bookings.status.${status.toLowerCase()}`, { defaultValue: status });
}

// export the user bookings as a CSV file
export function exportUserBookingsCsv(
  bookings: BookingWithRelations[],
  user: { id: number; fullName: string },
  t: TFunction
) {
  // get the headers for the CSV file
  const headers = [
    t('users.modals.bookings.table.columns.bookingId'),
    t('users.modals.bookings.table.columns.asset'),
    t('users.modals.bookings.table.columns.date'),
    t('users.modals.bookings.table.columns.status'),
  ];

  // get the rows for the CSV file
  const rows = bookings.map((booking) =>
    [
      csvEscape(booking.id),
      csvEscape(booking.asset.name),
      csvEscape(formatBookingDateRange(booking)),
      csvEscape(bookingStatusLabel(booking.status, t)),
    ].join(',')
  );

  // create the CSV file
  const csv = [headers.map(csvEscape).join(','), ...rows].join('\r\n');

  // create the blob for the CSV file
  const blob = new Blob([`\uFEFF${csv}`], {
    type: 'text/csv;charset=utf-8',
  });

  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `user-${user.id}-bookings-${new Date().toISOString().slice(0, 10)}.csv`;
  a.click();
  URL.revokeObjectURL(url);
}
