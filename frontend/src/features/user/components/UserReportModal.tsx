import * as React from 'react';
import { useEffect, useState } from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';
import type { UserDto } from '../types';
import { getUserReport } from '../api/users';

type GeneralReportResponseDTO = {
  totalBookingsCount: number;
  totalActiveBookingCount: number;
  totalCompletedBookingCount: number;
  totalCanceledBookingCount: number;
  totalPendingBookingCount: number;
  totalApprovedBookingCount: number;
  totalRejectedBookingCount: number;
};

type UserReportModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: Pick<UserDto, 'id' | 'name' | 'surname'> | null;
};

const statItems = (report: GeneralReportResponseDTO) => [
  { label: 'Ukupno rezervacija', value: report.totalBookingsCount },
  { label: 'Aktivne', value: report.totalActiveBookingCount },
  { label: 'Završene', value: report.totalCompletedBookingCount },
  { label: 'Otkazane', value: report.totalCanceledBookingCount },
  { label: 'Na čekanju', value: report.totalPendingBookingCount },
  { label: 'Odobrene', value: report.totalApprovedBookingCount },
  { label: 'Odbijene', value: report.totalRejectedBookingCount },
];

export const UserReportModal: React.FC<UserReportModalProps> = ({ isOpen, onClose, user }) => {
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
      setError('Greška pri učitavanju izvještaja.');
    } finally {
      setLoading(false);
    }
  };

    void fetchReport();
  }, [isOpen, user]);

  if (!isOpen || !user) return null;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel="Izvještaj korisnika"
      headerRight={
        <IconButton onClick={onClose} aria-label="Zatvori">
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <div>
          <p className="text-sm text-(--color-modal-label)">Korisnik</p>
          <p className="font-medium text-(--color-text)">
            {user.name} {user.surname}
          </p>
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
                className="rounded-xl border border-(--color-table-border) bg-(--color-table-surface) p-4"
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