import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { useTranslation } from 'react-i18next';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';
import type { UserDto } from '../types';

const statusClassNameConfig: Record<string, string> = {
  ACTIVE: 'bg-(--color-status-active-bg) text-(--color-status-active-text)',
  INACTIVE: 'bg-(--color-status-inactive-bg) text-(--color-status-inactive-text)',
};

export type UserModalUser = {
  id: UserDto['id'];
  name: string;
} & Pick<
  UserDto,
  | 'email'
  | 'username'
  | 'role'
  | 'status'
  | 'departmentId'
  | 'managerEmail'
  | 'notes'
>;

export type UserModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserModalUser | null;
};

export const UserModal: React.FC<UserModalProps> = ({ isOpen, onClose, user }) => {
  const { t } = useTranslation();
  if (!isOpen || !user) return null;

  const statusLabel =
    t(`users.status.${String(user.status).toLowerCase()}`, {
      defaultValue: String(user.status),
    }) || String(user.status);
  const statusClassName = statusClassNameConfig[user.status] ?? '';

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('users.modals.view.ariaLabel')}
      headerRight={
        <IconButton onClick={onClose} aria-label={t('users.modals.common.closeAria')}>
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <span
          className={[
            'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
            statusClassName,
          ].join(' ')}
        >
          {statusLabel}
        </span>

        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.name')}</p>
          <p data-testid="user-name" className="font-medium text-(--color-text)">
            {user.name}
          </p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.email')}</p>
          <p data-testid="user-email" className="font-medium text-(--color-text)">
            {user.email}
          </p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.username')}</p>
          <p data-testid="user-username" className="font-medium text-(--color-text)">{user.username}</p>
        </div>

        <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
          <div>
            <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.role')}</p>
            <p data-testid="user-role" className="font-medium text-(--color-text)">{user.role}</p>
          </div>

          <div>
            <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.department')}</p>
            <p data-testid="user-department-id" className="font-medium text-(--color-text)">{user.departmentId}</p>
          </div>

          <div>
            <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.managerEmail')}</p>
            <p data-testid="user-manager-email" className="font-medium text-(--color-text)">{user.managerEmail}</p>
          </div>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.notes')}</p>
          <p data-testid="user-note" className="font-medium text-(--color-text)">
            {user.notes || t('users.modals.common.emptyValue')}
          </p>
        </div>
      </div>
    </Modal>
  );
};

