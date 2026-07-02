import * as React from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '../../../components/ui/Button';
import { FloorMinus1 } from '../../../assets/Floor-1';
import { FloorMinus2 } from '../../../assets/Floor-2';
import { useCreateBooking } from '../hooks/useCreateBooking';
import type { BookingWithRelations, Filters } from '../types';
import type { AssetDto } from '../../asset/types';

type FloorLevel = '-1' | '-2';

export interface SpotClickInfo {
  spotNumber: number;
  assetId: number | null;
}

interface Props {
  bookings: BookingWithRelations[];
  assets: AssetDto[];
  filters?: Filters;
  refetchBookings: () => Promise<unknown>;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
}

function getTakenSpots(bookings: BookingWithRelations[], filters?: Filters): number[] {
  const referenceDate = filters?.fromDate ? new Date(filters.fromDate) : new Date();
  const refYear = referenceDate.getFullYear();
  const refMonth = referenceDate.getMonth();
  const refDay = referenceDate.getDate();
  const refStart = new Date(refYear, refMonth, refDay, 0, 0, 0);
  const refEnd = new Date(refYear, refMonth, refDay, 23, 59, 59);

  return bookings
    .filter((b) => {
      if (b.status !== 'APPROVED' && b.status !== 'ACTIVE') return false;
      const start = new Date(b.bookingStart);
      const end = new Date(b.bookingEnd);
      return start <= refEnd && end >= refStart;
    })
    .flatMap((b) => {
      const match = b.asset.name.match(/Parking Spot (\d+)/i);
      if (!match) return [];
      return [Number.parseInt(match[1], 10)];
    });
}

function buildSpotAssetMap(assets: AssetDto[]): Map<number, number> {
  const map = new Map<number, number>();
  for (const asset of assets) {
    const match = asset.name.match(/Parking Spot (\d+)/i);
    if (match) map.set(Number.parseInt(match[1], 10), asset.id);
  }
  return map;
}

function formatDate(filters?: Filters): string {
  const date = filters?.fromDate ? new Date(filters.fromDate) : new Date();
  return date.toLocaleDateString(undefined, {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  });
}

interface PopoverProps {
  info: SpotClickInfo;
  isTaken: boolean;
  filters?: Filters;
  refetchBookings: () => Promise<unknown>;
  onClose: () => void;
}

const SpotPopover: React.FC<PopoverProps> = ({
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

  const noDateSelected = !filters?.fromDate;

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center p-4">
      <button
        type="button"
        data-testid="spot-popover-backdrop"
        className="fixed inset-0 cursor-default bg-black/30"
        aria-label={t('bookings.parkingMap.closeAria')}
        onClick={onClose}
      />
      <div className="relative z-10 w-72 rounded-xl bg-white p-6 shadow-2xl">

        <div className="flex items-start justify-between">
          <div>
            <p className="text-xs font-semibold uppercase tracking-widest text-gray-400">
              {t('bookings.parkingMap.spotNumber')}
            </p>
            <p className="mt-1 text-3xl font-black text-gray-900">
              {info.spotNumber}
            </p>
          </div>
          <button data-testid="spot-popover-close-button"
            onClick={onClose}
            className="flex h-7 w-7 items-center justify-center rounded-lg text-gray-400 hover:bg-gray-100 hover:text-gray-600"
            aria-label={t('bookings.parkingMap.closeAria')}
          >
            <svg width="14" height="14" viewBox="0 0 16 16" fill="none">
              <path d="M2 2L14 14M14 2L2 14" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
            </svg>
          </button>
        </div>

        <div className="mt-3">
          <span data-testid="parking-spot-status" className={[
            'inline-flex items-center gap-1.5 rounded-full px-2.5 py-1 text-xs font-semibold',
            isTaken ? 'bg-orange-100 text-orange-700' : 'bg-blue-100 text-blue-700',
          ].join(' ')}>
            <span className={[
              'h-1.5 w-1.5 rounded-full',
              isTaken ? 'bg-orange-500' : 'bg-blue-500',
            ].join(' ')} />
            {isTaken ? t('bookings.parkingMap.taken') : t('bookings.parkingMap.available')}
          </span>
        </div>

        {!isTaken && !noDateSelected && (
          <input
            placeholder={t('bookings.parkingMap.notesPlaceholder')}
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            className="mt-4 w-full rounded-lg border border-gray-200 px-3 py-2 text-sm outline-none focus:border-blue-400"
          />
        )}

        <div className="mt-4">
          <Button
            data-testid="spot-book-button"
            className="w-full"
            disabled={isTaken || info.assetId === null || isCreating || noDateSelected}
            onClick={handleBook}
          >
            {isCreating ? t('bookings.parkingMap.booking') : t('bookings.table.book')}
          </Button>

          {noDateSelected && (
            <p className="mt-2 text-center text-xs text-gray-400">
              {t('bookings.parkingMap.selectDateFirst')}
            </p>
          )}
        </div>
      </div>
    </div>
  );
};

