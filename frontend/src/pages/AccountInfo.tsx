// External packages
import { useState } from 'react';
import { useTranslation } from 'react-i18next';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { BadgeRow } from '../components/ui/BadgeRow';
import { InfoRow } from '../components/ui/InfoRow';
import { Button } from '../components/ui/Button';

// Hooks
import { useCurrentUser } from '../features/user/hooks/useCurrentUser';
import { ChangePasswordModal } from '../features/user/components/ChangePasswordModal';

// Types
import type { UserDto } from '../features/user/types';

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
  const { t } = useTranslation();
  const { user, isLoading, error } = useCurrentUser();
  const [passwordModalOpen, setPasswordModalOpen] = useState(false);

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div
        data-testid="account-heading"
        className="flex w-full items-center justify-between gap-6"
      >
        <h1 className="text-3xl leading-11 font-black tracking-widest text-black dark:text-white">
          {t('account.heading')}
        </h1>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      {isLoading ? (
        <div className="mt-6 rounded-2xl border border-(--color-table-border) bg-white p-6 shadow-none dark:bg-(--color-surface)">
          <p className="text-sm text-(--color-table-text)">
            {t('account.loading')}
          </p>
        </div>
      ) : error ? (
        <div className="mt-6 rounded-2xl border border-red-200 bg-red-50 p-6 dark:border-red-900 dark:bg-red-950/30">
          <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
        </div>
      ) : !user ? (
        <div className="mt-6 rounded-2xl border border-(--color-table-border) bg-white p-6 shadow-none dark:bg-(--color-surface)">
          <p className="text-sm text-(--color-table-text)">
            {t('account.empty')}
          </p>
        </div>
      ) : (
        <div className="mt-6 grid w-full grid-cols-1 gap-6 xl:grid-cols-2">
          <section className="flex h-full flex-col rounded-2xl border border-(--color-table-border) bg-white p-6 dark:bg-(--color-surface)">
            <div>
              <p className="text-xs font-bold tracking-[0.18em] text-(--color-table-text) uppercase">
                {t('account.sections.profile')}
              </p>
              <h2
                data-testid="account-fullname"
                className="mt-2 text-xl font-bold text-black dark:text-white"
              >
                {user.name} {user.surname}
              </h2>
              <p
                data-testid="account-email"
                className="mt-1 text-sm text-(--color-table-text)"
              >
                {user.email}
              </p>
            </div>

            <div className="mt-6 flex-1">
              <InfoRow
                label={t('account.labels.id')}
                value={String(user.id)}
                emptyValue={t('account.common.emptyValue')}
              />
              <InfoRow
                label={t('account.labels.firstName')}
                value={user.name}
                emptyValue={t('account.common.emptyValue')}
              />
              <InfoRow
                label={t('account.labels.lastName')}
                value={user.surname}
                emptyValue={t('account.common.emptyValue')}
              />
              <InfoRow
                label={t('account.labels.username')}
                value={user.username}
                emptyValue={t('account.common.emptyValue')}
              />
              <InfoRow
                label={t('account.labels.email')}
                value={user.email}
                emptyValue={t('account.common.emptyValue')}
              />
              <InfoRow
                label={t('account.labels.password')}
                valueSlot={
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    className="shadow-none"
                    data-testid="account-open-change-password"
                    onClick={() => setPasswordModalOpen(true)}
                  >
                    {t('account.password.changeButton')}
                  </Button>
                }
              />
            </div>
          </section>

          <section className="rounded-2xl border border-(--color-table-border) bg-white p-6 dark:bg-(--color-surface)">
            <p className="text-xs font-bold tracking-[0.18em] text-(--color-table-text) uppercase">
              {t('account.sections.workDetails')}
            </p>
            <h2 className="mt-2 text-xl font-bold text-black dark:text-white">
              {t('account.sections.accountDetails')}
            </h2>

            <div className="mt-6">
              <BadgeRow
                label={t('account.labels.role')}
                value={user.role}
                badgeClassName={getRoleBadgeClass(user.role)}
                testId="account-role"
              />

              <BadgeRow
                label={t('account.labels.status')}
                value={user.status}
                badgeClassName={getStatusBadgeClass(user.status)}
                testId="account-status"
              />

              <InfoRow
                label={t('account.labels.department')}
                value={String(user.departmentId)}
                emptyValue={t('account.common.emptyValue')}
              />
              <InfoRow
                label={t('account.labels.managerEmail')}
                value={user.managerEmail}
                emptyValue={t('account.common.emptyValue')}
              />
              <InfoRow
                label={t('account.labels.notes')}
                value={user.notes ?? t('account.common.emptyValue')}
                emptyValue={t('account.common.emptyValue')}
              />
            </div>
          </section>
        </div>
      )}

      {user && (
        <ChangePasswordModal
          user={user}
          isOpen={passwordModalOpen}
          onClose={() => setPasswordModalOpen(false)}
        />
      )}
    </LayoutColumn>
  );
}
