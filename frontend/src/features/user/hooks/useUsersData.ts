// External packages
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
// API
import { createUser, deleteUser, getUsers, updateUser } from '../api/users';
// Types
import type { UserDto } from '../types';
// Utils
import { mapUserDtoToUpdateRequest } from '../utils/users';

export function useUsersData() {
  const { t } = useTranslation();
  const [users, setUsers] = useState<UserDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deletingUserId, setDeletingUserId] = useState<number | null>(null);

  useEffect(() => {
    let mounted = true;

    (async () => {
      try {
        setLoading(true);
        const data = await getUsers({ page: 0, size: 200 });
        if (mounted) setUsers(data);
      } catch {
        if (mounted) setError(t('users.errors.loadUsers'));
      } finally {
        if (mounted) setLoading(false);
      }
    })();

    return () => {
      mounted = false;
    };
  }, [t]);

  const update = async (user: UserDto) => {
    const dto = await updateUser(user.id, mapUserDtoToUpdateRequest(user));
    setUsers((prev) => prev.map((u) => (u.id === dto.id ? dto : u)));
    return dto;
  };

  const create = async (input: any) => {
    const dto = await createUser({ ...input, benefit: 'ALL' });
    setUsers((prev) => [dto, ...prev]);
  };

  const remove = async (id: number) => {
    try {
      setDeletingUserId(id);
      await deleteUser(id);
      // Soft-delete: keep row visible, mark as deleted immediately.
      setUsers((prev) =>
        prev.map((u) => (u.id === id ? { ...u, status: 'DELETED' } : u)),
      );
    } finally {
      setDeletingUserId(null);
    }
  };

  return {
    users,
    loading,
    error,
    deletingUserId,
    actions: { update, create, remove },
  };
}

