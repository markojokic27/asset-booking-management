import * as React from 'react';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';
import type { AssetDto } from '../types';
import { getAssetReport } from '../api/assetApi';

type GeneralReportResponseDTO = {
  totalBookingsCount: number;
  totalActiveBookingCount: number;
  totalCompletedBookingCount: number;
  totalCancelledBookingCount: number;
  totalPendingBookingCount: number;
  totalApprovedBookingCount: number;
  totalRejectedBookingCount: number;
};

type AssetReportModalProps = {
  isOpen: boolean;
  onClose: () => void;
  asset: Pick<AssetDto, 'id' | 'name'> | null;
};

export const AssetReportModal: React.FC<AssetReportModalProps> = ({ isOpen, onClose, asset }) => {
  const { t } = useTranslation();
  const [report, setReport] = useState<GeneralReportResponseDTO | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isOpen || !asset) return;

    const fetchReport = async () => {
      try {
        setLoading(true);
        setError('');
        const data = await getAssetReport(asset.id);
        setReport(data);
      } catch {
        setError(t('assets.errors.loadReport'));
      } finally {
        setLoading(false);
      }
    };

    void fetchReport();
  }, [isOpen, asset, t]);

  if (!isOpen || !asset) return null;

  const statItems =
    report &&
    [
      {
        key: 'total',
        label: t('assets.modals.report.stats.total'),
        value: report.totalBookingsCount,
      },
      {
        key: 'active',
        label: t('assets.modals.report.stats.active'),
        value: report.totalActiveBookingCount,
      },
      {
        key: 'completed',
        label: t('assets.modals.report.stats.completed'),
        value: report.totalCompletedBookingCount,
      },
      {
        key: 'cancelled',
        label: t('assets.modals.report.stats.cancelled'),
        value: report.totalCancelledBookingCount,
      },
      {
        key: 'pending',
        label: t('assets.modals.report.stats.pending'),
        value: report.totalPendingBookingCount,
      },
      {
        key: 'approved',
        label: t('assets.modals.report.stats.approved'),
        value: report.totalApprovedBookingCount,
      },
      {
        key: 'rejected',
        label: t('assets.modals.report.stats.rejected'),
        value: report.totalRejectedBookingCount,
        fullWidth: true as const,
      },
    ];

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('assets.table.ariaReport')}
      headerRight={
        <IconButton onClick={onClose} aria-label={t('assets.modals.close')}>
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <div>
          <p className="text-sm text-(--color-modal-label)">
            {t('assets.label')}
          </p>
          <p className="font-medium text-(--color-text)">{asset.name}</p>
        </div>

        {loading && (
          <p className="text-sm text-(--color-modal-label)">
            {t('assets.modals.report.loading')}
          </p>
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