// external imports
import { useCallback, useEffect, useMemo, useState } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

// components
import { LayoutColumn } from '../components/layout/Layout';
import { SearchInput } from '../components/ui/SearchBar';
import { PendingApprovalsTable } from '../features/booking/components/PendingApprovalsTable';

// hooks
import { useBookingApproval } from '../features/booking/hooks/useBookingApproval';
import { usePendingBookings } from '../features/booking/hooks/usePendingBookings';
import { useAuth } from '../features/auth/context/AuthContext';
import { filterPendingBookingsBySearch } from '../features/booking/utils/approvalFilter';
import { isManager } from '../features/user/utils/users';

// Approvals page
export default function Approvals() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { bookingId } = useParams();

  const { user, isLoading } = useAuth();
  const canFetch = !isLoading && user != null && isManager(user);
  const { bookings, loading, error, refetch } = usePendingBookings(user, canFetch);
  const [search, setSearch] = useState('');

  // filtered bookings for the approvals page
  const filteredBookings = useMemo(
    () => filterPendingBookingsBySearch(bookings, search),
    [bookings, search]
  );

  const selectedBooking = useMemo(
    () =>
      bookingId
        ? bookings.find((booking) => String(booking.id) === bookingId) ?? null
        : null,
    [bookings, bookingId]
  );

  useEffect(() => {
    if (bookingId && !loading && !isLoading && !selectedBooking) {
      navigate('/approvals', { replace: true });
    }
  }, [bookingId, loading, isLoading, selectedBooking, navigate]);

  const handleApprovalSuccess = useCallback(async () => {
    await refetch();
    if (bookingId) {
      navigate('/approvals', { replace: true });
    }
  }, [refetch, bookingId, navigate]);

  const { approve, reject, processingId, actionError } =
    useBookingApproval(handleApprovalSuccess);

  const handleOpenBooking = useCallback(
    (id: number | string) => {
      navigate(`/approvals/${id}`);
    },
    [navigate]
  );

  const handleCloseBooking = useCallback(() => {
    navigate('/approvals');
  }, [navigate]);

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

        {/* search input for the approvals page */}
        <div className="flex w-full items-center justify-end">
          <SearchInput
            value={search}
            onChange={setSearch}
            placeholder={t('approvals.search.placeholder')}
            className="w-70"
          />
        </div>

        <PendingApprovalsTable
          bookings={filteredBookings}
          isLoading={loading || isLoading}
          error={error || null}
          selectedBooking={selectedBooking}
          onOpenBooking={handleOpenBooking}
          onCloseBooking={handleCloseBooking}
          onApprove={(id) => void approve(id)}
          onReject={(id) => void reject(id)}
          processingId={processingId}
          actionError={actionError || null}
        />
      </div>
    </LayoutColumn>
  );
}
