// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';

// Assets
import { FloorMinus1 } from '../../../assets/Floor-1';
import { FloorMinus2 } from '../../../assets/Floor-2';

// Components
import { Button } from '../../../components/ui/Button';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import { DateInput } from './DateInput';
import { SpotPopover } from './SpotPopover';

// Types
import type { BookingWithRelations, Filters } from '../types';
import type { AssetDto } from '../../asset/types';
import type { SpotClickInfo } from './SpotPopover';

type FloorLevel = '-1' | '-2';

interface Props {
  bookings: BookingWithRelations[];
  assets: AssetDto[];
  filters?: Filters;
  refetchBookings: () => Promise<unknown>;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
}

function getTakenSpots(
  bookings: BookingWithRelations[],
  filters?: Filters
): number[] {
  const referenceDate = filters?.fromDate
    ? new Date(filters.fromDate)
    : new Date();
  const refYear = referenceDate.getFullYear();
  const refMonth = referenceDate.getMonth();
  const refDay = referenceDate.getDate();
  const refStart = new Date(refYear, refMonth, refDay, 0, 0, 0);
  const refEnd = new Date(refYear, refMonth, refDay, 23, 59, 59);

  return bookings
    .filter((b) => {
      if (b.status !== 'APPROVED') return false;
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
  const [selectedSpot, setSelectedSpot] = React.useState<SpotClickInfo | null>(
    null
  );

  const takenSpots = React.useMemo(
    () => getTakenSpots(bookings, filters),
    [bookings, filters]
  );
  const spotAssetMap = React.useMemo(() => buildSpotAssetMap(assets), [assets]);
  const dateLabel = formatDate(filters);

  const handleSpotClick = (spotNumber: number) => {
    setSelectedSpot({
      spotNumber,
      assetId: spotAssetMap.get(spotNumber) ?? null,
    });
  };

  const openModal = () => setIsOpen(true);
  const closeModal = () => {
    setIsOpen(false);
    setSelectedSpot(null);
  };

  const handleDateChange = (value: string) => {
    setFilters((prev) => ({ ...prev, fromDate: value }));
  };

  const handleClearDate = () => {
    setFilters((prev) => ({ ...prev, fromDate: '', toDate: '' }));
  };

  return (
    <>
      <Button
        data-testid="parking-map-button"
        variant="outline"
        onClick={openModal}
      >
        {t('bookings.viewParkingMap')}
      </Button>

      <Modal
        isOpen={isOpen}
        onClose={closeModal}
        title={
          <h2 className="text-3xl font-bold tracking-wide text-gray-900">
            {t('bookings.parkingMap.title')}
          </h2>
        }
        size="lg"
        className="h-[95vh] max-h-[95vh]"
        headerRight={
          <IconButton
            data-testid="parking-close-button"
            onClick={closeModal}
            aria-label={t('bookings.parkingMap.closeAria')}
          >
            <CloseIcon className="pointer-events-none" />
          </IconButton>
        }
      >
        <div className="flex h-full min-h-0 w-full flex-col items-center justify-center overflow-auto">
          <div className="mb-6 flex w-full flex-wrap justify-between gap-4">
            <div className="flex gap-1 rounded-lg border border-gray-200 bg-gray-100 p-1">
              {(['-1', '-2'] as FloorLevel[]).map((level) => (
                <Button
                  key={level}
                  data-testid={`level-button-${level}`}
                  onClick={() => setActiveFloor(level)}
                  variant="outline"
                  className={[
                    'rounded-md border-gray-100 px-4 py-1.5 text-sm font-semibold text-gray-900 transition-colors hover:border-gray-100 hover:text-gray-900 active:scale-100',
                    activeFloor === level
                      ? 'bg-white shadow-sm'
                      : 'border-gray-100 bg-gray-100 text-gray-500 hover:text-gray-700',
                  ].join(' ')}
                >
                  {t('bookings.parkingMap.levelTab', { level })}
                </Button>
              ))}
            </div>
            <div className="flex items-center gap-2">
              <DateInput
                id="parking-map-date"
                label=""
                placeholder={t('bookings.parkingMap.today', {
                  date: dateLabel,
                })}
                value={filters?.fromDate ?? ''}
                onChange={handleDateChange}
                className="h-11 w-48"
              />

              <Button
                onClick={handleClearDate}
                variant="outline"
                size="sm"
                className="h-11"
              >
                {t('bookings.parkingMap.clearDate')}
              </Button>
            </div>
          </div>
          {activeFloor === '-1' ? (
            <FloorMinus1
              takenSpots={takenSpots}
              onSpotClick={handleSpotClick}
            />
          ) : (
            <FloorMinus2
              takenSpots={takenSpots}
              onSpotClick={handleSpotClick}
            />
          )}
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
      </Modal>
    </>
  );
};

export default ParkingMap;
