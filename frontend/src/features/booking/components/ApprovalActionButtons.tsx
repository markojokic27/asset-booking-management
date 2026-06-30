// external imports
import type { MouseEvent } from 'react';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CloseIcon from '@mui/icons-material/Close';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';

// components
import { Button } from '../../../components/ui/Button';
import { IconButton } from '../../../components/ui/IconButton';

// props of the component
type Props = {
  bookingId: number;
  onApprove: (bookingId: number) => void;
  onReject: (bookingId: number) => void;
  processingId?: number | null;
  className?: string;
  size?: 'sm' | 'md';
  showLabels?: boolean;
};

// approval action buttons component
export function ApprovalActionButtons({
  bookingId,
  onApprove,
  onReject,
  processingId = null,
  className,
  size = 'md',
  showLabels = false,
}: Readonly<Props>) {
  const { t } = useTranslation();
  const isProcessing = processingId === bookingId;
  const isDisabled = processingId != null;

  const iconSize = size === 'sm' ? 'small' : 'medium';

  // prevent row click / parent handlers when using approve/reject actions
  const handleApprove = (event: MouseEvent) => {
    event.stopPropagation();
    onApprove(bookingId);
  };

  const handleReject = (event: MouseEvent) => {
    event.stopPropagation();
    onReject(bookingId);
  };

  return (
    <div className={twMerge('inline-flex items-center gap-2', className)}>
      {showLabels ? (
        <>
          <Button
            data-testid={`approve-booking-${bookingId}`}
            type="button"
            size={size}
            variant="outline"
            disabled={isDisabled}
            iconLeft={<CheckCircleIcon fontSize={iconSize} />}
            className="border-green-600 text-green-600 hover:border-green-700 hover:bg-green-50 hover:text-green-700 dark:border-green-500 dark:text-green-400 dark:hover:bg-green-950/40 dark:hover:text-green-300"
            onClick={handleApprove}
          >
            {t('approvals.actions.approveLabel')}
          </Button>
          <Button
            data-testid={`reject-booking-${bookingId}`}
            type="button"
            size={size}
            variant="outline"
            disabled={isDisabled}
            iconLeft={<CloseIcon fontSize={iconSize} />}
            className="border-red-600 text-red-600 hover:border-red-700 hover:bg-red-50 hover:text-red-700 dark:border-red-500 dark:text-red-400 dark:hover:bg-red-950/40 dark:hover:text-red-300"
            onClick={handleReject}
          >
            {t('approvals.actions.rejectLabel')}
          </Button>
        </>
      ) : (
        <>
          <IconButton
            data-testid={`approve-booking-${bookingId}`}
            size={size}
            aria-label={t('approvals.actions.approve')}
            disabled={isDisabled}
            className="text-green-600 hover:bg-green-50 hover:text-green-700 dark:text-green-400 dark:hover:bg-green-950/40 dark:hover:text-green-300"
            onClick={handleApprove}
          >
            <CheckCircleIcon fontSize={iconSize} />
          </IconButton>
          <IconButton
            data-testid={`reject-booking-${bookingId}`}
            variant="danger"
            size={size}
            aria-label={t('approvals.actions.reject')}
            disabled={isDisabled}
            onClick={handleReject}
          >
            <CloseIcon fontSize={iconSize} />
          </IconButton>
        </>
      )}
      {isProcessing && (
        <span className="sr-only">{t('approvals.actions.processing')}</span>
      )}
    </div>
  );
}
