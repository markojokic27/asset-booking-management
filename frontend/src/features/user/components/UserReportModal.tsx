import * as React from 'react';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';
import type { UserDto } from '../types';
import { getUserReport } from '../api/users';

type GeneralReportResponseDTO = {
  totalBookingsCount: number;
  totalActiveBookingCount: number;
  totalCompletedBookingCount: number;
  totalCancelledBookingCount: number;
  totalPendingBookingCount: number;
  totalApprovedBookingCount: number;
  totalRejectedBookingCount: number;
};

type UserReportModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: Pick<UserDto, 'id' | 'name' | 'surname'> | null;
};

export const UserReportModal: React.FC<UserReportModalProps> = ({ isOpen, onClose, user }) => {
  const { t } = useTranslation();
  const [report, setReport] = useState<GeneralReportResponseDTO | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isOpen || !user) return;

    const fetchReport = async () => {
      try {
        setLoading(true);
        setError('');
        const data = await getUserReport(user.id);
        setReport(data);
      } catch {
        setError(t('users.errors.loadReport'));
      } finally {
        setLoading(false);
      }
    };

    void fetchReport();
  }, [isOpen, user, t]);

  if (!isOpen || !user) return null;

  const statItems =
    report &&
    [
      {
        key: 'total',
        label: t('users.modals.report.stats.total'),
        value: report.totalBookingsCount,
      },
      {
        key: 'active',
        label: t('users.modals.report.stats.active'),
        value: report.totalActiveBookingCount,
      },
      {
        key: 'completed',
        label: t('users.modals.report.stats.completed'),
        value: report.totalCompletedBookingCount,
      },
      {
        key: 'cancelled',
        label: t('users.modals.report.stats.cancelled'),
        value: report.totalCancelledBookingCount,
      },
      {
        key: 'pending',
        label: t('users.modals.report.stats.pending'),
        value: report.totalPendingBookingCount,
      },
      {
        key: 'approved',
        label: t('users.modals.report.stats.approved'),
        value: report.totalApprovedBookingCount,
      },
      {
        key: 'rejected',
        label: t('users.modals.report.stats.rejected'),
        value: report.totalRejectedBookingCount,
        fullWidth: true as const,
      },
    ];

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('users.table.rowActions.reportAria')}
      headerRight={
        <IconButton onClick={onClose} aria-label={t('users.modals.common.closeAria')}>
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <div>
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.report.userLabel')}</p>
          <p className="font-medium text-(--color-text)">
            {user.name} {user.surname}
          </p>
        </div>

        {loading && (
          <p className="text-sm text-(--color-modal-label)">{t('users.modals.report.loading')}</p>
        )}

        {error && (
          <p className="text-sm text-red-500">{error}</p>
        )}

        {statItems && !loading && (
          <div className="grid grid-cols-2 gap-4">
            {statItems.map((item) => (
              <div
                key={item.key}
                className={`rounded-xl border border-(--color-table-border) bg-(--color-table-surface) p-4${'fullWidth' in item && item.fullWidth ? ' col-span-2' : ''}`}
              >
                <p className="text-sm text-(--color-modal-label)">{item.label}</p>
                <p className="text-2xl font-bold text-(--color-text)">{item.value}</p>
              </div>
            ))}
          </div>
        )}
      </div>
    </Modal>
  );
};
