// External packages
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import AddIcon from '@mui/icons-material/Add';
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';

//Components
import { LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { SearchInput } from '../components/ui/SearchBar';
import { Pagination } from '../components/ui/Pagination';
import { DeleteModal } from '../components/ui/DeleteModal';
import { UserModal } from '../features/user/components/UserModal';
import { UsersTable } from '../features/user/components/UsersTable';
import { UserEditModal } from '../features/user/components/UserEditModal';
import { UserCreateModal } from '../features/user/components/UserCreateModal';
import { UserBookingsModal } from '../features/user/components/UserBookingsModal';

// Hooks
import { getFullName } from '../features/user/utilis/users';
import { useUsers } from '../features/user/hooks/useUsers';

// Types
import type { UserDto } from '../features/user/types';

type DeleteState = { type: 'none' } | { type: 'delete'; user: UserDto };

export default function Users() {
  const { t } = useTranslation();

  const { list, sorting, pagination, selection, modals, actions } = useUsers();

  const [deleteState, setDeleteState] = useState<DeleteState>({
    type: 'none',
  });

  const closeDeleteModal = () => setDeleteState({ type: 'none' });

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div className="flex w-full flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <h1 className="text-3xl font-black tracking-widest">
          {t('users.title')}
        </h1>

        <div className="flex flex-col gap-3 sm:flex-row">
          <Button
            size="sm"
            variant="outline"
            iconLeft={<FileDownloadOutlinedIcon fontSize="small" />}
            onClick={actions.exportUsersCsv}
          >
            {t('users.actions.export')}
          </Button>

          <Button
            size="sm"
            iconLeft={<AddIcon fontSize="small" />}
            onClick={() => modals.open('create')}
          >
            {t('users.actions.new')}
          </Button>
        </div>
      </div>

      <div className="mt-6 flex justify-end">
        <SearchInput
          value={list.search}
          onChange={list.setSearch}
          placeholder={t('users.search.placeholder')}
          className="w-70"
        />
      </div>

      <div className="mt-6">
        <UsersTable
          data={list.pagedUsers}
          nameSortDir={sorting.nameSortDir}
          onToggleNameSort={sorting.toggleNameSortDir}
          onView={(u) => modals.open('view', u)}
          onEdit={(u) => modals.open('edit', u)}
          onBookings={(u) => modals.open('bookings', u)}
          onDelete={(u) => setDeleteState({ type: 'delete', user: u })}
          emptyMessage={
            list.isLoading
              ? t('users.empty.loading')
              : list.error || t('users.empty.none')
          }
        />
      </div>

      {list.filteredUsers.length > 0 && (
        <Pagination
          page={pagination.page}
          totalPages={pagination.totalPages}
          items={pagination.items}
          onPageChange={pagination.setPage}
        />
      )}

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

      <UserEditModal
        isOpen={modals.modal === 'edit'}
        onClose={modals.close}
        user={selection.activeUser}
        onSave={async (user) => {
          await actions.update(user);
        }}
      />

      <UserCreateModal
        isOpen={modals.modal === 'create'}
        onClose={modals.close}
        onCreate={actions.create}
      />

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
          if (deleteState.type === 'delete') {
            await actions.remove(deleteState.user.id);
            closeDeleteModal();
          }
        }}
      />
    </LayoutColumn>
  );
}
