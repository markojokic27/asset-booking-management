// External packages
import { useEffect, useState } from 'react';

// Components
import { LayoutColumn } from '../components/layout/Layout';

type UserDto = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  role: 'EMPLOYEE' | 'ADMIN' | 'MANAGER';
  status: 'ACTIVE' | 'INACTIVE';
  departmentId: number;
  managerEmail: string;
  notes?: string;
};

const mockUser: UserDto = {
  id: '1',
  firstName: 'Ana',
  lastName: 'Horvat',
  email: 'ana.horvat@example.com',
  username: 'ana.horvat',
  role: 'ADMIN',
  status: 'ACTIVE',
  departmentId: 1,
  managerEmail: 'manager@example.com',
  notes: 'Team lead.',
};

type InfoRowProps = {
  label: string;
  value?: string | null;
  valueClassName?: string;
};

function InfoRow({ label, value, valueClassName = '' }: InfoRowProps) {
  return (
    <div className="flex flex-col gap-1 border-b border-(--color-table-border) py-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
      <span className="text-sm font-semibold tracking-wide text-(--color-table-text)">
        {label}
      </span>
      <span
        className={`text-sm text-black dark:text-white sm:text-right ${valueClassName}`}
      >
        {value && value.trim() !== '' ? value : '-'}
      </span>
    </div>
  );
}

function getRoleBadgeClass(role: UserDto['role']) {
  switch (role) {
    case 'ADMIN':
      return 'bg-blue-50 text-blue-700 ring-1 ring-blue-200 dark:bg-blue-950/40 dark:text-blue-300 dark:ring-blue-900';
    case 'MANAGER':
      return 'bg-purple-50 text-purple-700 ring-1 ring-purple-200 dark:bg-purple-950/40 dark:text-purple-300 dark:ring-purple-900';
    case 'EMPLOYEE':
    default:
      return 'bg-slate-100 text-slate-700 ring-1 ring-slate-200 dark:bg-slate-800 dark:text-slate-300 dark:ring-slate-700';
  }
}

function getStatusBadgeClass(status: UserDto['status']) {
  switch (status) {
    case 'ACTIVE':
      return 'bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200 dark:bg-emerald-950/40 dark:text-emerald-300 dark:ring-emerald-900';
    case 'INACTIVE':
    default:
      return 'bg-red-50 text-red-700 ring-1 ring-red-200 dark:bg-red-950/40 dark:text-red-300 dark:ring-red-900';
  }
}

export default function AccountInfo() {
  const [user, setUser] = useState<UserDto | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        setIsLoading(true);
        setError(null);

        // const response = await api.get('/users/me');
        // setUser(response.data);

        setUser(mockUser);
      } catch {
        setError('Failed to load user data.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchUser();
  }, []);

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div data-testid="account-heading" className="flex w-full items-center justify-between gap-6">
        <h1 className="text-3xl leading-11 font-black tracking-widest text-black dark:text-white">
          Account info
        </h1>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      {isLoading ? (
        <div className="mt-6 rounded-2xl border border-(--color-table-border) bg-white p-6 shadow-none dark:bg-(--color-surface)">
          <p className="text-sm text-(--color-table-text)">Loading user data...</p>
        </div>
      ) : error ? (
        <div className="mt-6 rounded-2xl border border-red-200 bg-red-50 p-6 dark:border-red-900 dark:bg-red-950/30">
          <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
        </div>
      ) : !user ? (
        <div className="mt-6 rounded-2xl border border-(--color-table-border) bg-white p-6 shadow-none dark:bg-(--color-surface)">
          <p className="text-sm text-(--color-table-text)">No user data available.</p>
        </div>
      ) : (
        <div className="mt-6 grid w-full grid-cols-1 gap-6 xl:grid-cols-2">
          <section className="rounded-2xl border border-(--color-table-border) bg-white p-6 dark:bg-(--color-surface)">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-xs font-bold tracking-[0.18em] text-(--color-table-text) uppercase">
                  Profile
                </p>
                <h2 data-testid="account-fullname" className="mt-2 text-xl font-bold text-black dark:text-white">
                  {user.firstName} {user.lastName}
                </h2>
                <p data-testid="account-email" className="mt-1 text-sm text-(--color-table-text)">
                  {user.email}
                </p>
              </div>
            </div>

            <div className="mt-6">
              <InfoRow label="ID" value={user.id} />
              <InfoRow label="First name" value={user.firstName} />
              <InfoRow label="Last name" value={user.lastName} />
              <InfoRow label="Username" value={user.username} />
              <InfoRow label="Email" value={user.email} />
            </div>
          </section>

          <section className="rounded-2xl border border-(--color-table-border) bg-white p-6 dark:bg-(--color-surface)">
            <p className="text-xs font-bold tracking-[0.18em] text-(--color-table-text) uppercase">
              Work details
            </p>
            <h2 className="mt-2 text-xl font-bold text-black dark:text-white">
              Account details
            </h2>

            <div className="mt-6">
              <div className="flex flex-col gap-1 border-b border-(--color-table-border) py-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
                <span className="text-sm font-semibold tracking-wide text-(--color-table-text)">
                  Role
                </span>
                <span data-testid="account-role"
                  className={`inline-flex w-fit rounded-full px-2.5 py-1 text-xs font-semibold sm:ml-auto ${getRoleBadgeClass(user.role)}`}
                >
                  {user.role}
                </span>
              </div>

              <div className="flex flex-col gap-1 border-b border-(--color-table-border) py-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
                <span className="text-sm font-semibold tracking-wide text-(--color-table-text)">
                  Status
                </span>
                <span data-testid="account-status"
                  className={`inline-flex w-fit rounded-full px-2.5 py-1 text-xs font-semibold sm:ml-auto ${getStatusBadgeClass(user.status)}`}
                >
                  {user.status}
                </span>
              </div>

              <InfoRow
                label="Department"
                value={String(user.departmentId)}
              />
              <InfoRow label="Manager email" value={user.managerEmail} />
              <InfoRow label="Notes" value={user.notes ?? '-'} />
            </div>
          </section>
        </div>
      )}
    </LayoutColumn>
  );
}