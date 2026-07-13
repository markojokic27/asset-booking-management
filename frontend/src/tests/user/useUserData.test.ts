import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { renderHook, act, waitFor } from '@testing-library/react';
import { getUsers, createUser, updateUser, deleteUser } from '../../features/user/api/users';
import { useUsersData } from '../../features/user/hooks/useUsersData';

vi.mock('react-i18next', () => {
  const t = (key: string) => key;
  return {
    useTranslation: () => ({ t }),
  };
});

vi.mock('../../features/user/api/users', () => ({
  getUsers: vi.fn(),
  createUser: vi.fn(),
  updateUser: vi.fn(),
  deleteUser: vi.fn(),
}));

vi.mock('../../features/user/utils/users', () => ({
  mapUserDtoToUpdateRequest: vi.fn((user) => user),
}));

const mockUser  = { id: 1, name: 'John', status: 'ACTIVE' };
const mockUser2 = { id: 2, name: 'Jane', status: 'ACTIVE' };

// Runs the hook and waits for the initial fetch to complete
const setup = async () => {
  const { result } = renderHook(() => useUsersData());
  await waitFor(() => expect(result.current.loading).toBe(false));
  return result;
};

// Returns resolve/reject controls for a pending promise — used in unmount tests
const setupPending = <T>(mock: ReturnType<typeof vi.mocked<any>>) => {
  let resolve!: (v: T) => void;
  let reject!: (e: unknown) => void;
  mock.mockReturnValue(new Promise<T>((res, rej) => { resolve = res; reject = rej; }));
  return { resolve, reject };
};

describe('useUsersData', () => {

  beforeEach(() => {
    vi.mocked(getUsers).mockResolvedValue([mockUser, mockUser2] as any);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('starts with loading true and empty users', () => {
    const { result } = renderHook(() => useUsersData());
    expect(result.current.loading).toBe(true);
    expect(result.current.users).toEqual([]);
  });

  it('loads users on mount', async () => {
    const result = await setup();
    expect(result.current.users).toEqual([mockUser, mockUser2]);
    expect(result.current.error).toBeNull();
    expect(getUsers).toHaveBeenCalledWith({ page: 0, size: 200 });
  });

  it('sets error when getUsers fails', async () => {
    vi.mocked(getUsers).mockRejectedValue(new Error('Network error'));
    const { result } = renderHook(() => useUsersData());
    await waitFor(() => expect(result.current.loading).toBe(false));
    expect(result.current.error).toBe('users.errors.loadUsers');
    expect(result.current.users).toEqual([]);
  });

  // Ensures state is not updated after the component unmounts
  it('does not update state after unmount (success)', async () => {
    const { resolve } = setupPending<any[]>(vi.mocked(getUsers));
    const { result, unmount } = renderHook(() => useUsersData());
    unmount();
    await act(async () => resolve([mockUser]));
    expect(result.current.users).toEqual([]);
    expect(result.current.loading).toBe(true);
  });

  it('does not set error after unmount (failure)', async () => {
    const { reject } = setupPending<any[]>(vi.mocked(getUsers));
    const { result, unmount } = renderHook(() => useUsersData());
    unmount();
    await act(async () => reject(new Error('Network error')));
    expect(result.current.error).toBeNull();
    expect(result.current.loading).toBe(true);
  });

  it('replaces the user in the list and returns updated dto', async () => {
    const updatedUser = { ...mockUser, name: 'John Updated' };
    vi.mocked(updateUser).mockResolvedValue(updatedUser as any);

    const result = await setup();
    let returnValue: any;

    await act(async () => {
      returnValue = await result.current.actions.update(mockUser as any);
    });

    expect(result.current.users[0]).toEqual(updatedUser);
    expect(result.current.users[1]).toEqual(mockUser2);
    expect(returnValue).toEqual(updatedUser);
  });

  it('adds new user to top and calls api with benefit ALL', async () => {
    const newUser = { id: 3, name: 'Bob', status: 'ACTIVE' };
    vi.mocked(createUser).mockResolvedValue(newUser as any);

    const result = await setup();
    await act(async () => { await result.current.actions.create({ name: 'Bob' }); });

    expect(result.current.users[0]).toEqual(newUser);
    expect(result.current.users).toHaveLength(3);
    expect(createUser).toHaveBeenCalledWith({ name: 'Bob', benefit: 'ALL' });
  });

  // Soft-delete: sets status to DELETED instead of removing from the list
  describe('remove', () => {

    it('soft-deletes user and resets deletingUserId', async () => {
      vi.mocked(deleteUser).mockResolvedValue(undefined as any);
      const result = await setup();
      await act(async () => { await result.current.actions.remove(1); });

      expect(result.current.users.find(u => u.id === 1)?.status).toBe('DELETED');
      expect(result.current.users.find(u => u.id === 2)?.status).toBe('ACTIVE');
      expect(result.current.deletingUserId).toBeNull();
    });

    it('resets deletingUserId even if deleteUser throws', async () => {
      vi.mocked(deleteUser).mockRejectedValue(new Error('Server error'));
      const result = await setup();
      await act(async () => { await result.current.actions.remove(1).catch(() => {}); });

      expect(result.current.deletingUserId).toBeNull();
    });

  });

});