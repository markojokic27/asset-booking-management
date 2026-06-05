// external imports
import { useTranslation } from 'react-i18next';

// components
import { LayoutColumn } from '../components/layout/Layout';
import { MyBookingsTable } from '../features/booking/components/MyBookingsTable';

// hooks
import { useMyBookings } from '../features/booking/hooks/useMyBookings';
import { useCurrentUser } from '../features/user/hooks/useCurrentUser';

// utils
import { isAdmin } from '../features/user/utilis/users';

export default function MyBookings() {
  const { t } = useTranslation();
  const { user, isLoading: isUserLoading } = useCurrentUser();
  const { bookings, loading, error } = useMyBookings(
    user,
    !isUserLoading && user != null
  );

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex min-h-screen flex-col pt-35 pb-10"
    >
      <div className="flex w-full flex-col gap-4">
        <div className="flex flex-col gap-2">
          {/* title for the my or all bookings page */}
          <h1 className="text-3xl font-black tracking-widest text-black dark:text-white">
            {isAdmin(user) ? t('myBookings.titleAdmin') : t('myBookings.title')}
          </h1>
        </div>
        {/* divider for the my bookings page */}
        <div className="h-px w-full bg-(--color-table-border)" />

        {/* my bookings table */}
        <MyBookingsTable
          bookings={bookings}
          isLoading={loading || isUserLoading}
          error={error}
        />
      </div>
    </LayoutColumn>
  );
}
