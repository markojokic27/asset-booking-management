// External packages
import { useMemo, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import AddIcon from '@mui/icons-material/Add';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';

// Reusable UI components
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { SearchInput } from '../components/ui/SearchBar';
import { Pagination } from '../components/ui/Pagination';
import { DeleteModal } from '../components/ui/DeleteModal';
import { FormDropdown } from '../components/ui/FormDropdown';

// User-related feature components
import { UserModal } from '../features/user/components/UserModal';
import { UsersTable } from '../features/user/components/UsersTable';
import { UserFormModal } from '../features/user/components/UserFormModal';
import { UserBookingsModal } from '../features/user/components/UserBookingsModal';
import { UserReportModal } from '../features/user/components/UserReportModal';
import { ShowDeletedFilter } from '../features/user/components/ShowDeletedFilter';

// Utility functions
import { getFullName, isAdmin } from '../features/user/utils/users';

// Custom hooks
import { useUsers } from '../features/user/hooks/useUsers';
import { useDepartments } from '../features/department/hooks/useDepartments';
import { useAuth } from '../features/auth/context/AuthContext';

// Types
import type { UserDto, UserRole } from '../features/user/types';
import { userRoleSchema } from '../features/user/validation';
import { Toast } from '../components/ui/Toast';

type DeleteState = { type: 'none' } | { type: 'delete'; user: UserDto };

export default function Users() {
  // current user
  const { user, isLoading } = useAuth();

  // if the user is not an admin, redirect to the bookings page
  if (!isLoading && !isAdmin(user)) {
    return <Navigate to="/bookings" replace />;
  }

  return <UsersPage />;
}

function UsersPage() {
  const { t } = useTranslation();

  const { list, sorting, pagination, selection, modals, actions } = useUsers();

  const { departmentOptions } = useDepartments();

  const [deleteState, setDeleteState] = useState<DeleteState>({
    type: 'none',
  });

  const closeDeleteModal = () => setDeleteState({ type: 'none' });

  // resolve role filter options
  const roleFilterOptions = useMemo(
    () => [
      { value: '', label: t('users.filters.allRoles') },
      ...userRoleSchema.options.map((role) => ({
        value: role,
        label: t(`users.roles.${role}`),
      })),
    ],
    [t]
  );

  // resolve department filter options
  const departmentFilterOptions = useMemo(
    () => [
      { value: '', label: t('users.filters.allDepartments') },
      ...departmentOptions,
    ],
    [departmentOptions, t]
  );

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      {/* Page header and action buttons */}
      <div className="flex w-full flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <h1 className="text-3xl font-black tracking-widest">
          {t('users.title')}
        </h1>

        <div className="flex flex-col gap-3 sm:flex-row">
          {/* Export users to CSV */}
          <Button
            size="sm"
            variant="outline"
            iconLeft={<FileDownloadOutlinedIcon fontSize="small" />}
            onClick={actions.exportUsersCsv}
          >
            {t('users.actions.export')}
          </Button>

          {/* Open modal for creating a new user */}
          <Button
            data-testid="add-user-button"
            size="sm"
            iconLeft={<AddIcon fontSize="small" />}
            onClick={() => modals.open('create')}
          >
            {t('users.actions.new')}
          </Button>
        </div>
      </div>

      {/* Divider line */}
      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      {/* Filters and search section */}
      <div className="mt-6 flex w-full flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
        <div className="flex flex-col items-start gap-3">
          <ShowDeletedFilter
            checked={list.showDeleted}
            onToggle={list.toggleShowDeleted}
          />

          {/* Role and department filters */}
          <div className="flex flex-wrap items-center gap-3">
            {/* Role filter */}
            <div className="relative w-full sm:w-44">
              <FormDropdown
                data-testid="user-role-filter"
                id="users-role-filter"
                aria-label={t('users.filters.role')}
                value={list.selectedRole}
                onChange={(event) =>
                  list.setSelectedRole(event.target.value as UserRole | '')
                }
                options={roleFilterOptions}
                className="h-10 cursor-pointer border-2 py-0 text-(--color-table-text) shadow-none"
              />
            </div>

            {/* Department filter */}
            <div className="relative w-full sm:w-52">
              <FormDropdown
                data-testid="user-department-filter"
                id="users-department-filter"
                aria-label={t('users.filters.department')}
                value={list.selectedDepartment}
                onChange={(event) =>
                  list.setSelectedDepartment(
                    event.target.value === '' ? '' : Number(event.target.value)
                  )
                }
                options={departmentFilterOptions}
                className="h-10 cursor-pointer border-2 py-0 text-(--color-table-text) shadow-none"
              />
            </div>
          </div>
        </div>

        {/* Search users by input value */}
        <SearchInput
          value={list.search}
          onChange={list.setSearch}
          placeholder={t('users.search.placeholder')}
          className="w-full sm:w-70"
        />
      </div>

      {/* Users table */}
      <div className="mt-6">
        <UsersTable
          data={list.pagedUsers}
          // Sorting configuration
          nameSortDir={sorting.nameSortDir}
          onToggleNameSort={sorting.toggleNameSortDir}
          // User actions
          onView={(u) => modals.open('view', u)}
          onEdit={(u) => modals.open('edit', u)}
          onBookings={(u) => modals.open('bookings', u)}
          onDelete={(u) => setDeleteState({ type: 'delete', user: u })}
          onReport={(u) => modals.open('report', u)}
          // Display loading, error or empty state message
          emptyMessage={
            list.isLoading
              ? t('users.empty.loading')
              : list.error || t('users.empty.none')
          }
        />
      </div>

      {/* Show pagination only if users exist */}
      {list.filteredUsers.length > 0 && (
        <Pagination
          page={pagination.page}
          totalPages={pagination.totalPages}
          items={pagination.items}
          onPageChange={pagination.setPage}
        />
      )}

      {/* Modal for viewing user details */}
      <UserModal
        isOpen={modals.modal === 'view'}
        onClose={modals.close}
        user={
          selection.activeUser
            ? {
                id: selection.activeUser.id,
                name: getFullName(selection.activeUser),
                email: selection.activeUser.email,
                username: selection.activeUser.username,
                role: selection.activeUser.role,
                status: selection.activeUser.status,
                departmentId: selection.activeUser.departmentId,
                managerEmail: selection.activeUser.managerEmail,
                notes: selection.activeUser.notes,
              }
            : null
        }
      />

      {/* Modal for creating or editing users */}
      <UserFormModal
        isOpen={modals.modal === 'create' || modals.modal === 'edit'}
        mode={modals.modal === 'create' ? 'create' : 'edit'}
        user={selection.activeUser}
        onClose={modals.close}
        onCreate={actions.create}
        onSave={async (user) => {
          await actions.update(user);
        }}
      />

      {/* Modal displaying user bookings */}
      <UserBookingsModal
        isOpen={modals.modal === 'bookings'}
        onClose={modals.close}
        user={
          selection.activeUser
            ? {
                id: selection.activeUser.id,
                fullName: getFullName(selection.activeUser),
              }
            : null
        }
      />

      {/* Modal for user reports */}
      <UserReportModal
        isOpen={modals.modal === 'report'}
        onClose={modals.close}
        user={
          selection.activeUser
            ? {
                id: selection.activeUser.id,
                name: selection.activeUser.name,
                surname: selection.activeUser.surname,
              }
            : null
        }
      />

      {/* Confirmation modal before deleting a user */}
      <DeleteModal
        isOpen={deleteState.type === 'delete'}
        onClose={closeDeleteModal}
        item={deleteState.type === 'delete' ? deleteState.user : null}
        getItemName={(u) => getFullName(u)}
        title={t('users.delete.title')}
        description={t('users.delete.description', {
          name:
            deleteState.type === 'delete' ? getFullName(deleteState.user) : '',
        })}
        onConfirm={async () => {
          // Delete selected user and close modal
          if (deleteState.type === 'delete') {
            await actions.remove(deleteState.user.id);
            Toast.success(
              t('layout.toast.userDeleted', {
                name: getFullName(deleteState.user),
              })
            );
            closeDeleteModal();
          }
        }}
      />
    </LayoutColumn>
  );
}
