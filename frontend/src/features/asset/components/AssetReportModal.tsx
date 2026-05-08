import * as React from 'react';
import { useEffect, useState } from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';
import type { AssetDto } from '../types';
import { getAssetReport } from '../api/assetApi';

type GeneralReportResponseDTO = {
  totalBookingsCount: number;
  totalActiveBookingCount: number;
  totalCompletedBookingCount: number;
  totalCanceledBookingCount: number;
  totalPendingBookingCount: number;
  totalApprovedBookingCount: number;
  totalRejectedBookingCount: number;
};

type AssetReportModalProps = {
  isOpen: boolean;
  onClose: () => void;
  asset: Pick<AssetDto, 'id' | 'name'> | null;
};

const statItems = (report: GeneralReportResponseDTO) => [
  { label: 'Ukupno rezervacija', value: report.totalBookingsCount },
  { label: 'Aktivne',            value: report.totalActiveBookingCount },
  { label: 'Završene',           value: report.totalCompletedBookingCount },
  { label: 'Otkazane',           value: report.totalCanceledBookingCount },
  { label: 'Na čekanju',         value: report.totalPendingBookingCount },
  { label: 'Odobrene',           value: report.totalApprovedBookingCount },
  { label: 'Odbijene',           value: report.totalRejectedBookingCount, fullWidth: true },
];

export const AssetReportModal: React.FC<AssetReportModalProps> = ({ isOpen, onClose, asset }) => {
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
        setError('Greška pri učitavanju izvještaja.');
      } finally {
        setLoading(false);
      }
    };

    void fetchReport();
  }, [isOpen, asset]);

  if (!isOpen || !asset) return null;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel="Izvještaj sredstva"
      headerRight={
        <IconButton onClick={onClose} aria-label="Zatvori">
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <div>
          <p className="text-sm text-(--color-modal-label)">Asset</p>
          <p className="font-medium text-(--color-text)">{asset.name}</p>
        </div>

        {loading && (
          <p className="text-sm text-(--color-modal-label)">Učitavanje...</p>
        )}

        {error && (
          <p className="text-sm text-red-500">{error}</p>
        )}

        {report && !loading && (
          <div className="grid grid-cols-2 gap-4">
            {statItems(report).map((item) => (
              <div
                key={item.label}
                className={`rounded-xl border border-(--color-table-border) bg-(--color-table-surface) p-4${item.fullWidth ? ' col-span-2' : ''}`}
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