export const ParkingMap: React.FC<Props> = ({
  bookings,
  assets,
  filters,
  refetchBookings,
  setFilters,
}) => {
  const { t } = useTranslation();
  const [isOpen, setIsOpen] = React.useState(false);
  const [activeFloor, setActiveFloor] = React.useState<FloorLevel>('-1');
  const [selectedSpot, setSelectedSpot] = React.useState<SpotClickInfo | null>(null);

  const takenSpots = React.useMemo(() => getTakenSpots(bookings, filters), [bookings, filters]);
  const spotAssetMap = React.useMemo(() => buildSpotAssetMap(assets), [assets]);
  const dateLabel = formatDate(filters);

  const handleSpotClick = (spotNumber: number) => {
    setSelectedSpot({
      spotNumber,
      assetId: spotAssetMap.get(spotNumber) ?? null,
    });
  };

  const openModal = () => setIsOpen(true);
  const closeModal = () => { setIsOpen(false); setSelectedSpot(null); };

  const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const date = e.target.value;
    setFilters((prev) => ({
      ...prev,
      fromDate: date,
      toDate: date,
    }));
  };

  const handleClearDate = () => {
    setFilters((prev) => ({ ...prev, fromDate: '', toDate: '' }));
  };

  React.useEffect(() => {
    if (!isOpen) return;
    const handler = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        if (selectedSpot) setSelectedSpot(null);
        else closeModal();
      }
    };
    window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [isOpen, selectedSpot]);

  React.useEffect(() => {
    document.body.style.overflow = isOpen ? 'hidden' : '';
    return () => { document.body.style.overflow = ''; };
  }, [isOpen]);

  return (
    <>
      <Button data-testid="parking-map-button" variant="outline" onClick={openModal}>
        {t('bookings.viewParkingMap')}
      </Button>

      {isOpen && (
        <div
          data-testid="spot-popover"
          className="fixed inset-0 z-50 flex items-center justify-center p-4"
          role="dialog"
          aria-modal="true"
          aria-label={t('bookings.parkingMap.title')}
        >
          <button
            type="button"
            data-testid="parking-map-backdrop"
            className="fixed inset-0 cursor-default bg-black/50"
            aria-label={t('bookings.parkingMap.closeAria')}
            onClick={closeModal}
          />
          <div className="relative z-10 flex max-h-[90vh] w-full max-w-3xl flex-col overflow-hidden rounded-xl bg-white shadow-2xl">

            <div className="flex items-center justify-between border-b border-gray-200 px-6 py-4">
              <div>
                <h2 className="text-lg font-bold tracking-wide text-gray-900">
                  {t('bookings.parkingMap.title')}
                </h2>
                <div className="mt-1 flex items-center gap-2">
                  <input
                    type="date"
                    value={filters?.fromDate ?? ''}
                    onChange={handleDateChange}
                    className="rounded-lg border border-gray-200 px-2 py-1 text-xs text-gray-600 outline-none focus:border-blue-400"
                  />
                  {filters?.fromDate && (
                    <button
                      onClick={handleClearDate}
                      className="text-xs text-gray-400 hover:text-gray-600"
                    >
                      {t('bookings.parkingMap.clearDate')}
                    </button>
                  )}
                  {!filters?.fromDate && (
                    <span className="text-xs text-gray-400">
                      {t('bookings.parkingMap.today', { date: dateLabel })}
                    </span>
                  )}
                </div>
              </div>

              <div className="flex gap-1 rounded-lg border border-gray-200 bg-gray-100 p-1">
                {(['-1', '-2'] as FloorLevel[]).map((level) => (
                  <button data-testid={`level-button-${level}`}
                    key={level}
                    onClick={() => setActiveFloor(level)}
                    className={[
                      'rounded-md px-4 py-1.5 text-sm font-semibold transition-colors',
                      activeFloor === level
                        ? 'bg-white text-gray-900 shadow-sm'
                        : 'text-gray-500 hover:text-gray-700',
                    ].join(' ')}
                  >
                    {t('bookings.parkingMap.levelTab', { level })}
                  </button>
                ))}
              </div>

              <button data-testid="parking-close-button"
                onClick={closeModal}
                className="flex h-8 w-8 items-center justify-center rounded-lg text-gray-400 transition-colors hover:bg-gray-100 hover:text-gray-600"
                aria-label={t('bookings.parkingMap.closeAria')}
              >
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                  <path d="M2 2L14 14M14 2L2 14" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
                </svg>
              </button>
            </div>

            <div className="overflow-y-auto p-4">
              {activeFloor === '-1'
                ? <FloorMinus1 takenSpots={takenSpots} onSpotClick={handleSpotClick} />
                : <FloorMinus2 takenSpots={takenSpots} onSpotClick={handleSpotClick} />
              }
            </div>
          </div>

          {selectedSpot && (
            <SpotPopover
              info={selectedSpot}
              isTaken={takenSpots.includes(selectedSpot.spotNumber)}
              filters={filters}
              refetchBookings={refetchBookings}
              onClose={() => setSelectedSpot(null)}
            />
          )}
        </div>
      )}
    </>
  );
};

export default ParkingMap;