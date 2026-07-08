// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';
import { Link } from 'react-router-dom';

// Components
import { Button } from '../../../components/ui/Button';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';

// Types
import type { Filters } from '../types';
import { useCreateBooking } from '../hooks/useCreateBooking';
import i18n from '../../../config/i18n';

export interface SpotClickInfo {
  spotNumber: number;
  assetId: number | null;
}

interface Props {
  info: SpotClickInfo;
  isTaken: boolean;
  filters?: Filters;
  refetchBookings: () => Promise<unknown>;
  onClose: () => void;
}

export const SpotPopover: React.FC<Props> = ({
  info,
  isTaken,
  filters,
  refetchBookings,
  onClose,
}) => {
  const { t } = useTranslation();
  const [notes, setNotes] = React.useState('');

  const parkingFilters: Filters = {
    search: '',
    fromDate: filters?.fromDate ?? '',
    toDate: filters?.fromDate ?? '',
    fromHour: '06:00',
    toHour: '22:00',
    selectedWeekdays: [],
  };

  const { isCreating, handleCreateBooking } = useCreateBooking({
    assetId: info.assetId ?? 0,
    filters: parkingFilters,
    notes,
    setNotes,
    refetch: refetchBookings,
    bookingPeriod: 'DAY',
    availableRecurringDates: [],
    t,
  });

  const handleBook = async () => {
    await handleCreateBooking();
    onClose();
  };

  const formatDate = (date: string) =>
    new Date(date).toLocaleDateString(i18n.language, {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  const date = formatDate(parkingFilters.fromDate);

  return (
    <Modal
      isOpen
      onClose={onClose}
      size="md"
      title={
        <div className="flex items-center gap-4">
          <h2 className="text-xl font-semibold text-(--color-table-text)">
            {t('bookings.parkingMap.spotNumber')} {info.spotNumber}
          </h2>
          <div>
            <span
              data-testid="parking-spot-status"
              className={[
                'inline-flex items-center gap-1.5 rounded-full px-2.5 py-1 text-xs font-semibold',
                isTaken
                  ? 'bg-orange-100 text-orange-700'
                  : 'bg-blue-100 text-blue-700',
              ].join(' ')}
            >
              <span
                className={[
                  'h-1.5 w-1.5 rounded-full',
                  isTaken ? 'bg-orange-500' : 'bg-blue-500',
                ].join(' ')}
              />

              {isTaken
                ? t('bookings.parkingMap.taken')
                : t('bookings.parkingMap.available')}
            </span>
          </div>
        </div>
      }
      className="max-w-2xl"
      headerRight={
        <IconButton
          data-testid="spot-popover-close-button"
          onClick={onClose}
          aria-label={t('bookings.parkingMap.closeAria')}
        >
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
    >
      <p className="mb-4 text-(--color-table-text)">
        {isTaken
          ? t('bookings.confirmation.parkingSpotTaken')
          : t('bookings.confirmation.singleDay', {
              date,
            })}
      </p>
      {!isTaken && (
        <input
          placeholder={t('bookings.parkingMap.notesPlaceholder')}
          value={notes}
          onChange={(e) => setNotes(e.target.value)}
          className="mt-4 w-full rounded-lg border border-gray-200 px-3 py-2 text-sm outline-none focus:border-blue-400"
        />
      )}

      <div className="mt-4">
        <div className="flex gap-4">
          <Link to={`/assets/${info.assetId}/bookings`} className="w-1/2">
            <Button variant="outline" onClick={onClose} className="w-full">
              Calendar
            </Button>
          </Link>

          <Button
            data-testid="spot-book-button"
            className="w-1/2"
            disabled={isTaken || info.assetId === null || isCreating}
            onClick={handleBook}
          >
            {isCreating
              ? t('bookings.parkingMap.booking')
              : t('bookings.table.book')}
          </Button>
        </div>
      </div>
    </Modal>
  );
};
