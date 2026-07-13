// Hooks
import { useUsersData } from './useUsersData';
import { useUserFilters } from './useUsersFilters';
import { usePagination } from './usePagination';
import { useUserModals } from './useUserModals';

// Utilis
import { exportUsersCsv } from '../utils/csv';

export function useUsers() {
  const data = useUsersData();
  const filters = useUserFilters(data.users);
  const pagination = usePagination(filters.data, 10);
  const modals = useUserModals();

  return {
    list: {
      users: data.users,
      filteredUsers: filters.data,
      pagedUsers: pagination.paged,
      isLoading: data.loading,
      error: data.error,
      search: filters.search,
      setSearch: filters.setSearch,
      showDeleted: filters.showDeleted,
      toggleShowDeleted: filters.toggleShowDeleted,
      selectedRole: filters.selectedRole,
      setSelectedRole: filters.setSelectedRole,
      selectedDepartment: filters.selectedDepartment,
      setSelectedDepartment: filters.setSelectedDepartment,
    },

    sorting: {
      nameSortDir: filters.sortDir,
      toggleNameSortDir: filters.toggleSort,
    },

    pagination,

    selection: {
      activeUser: modals.activeUser,
    },

    modals,

    actions: {
      ...data.actions,
      exportUsersCsv: () => exportUsersCsv(filters.data),
    },

    meta: {
      deletingUserId: data.deletingUserId,
    },
  };
}
