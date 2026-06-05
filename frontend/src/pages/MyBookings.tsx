// external imports
import { useTranslation } from 'react-i18next';

// components
import { LayoutColumn } from '../components/layout/Layout';

// hooks
import { useCurrentUser } from '../features/user/hooks/useCurrentUser';

// utils
import { isAdmin } from '../features/user/utilis/users';

export default function MyBookings() {
  const { t } = useTranslation();
  const { user } = useCurrentUser();

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex min-h-screen flex-col pt-35 pb-10"
    >
      <div className="flex w-full flex-col gap-4">
        <div className="flex flex-col gap-2">
          <h1 className="text-3xl font-black tracking-widest text-black dark:text-white">
            {isAdmin(user) ? t('myBookings.titleAdmin') : t('myBookings.title')}
          </h1>
        </div>
        <div className="h-px w-full bg-(--color-table-border)" />
      </div>
    </LayoutColumn>
  );
}
