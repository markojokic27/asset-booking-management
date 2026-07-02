// external imports
import { useTranslation } from 'react-i18next';

// types
import type { BookingStatus } from '../types';

// mapping of status to class name
const statusClassNameConfig: Record<BookingStatus, string> = {
  APPROVED:
    'bg-(--color-status-active-bg) text-(--color-status-active-text)',
  PENDING:
    'bg-(--color-status-damaged-bg) text-(--color-status-damaged-text)',
  REJECTED:
    'bg-(--color-status-inactive-bg) text-(--color-status-inactive-text)',
  CANCELLED:
    'bg-(--color-status-deleted-bg) text-(--color-status-deleted-text)',
  COMPLETED:
    'bg-(--color-status-completed-bg) text-(--color-status-completed-text)',
};

// generate translation key for status
const bookingStatusKey = (status: BookingStatus) =>
  `bookings.status.${status.toLowerCase()}` as const;

// props for the BookingStatusBadge component
type BookingStatusBadgeProps = {
  status: BookingStatus;
};

// BookingStatusBadge component
export function BookingStatusBadge({ status }: BookingStatusBadgeProps) {
  const { t } = useTranslation();

  // get translation label for status
  const label = t(bookingStatusKey(status), { defaultValue: status });
  // get class name for status
  const statusClassName = statusClassNameConfig[status] ?? '';

  return (
    <span
      className={[
        // base class name
        'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
        // dynamic class name for status
        statusClassName,
      ].join(' ')}
    >
      {label}
    </span>
  );
}
