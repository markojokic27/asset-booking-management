import { useEffect, useMemo, useState } from 'react';
import { createUser, getUsers, updateUser } from '../api/users';
import type { UserDto, UserUpsertRequest } from '../types';

function csvEscape(value: unknown) {
  const s = value == null ? '' : String(value);
  const needsQuotes = /[",\r\n]/.test(s);
  const escaped = s.replace(/"/g, '""');
  return needsQuotes ? `"${escaped}"` : escaped;
}

function downloadCsv(filename: string, csv: string) {
  const blob = new Blob([`\uFEFF${csv}`], { type: 'text/csv;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  a.remove();
  URL.revokeObjectURL(url);
}

export function getFullName(user: Pick<UserDto, 'name' | 'surname'>) {
  return `${user.name} ${user.surname}`.trim();
}

export function getDisplayName(user: Pick<UserDto, 'name' | 'surname'>) {
  return `${user.surname} ${user.name}`.trim();
}

type Options = {
  pageSize?: number;
  initialNameSortDir?: 'asc' | 'desc';
};

export function useUsers(options: Options = {}) {
  const pageSize = options.pageSize ?? 10;

  const [search, setSearch] = useState('');
  const [page, setPage] = useState(1);
  const [nameSortDir, setNameSortDir] = useState<'asc' | 'desc'>(
    options.initialNameSortDir ?? 'asc'
  );

  const [users, setUsers] = useState<UserDto[]>([]);
  const [isLoadingUsers, setIsLoadingUsers] = useState(true);
  const [usersError, setUsersError] = useState<string | null>(null);

  const [activeUser, setActiveUser] = useState<UserDto | null>(null);
  const [isUserModalOpen, setIsUserModalOpen] = useState(false);
  const [isUserEditModalOpen, setIsUserEditModalOpen] = useState(false);
  const [isUserCreateModalOpen, setIsUserCreateModalOpen] = useState(false);
  const [isBookingsModalOpen, setIsBookingsModalOpen] = useState(false);

  useEffect(() => {
    let isMounted = true;

    const load = async () => {
      try {
        setIsLoadingUsers(true);
        setUsersError(null);

        const data = await getUsers({ page: 0, size: 200 });

        if (!isMounted) return;
        setUsers(data);
      } catch {
        if (!isMounted) return;
        setUsers([]);
        setUsersError('Failed to load users.');
      } finally {
        if (!isMounted) return;
        setIsLoadingUsers(false);
      }
    };

    load();
    return () => {
      isMounted = false;
    };
  }, []);

  const filteredUsers = useMemo(() => {
    const q = search.trim().toLowerCase();
    const base = !q
      ? users
      : users.filter(
          (u) =>
            u.name.toLowerCase().includes(q) ||
            u.surname.toLowerCase().includes(q) ||
            getFullName(u).toLowerCase().includes(q) ||
            u.email.toLowerCase().includes(q)
        );

    const collator = new Intl.Collator('hr', { sensitivity: 'base' });
    const dir = nameSortDir === 'asc' ? 1 : -1;
    return [...base].sort((a, b) => {
      const lastCmp = collator.compare(a.surname, b.surname);
      if (lastCmp !== 0) return lastCmp * dir;
      const firstCmp = collator.compare(a.name, b.name);
      if (firstCmp !== 0) return firstCmp * dir;
      return collator.compare(a.email, b.email) * dir;
    });
  }, [search, nameSortDir, users]);

  useEffect(() => {
    setPage(1);
  }, [search]);

  const totalPages = Math.max(1, Math.ceil(filteredUsers.length / pageSize));
  const safePage = Math.min(page, totalPages);

  useEffect(() => {
    if (page > totalPages) setPage(totalPages);
  }, [page, totalPages]);

  const pagedUsers = useMemo(() => {
    const start = (safePage - 1) * pageSize;
    return filteredUsers.slice(start, start + pageSize);
  }, [filteredUsers, pageSize, safePage]);

  const paginationItems: Array<number | 'ellipsis'> = useMemo(() => {
    if (totalPages <= 7)
      return Array.from({ length: totalPages }, (_, i) => i + 1);

    const items: Array<number | 'ellipsis'> = [1];
    const left = Math.max(2, safePage - 1);
    const right = Math.min(totalPages - 1, safePage + 1);

    if (left > 2) items.push('ellipsis');
    for (let i = left; i <= right; i++) items.push(i);
    if (right < totalPages - 1) items.push('ellipsis');

    items.push(totalPages);
    return items;
  }, [safePage, totalPages]);

  const toggleNameSortDir = () =>
    setNameSortDir((d) => (d === 'asc' ? 'desc' : 'asc'));

  const openViewUser = (user: UserDto) => {
    setActiveUser(user);
    setIsUserModalOpen(true);
  };

  const closeViewUser = () => {
    setIsUserModalOpen(false);
    setActiveUser(null);
  };

  const openEditUser = (user: UserDto) => {
    setActiveUser(user);
    setIsUserEditModalOpen(true);
  };

  const closeEditUser = () => {
    setIsUserEditModalOpen(false);
    setActiveUser(null);
  };

  const openCreateUser = () => {
    setActiveUser(null);
    setIsUserCreateModalOpen(true);
  };

  const closeCreateUser = () => setIsUserCreateModalOpen(false);

  const openBookings = (user: UserDto) => {
    setActiveUser(user);
    setIsBookingsModalOpen(true);
  };

  const closeBookings = () => {
    setIsBookingsModalOpen(false);
    setActiveUser(null);
  };

  const exportUsersCsv = () => {
    const headers: Array<keyof UserDto> = [
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

    const rows = filteredUsers.map((u) =>
      headers.map((h) => csvEscape(u[h])).join(',')
    );

    const csv = [headers.join(','), ...rows].join('\r\n');
    const date = new Date().toISOString().slice(0, 10);
    downloadCsv(`users-${date}.csv`, csv);
  };

  const saveEditedUser = async (updatedUser: UserDto) => {
    const benefit = updatedUser.benefit ?? 'ALL';
    const dto = await updateUser(updatedUser.id, {
      username: updatedUser.username,
      surname: updatedUser.surname,
      name: updatedUser.name,
      email: updatedUser.email,
      // Backend currently requires non-empty password on update
      // Dummy password for now
      password: '********',
      role: updatedUser.role,
      status: updatedUser.status,
      departmentId: updatedUser.departmentId,
      managerEmail: updatedUser.managerEmail,
      notes: updatedUser.notes ?? '',
      benefit,
    });

    setUsers((currentUsers) =>
      currentUsers.map((u) => (u.id === dto.id ? dto : u))
    );
    setActiveUser(dto);
  };

  type CreateUserInput = Pick<
    UserUpsertRequest,
    | 'username'
    | 'surname'
    | 'name'
    | 'email'
    | 'role'
    | 'status'
    | 'departmentId'
    | 'managerEmail'
    | 'notes'
    | 'password'
  >;

  const createNewUser = async (newUser: CreateUserInput) => {
    const dto = await createUser({
      username: newUser.username,
      surname: newUser.surname,
      name: newUser.name,
      email: newUser.email,
      password: newUser.password,
      role: newUser.role,
      status: newUser.status,
      departmentId: newUser.departmentId,
      managerEmail: newUser.managerEmail,
      notes: newUser.notes ?? '',
      benefit: 'ALL',
    });

    setUsers((currentUsers) => [dto, ...currentUsers]);
  };

  return {
    list: {
      users,
      filteredUsers,
      pagedUsers,
      isLoading: isLoadingUsers,
      error: usersError,
      search,
      setSearch,
    },
    sorting: {
      nameSortDir,
      toggleNameSortDir,
    },
    pagination: {
      page,
      setPage,
      pageSize,
      totalPages,
      safePage,
      items: paginationItems,
    },
    selection: {
      activeUser,
      setActiveUser,
    },
    modals: {
      isUserModalOpen,
      isUserEditModalOpen,
      isUserCreateModalOpen,
      isBookingsModalOpen,
      openViewUser,
      closeViewUser,
      openEditUser,
      closeEditUser,
      openCreateUser,
      closeCreateUser,
      openBookings,
      closeBookings,
    },
    actions: {
      exportUsersCsv,
      saveEditedUser,
      createNewUser,
    },
  };
}
