import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';

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
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label="User details"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-[800px] overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-end px-8 pt-6 pb-4">
          <button
            type="button"
            onClick={onClose}
            aria-label="Close"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
          >
            <CloseIcon className="pointer-events-none" />
          </button>
        </div>
        <div className="mx-8 h-px bg-(--color-table-border)" />

        <div className="px-8 py-8">
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
              <p className="font-medium text-(--color-text)">{user.name}</p>
            </div>

            <div>
              <p className="text-sm text-(--color-modal-label)">Email</p>
              <p className="font-medium text-(--color-text)">{user.email}</p>
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
        </div>

        <div className="mx-8 h-px bg-(--color-table-border)" />
        <div className="px-8 py-5" />
      </div>
    </div>
  );
};

