import { describe, it, expect, vi, beforeEach } from 'vitest';
import { renderHook } from '@testing-library/react';


vi.mock('../../features/user/hooks/useUsersData',    () => ({ useUsersData:    vi.fn() }));
vi.mock('../../features/user/hooks/useUsersFilters', () => ({ useUserFilters:  vi.fn() }));
vi.mock('../../features/user/hooks/usePagination',   () => ({ usePagination:   vi.fn() }));
vi.mock('../../features/user/hooks/useUserModals',   () => ({ useUserModals:   vi.fn() }));
vi.mock('../../features/user/utils/csv',            () => ({ exportUsersCsv:  vi.fn() }));

import { useUsersData }   from '../../features/user/hooks/useUsersData';
import { useUserFilters } from '../../features/user/hooks/useUsersFilters';
import { usePagination }  from '../../features/user/hooks/usePagination';
import { useUserModals }  from '../../features/user/hooks/useUserModals';
import { exportUsersCsv } from '../../features/user/utils/csv';
import { useUsers }       from '../../features/user/hooks/useUsers';


const mockUsers = [{ id: 1, name: 'John' }];

const mockData = {
  users: mockUsers,
  loading: false,
  error: null,
  deletingUserId: null,
  actions: { update: vi.fn(), create: vi.fn(), remove: vi.fn() },
};

const mockFilters = {
  data: mockUsers,
  search: '',
  setSearch: vi.fn(),
  showDeleted: false,
  toggleShowDeleted: vi.fn(),
  selectedRole: '' as const,
  setSelectedRole: vi.fn(),
  selectedDepartment: '' as const,
  setSelectedDepartment: vi.fn(),
  sortDir: 'asc' as const,
  toggleSort: vi.fn(),
};

const mockPagination = {
  paged: mockUsers,
  page: 1,
  totalPages: 1,
  setPage: vi.fn(),
};

const mockModals = {
  activeUser: null,
  openModal: vi.fn(),
  closeModal: vi.fn(),
};


describe('useUsers', () => {

  beforeEach(() => {
    vi.mocked(useUsersData).mockReturnValue(mockData as any);
    vi.mocked(useUserFilters).mockReturnValue(mockFilters as any);
    vi.mocked(usePagination).mockReturnValue(mockPagination as any);
    vi.mocked(useUserModals).mockReturnValue(mockModals as any);
  });

  // Runs the hook once and returns result — avoids repeating renderHook in every test
  const setup = () => renderHook(() => useUsers()).result;

  it('passes correct args to sub-hooks', () => {
    renderHook(() => useUsers());
    expect(useUserFilters).toHaveBeenCalledWith(mockUsers);
    expect(usePagination).toHaveBeenCalledWith(mockFilters.data, 10);
  });

  it('returns correct list state', () => {
    const { current } = setup();
    expect(current.list.users).toBe(mockUsers);
    expect(current.list.filteredUsers).toBe(mockFilters.data);
    expect(current.list.pagedUsers).toBe(mockPagination.paged);
    expect(current.list.isLoading).toBe(false);
    expect(current.list.error).toBeNull();
    expect(current.list.search).toBe('');
    expect(current.list.setSearch).toBe(mockFilters.setSearch);
    expect(current.list.showDeleted).toBe(false);
    expect(current.list.toggleShowDeleted).toBe(mockFilters.toggleShowDeleted);
    expect(current.list.selectedRole).toBe('');
    expect(current.list.setSelectedRole).toBe(mockFilters.setSelectedRole);
    expect(current.list.selectedDepartment).toBe('');
    expect(current.list.setSelectedDepartment).toBe(mockFilters.setSelectedDepartment);
  });

  it('returns correct sorting controls', () => {
    const { current } = setup();
    expect(current.sorting.nameSortDir).toBe('asc');
    expect(current.sorting.toggleNameSortDir).toBe(mockFilters.toggleSort);
  });

  it('returns correct selection and meta state', () => {
    const { current } = setup();
    expect(current.selection.activeUser).toBeNull();
    expect(current.meta.deletingUserId).toBeNull();
  });

  it('spreads data.actions and adds exportUsersCsv', () => {
    const { current } = setup();
    expect(current.actions.update).toBe(mockData.actions.update);
    expect(current.actions.create).toBe(mockData.actions.create);
    expect(current.actions.remove).toBe(mockData.actions.remove);

    current.actions.exportUsersCsv();
    expect(exportUsersCsv).toHaveBeenCalledWith(mockFilters.data);
  });

});