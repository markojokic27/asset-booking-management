// external imports
import { Navigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

// components
import { LayoutColumn } from '../components/layout/Layout';

// hooks
import { useCurrentUser } from '../features/user/hooks/useCurrentUser';
import { isManager } from '../features/user/utilis/users';

// Approvals page
export default function Approvals() {
  // translation function
  const { t } = useTranslation();

  // current user
  const { user, isLoading } = useCurrentUser();

  // if the user is not a manager, redirect to the bookings page
  if (!isLoading && !isManager(user)) {
    return <Navigate to="/bookings" replace />;
  }

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex min-h-screen flex-col pt-35 pb-10"
    >
      <div className="flex w-full flex-col gap-4">
        <div className="flex flex-col gap-2">
          {/* title for the approvals page */}
          <h1 className="text-3xl font-black tracking-widest text-black dark:text-white">
            {t('approvals.title')}
          </h1>
        </div>
        {/* divider for the approvals page */}
        <div className="h-px w-full bg-(--color-table-border)" />
      </div>
    </LayoutColumn>
  );
}
