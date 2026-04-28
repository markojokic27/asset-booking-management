import AddIcon from '@mui/icons-material/Add';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { DeleteModal } from '../components/ui/DeleteModal';
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
import type { UserDto } from '../features/user/types';

type ModalState =
  | { type: 'none' }
  | { type: 'delete'; user: UserDto };

export default function Users() {
  const { t } = useTranslation();
  const { list, sorting, pagination, selection, modals, actions } = useUsers({
    pageSize: 10,
  });
  const [modal, setModal] = useState<ModalState>({ type: 'none' });

  const closeDeleteModal = () => setModal({ type: 'none' });

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div className="flex w-full flex-col items-start justify-between gap-4 sm:flex-row sm:items-center sm:gap-6">
        <h1 className="text-3xl leading-11 font-black tracking-widest text-black dark:text-white">
          {t('users.title')}
        </h1>

        <div className="flex w-full flex-col gap-3 sm:w-auto sm:flex-row sm:items-center sm:gap-4">
          <Button
            size="sm"
            variant="outline"
            iconLeft={<FileDownloadOutlinedIcon fontSize="small" />}
            className="w-full shadow-none sm:w-auto"
            onClick={actions.exportUsersCsv}
          >
            {t('users.actions.export')}
          </Button>
          <Button
            size="sm"
            iconLeft={<AddIcon fontSize="small" />}
            className="w-full shadow-none sm:w-auto"
            onClick={modals.openCreateUser}
          >
            {t('users.actions.new')}
          </Button>
        </div>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />
      <div className="mt-6 flex w-full justify-end">
        <SearchInput
          value={list.search}
          onChange={list.setSearch}
          placeholder={t('users.search.placeholder')}
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
          onDelete={(user) => setModal({ type: 'delete', user })}
          emptyMessage={
            list.isLoading
              ? t('users.empty.loading')
              : list.error
                ? list.error
                : t('users.empty.none')
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

      <DeleteModal
        isOpen={modal.type === 'delete'}
        onClose={closeDeleteModal}
        item={modal.type === 'delete' ? modal.user : null}
        getItemName={(user) => getFullName(user)}
        title={t('users.delete.title')}
        description={t('users.delete.description', {
          name: modal.type === 'delete' ? getFullName(modal.user) : '',
        })}
        onConfirm={async () => {
          if (modal.type === 'delete') {
            await actions.deleteExistingUser(modal.user);
            closeDeleteModal();
          }
        }}
      />
    </LayoutColumn>
  );
}
