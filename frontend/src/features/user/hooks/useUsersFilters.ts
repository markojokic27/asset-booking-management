// External packages
import { useMemo, useState } from 'react';

// Types
import type { UserDto } from '../types';

export function useUserFilters(users: UserDto[]) {
  const [search, setSearch] = useState('');
  const [sortDir, setSortDir] = useState<'asc' | 'desc'>('asc');
  const [showDeleted, setShowDeleted] = useState(false);

  const collator = useMemo(
    () => new Intl.Collator('hr', { sensitivity: 'base' }),
    []
  );

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();

    return users.filter(
      (u) =>
        (showDeleted || u.status !== 'DELETED') &&
        (!q ||
          u.name.toLowerCase().includes(q) ||
          u.surname.toLowerCase().includes(q) ||
          u.email.toLowerCase().includes(q))
    );
  }, [users, search, showDeleted]);

  const sorted = useMemo(() => {
    const dir = sortDir === 'asc' ? 1 : -1;

    return [...filtered].sort((a, b) => {
      const last = collator.compare(a.surname, b.surname);
      if (last !== 0) return last * dir;

      const first = collator.compare(a.name, b.name);
      if (first !== 0) return first * dir;

      return collator.compare(a.email, b.email) * dir;
    });
  }, [filtered, sortDir, collator]);

  return {
    data: sorted,
    search,
    setSearch,
    showDeleted,
    toggleShowDeleted: () => setShowDeleted((v) => !v),
    sortDir,
    toggleSort: () => setSortDir((d) => (d === 'asc' ? 'desc' : 'asc')),
  };
}
