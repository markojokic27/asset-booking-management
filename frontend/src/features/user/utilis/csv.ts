function csvEscape(value: unknown) {
  const s = value == null ? '' : String(value);
  const needsQuotes = /[",\r\n]/.test(s);
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
