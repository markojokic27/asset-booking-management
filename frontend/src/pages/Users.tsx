import AddIcon from '@mui/icons-material/Add';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { SearchInput } from '../components/ui/SearchBar';
import { Pagination } from '../components/ui/Pagination';
import { UserModal } from '../features/user/components/UserModal';
import { UserEditModal } from '../features/user/components/UserEditModal';
import { UserCreateModal } from '../features/user/components/UserCreateModal';
import { UserBookingsModal } from '../features/user/components/UserBookingsModal';
import { UsersTable } from '../features/user/components/UsersTable';
import {
  getFullName,
  useUsers,
} from '../features/user/hooks/useUsers';

export default function Users() {
  const { list, sorting, pagination, selection, modals, actions } = useUsers({
    pageSize: 10,
  });

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div className="flex w-full flex-col items-start justify-between gap-4 sm:flex-row sm:items-center sm:gap-6">
        <h1 className="text-3xl leading-11 font-black tracking-widest text-black dark:text-white">
          Users
        </h1>

        <div className="flex w-full flex-col gap-3 sm:w-auto sm:flex-row sm:items-center sm:gap-4">
          <Button
            size="sm"
            variant="outline"
            iconLeft={<FileDownloadOutlinedIcon fontSize="small" />}
            className="w-full shadow-none sm:w-auto"
            onClick={actions.exportUsersCsv}
          >
            Export
          </Button>
          <Button
            size="sm"
            iconLeft={<AddIcon fontSize="small" />}
            className="w-full shadow-none sm:w-auto"
            onClick={modals.openCreateUser}
          >
            New
          </Button>
        </div>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />
      <div className="mt-6 flex w-full justify-end">
        <SearchInput
          value={list.search}
          onChange={list.setSearch}
          placeholder="Search users..."
          className="mb-0 w-70"
        />
      </div>
      <div className="mt-6 w-full">
        <UsersTable
          data={list.pagedUsers}
          nameSortDir={sorting.nameSortDir}
          onToggleNameSort={sorting.toggleNameSortDir}
          onView={modals.openViewUser}
          onEdit={modals.openEditUser}
          onBookings={modals.openBookings}
          emptyMessage={
            list.isLoading
              ? 'Loading users...'
              : list.error
                ? list.error
                : 'No users yet.'
          }
        />
      </div>

      {list.filteredUsers.length > 0 && (
        <Pagination
          page={pagination.safePage}
          totalPages={pagination.totalPages}
          items={pagination.items}
          onPageChange={pagination.setPage}
        />
      )}

      <UserModal
        isOpen={modals.isUserModalOpen}
        onClose={modals.closeViewUser}
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

      <UserEditModal
        isOpen={modals.isUserEditModalOpen}
        onClose={modals.closeEditUser}
        user={selection.activeUser}
        onSave={actions.saveEditedUser}
      />

      <UserCreateModal
        isOpen={modals.isUserCreateModalOpen}
        onClose={modals.closeCreateUser}
        onCreate={actions.createNewUser}
      />

      <UserBookingsModal
        isOpen={modals.isBookingsModalOpen}
        onClose={modals.closeBookings}
        user={
          selection.activeUser
            ? {
              id: selection.activeUser.id,
              fullName: getFullName(selection.activeUser),
            }
            : null
        }
      />
    </LayoutColumn>
  );
}
