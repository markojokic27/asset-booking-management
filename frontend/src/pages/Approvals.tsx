// external imports
import { useCallback } from 'react';
import { Navigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

// components
import { LayoutColumn } from '../components/layout/Layout';
import { PendingApprovalsTable } from '../features/booking/components/PendingApprovalsTable';

// hooks
import { useBookingApproval } from '../features/booking/hooks/useBookingApproval';
import { usePendingBookings } from '../features/booking/hooks/usePendingBookings';
import { useCurrentUser } from '../features/user/hooks/useCurrentUser';
import { isManager } from '../features/user/utilis/users';

// Approvals page
export default function Approvals() {
  const { t } = useTranslation();

  const { user, isLoading } = useCurrentUser();
  const canFetch = !isLoading && user != null && isManager(user);
  const { bookings, loading, error, refetch } = usePendingBookings(user, canFetch);

  const handleApprovalSuccess = useCallback(async () => {
    await refetch();
  }, [refetch]);

  const { approve, reject, processingId, actionError } =
    useBookingApproval(handleApprovalSuccess);

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
          <h1 className="text-3xl font-black tracking-widest text-black dark:text-white">
            {t('approvals.title')}
          </h1>
        </div>

        <div className="h-px w-full bg-(--color-table-border)" />

        <PendingApprovalsTable
          bookings={bookings}
          isLoading={loading || isLoading}
          error={error || null}
          onApprove={(bookingId) => void approve(bookingId)}
          onReject={(bookingId) => void reject(bookingId)}
          processingId={processingId}
          actionError={actionError || null}
        />
      </div>
    </LayoutColumn>
  );
}
