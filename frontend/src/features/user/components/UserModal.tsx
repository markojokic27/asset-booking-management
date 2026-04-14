import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';

const statusConfig: Record<string, { label: string; className: string }> = {
  ACTIVE: {
    label: 'Active',
    className: 'bg-(--color-status-active-bg) text-(--color-status-active-text)',
  },
  INACTIVE: {
    label: 'Inactive',
    className: 'bg-(--color-status-inactive-bg) text-(--color-status-inactive-text)',
  },
};

export type UserModalUser = {
  id: string;
  name: string;
  email: string;
  username: string;
  role: string;
  status: string;
  departmentId: number;
  managerEmail: string;
  notes?: string;
};

export type UserModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserModalUser | null;
};

export const UserModal: React.FC<UserModalProps> = ({ isOpen, onClose, user }) => {
  if (!isOpen || !user) return null;

  const status = statusConfig[user.status] ?? { label: user.status, className: '' };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel="User details"
      headerRight={
        <IconButton onClick={onClose} aria-label="Close">
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <span
          className={[
            'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
            status.className,
          ].join(' ')}
        >
          {status.label}
        </span>

        <div>
          <p className="text-sm text-(--color-modal-label)">Name</p>
          <p data-testid="user-name-value" className="font-medium text-(--color-text)">
            {user.name}
          </p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">Email</p>
          <p data-testid="user-email-value" className="font-medium text-(--color-text)">
            {user.email}
          </p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">Username</p>
          <p className="font-medium text-(--color-text)">{user.username}</p>
        </div>

        <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
          <div>
            <p className="text-sm text-(--color-modal-label)">Role</p>
            <p className="font-medium text-(--color-text)">{user.role}</p>
          </div>

          <div>
            <p className="text-sm text-(--color-modal-label)">Status</p>
            <p className="font-medium text-(--color-text)">{status.label}</p>
          </div>

          <div>
            <p className="text-sm text-(--color-modal-label)">Department</p>
            <p className="font-medium text-(--color-text)">{user.departmentId}</p>
          </div>

          <div>
            <p className="text-sm text-(--color-modal-label)">Manager email</p>
            <p className="font-medium text-(--color-text)">{user.managerEmail}</p>
          </div>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">Notes</p>
          <p className="font-medium text-(--color-text)">{user.notes || '-'}</p>
        </div>
      </div>
    </Modal>
  );
};

