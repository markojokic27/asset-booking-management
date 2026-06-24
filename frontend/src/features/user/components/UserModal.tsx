// external dependencies
import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { useTranslation } from 'react-i18next';

// components
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';

// hooks
import { useDepartments } from '../../department/hooks/useDepartments';

// types
import type { UserModalUser } from '../types';

const statusClassNameConfig: Record<string, string> = {
  ACTIVE: 'bg-(--color-status-active-bg) text-(--color-status-active-text)',
  INACTIVE: 'bg-(--color-status-inactive-bg) text-(--color-status-inactive-text)',
};

export type UserModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserModalUser | null;
};

export const UserModal: React.FC<UserModalProps> = ({ isOpen, onClose, user }) => {
  const { t } = useTranslation();
  const { getDepartmentName } = useDepartments();
  if (!isOpen || !user) return null;

  const departmentLabel =
    getDepartmentName(user.departmentId) ?? t('users.modals.common.emptyValue');

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
      title={<h2 className="text-2xl font-bold">{user.name}</h2>}
      headerRight={
        <IconButton data-testid="user-close-button" onClick={onClose} aria-label={t('users.modals.common.closeAria')}>
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        {/* Status */}
        <span
          className={[
            'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
            statusClassName,
          ].join(' ')}
        >
          {statusLabel}
        </span>

        {/* Name */}
        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.name')}</p>
          <p data-testid="user-name" className="font-medium text-(--color-text)">
            {user.name}
          </p>
        </div>

        {/* Email */}
        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.email')}</p>
          <p data-testid="user-email" className="font-medium text-(--color-text)">
            {user.email}
          </p>
        </div>

        {/* Username */}
        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.username')}</p>
          <p data-testid="user-username" className="font-medium text-(--color-text)">{user.username}</p>
        </div>

        {/* Role */}
        <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
          <div>
            <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.role')}</p>
            <p data-testid="user-role" className="font-medium text-(--color-text)">{user.role}</p>
          </div>

          {/* Department */}
          <div>
            <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.department')}</p>
            <p data-testid="user-department-id" className="font-medium text-(--color-text)">{departmentLabel}</p>
          </div>

          {/* Manager Email */}
          <div>
            <p className="text-sm text-(--color-modal-label)">{t('users.modals.view.fields.managerEmail')}</p>
            <p data-testid="user-manager-email" className="font-medium text-(--color-text)">{user.managerEmail}</p>
          </div>
        </div>

        {/* Notes */}
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
