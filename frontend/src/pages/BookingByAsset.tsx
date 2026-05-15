// External packages
import * as React from 'react';
import { useParams } from 'react-router-dom';
import * as React from 'react';

// Hooks
import { useBookingsByAsset } from '../features/booking/hooks/useBookingByAsset';

// Utils
import { mapBookingsToCalendarEvents } from '../features/booking/utilis/bookingLogic';
import { useBookingFilters } from '../features/booking/hooks/useBookingFilters';
import { useBookingAvailability } from '../features/booking/hooks/useBookingAvailability';
import { useCreateBooking } from '../features/booking/hooks/useCreateBooking';


// Components
import { LayoutColumn } from '../components/layout/Layout';
import { FiltersBar } from '../features/booking/components/FilterBar';
import { AvailabilityCalendar } from '../features/booking/components/AvailabilityCalendar';
import { Button } from '../components/ui/Button';
import { FiltersBar } from '../features/booking/components/FilterBar';
import { AvailabilityCalendar } from '../features/booking/components/AvailabilityCalendar';
import { Button } from '../components/ui/Button';

// Types
import type { AssetDto } from '../features/asset/types';
import type { Filters } from '../features/booking/types';

import { getAssetById } from '../features/asset/api/assetApi';

const defaultFilters: Filters = {
  search: '',
  fromDate: '',
  toDate: '',
  fromHour: '',
  toHour: '',
};

export default function BookingsByAsset() {
  const { assetId } = useParams();
  const [filters, setFilters] = React.useState<Filters>(defaultFilters);

  const { loading, error } = useBookingsByAsset(assetId!);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Greška</div>;

  const asset = getAssetById(assetId!) as unknown as AssetDto;

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div>
        <h2 className="mb-4 text-xl font-semibold">Book {asset.name}</h2>

        <div className="mb-5 text-sm">
          <p className="font-semibold">{asset.name}</p>
          <p>Model: {asset.name ?? '-'}</p>
          <p>Location: {asset.location ?? '-'}</p>
        </div>

        <div className="">
          <div className="mb-6 flex w-full items-end justify-between">
            <FiltersBar
              variant={'DAY'}
              filters={filters}
              setFilters={setFilters}
              showSearch={false}
              className="grid-cols-1 sm:grid-cols-2 lg:max-w-[80%] lg:grid-cols-2"
            />
            <Button
              variant="solid"
              className="h-fit"
              size="md"
              onClick={() => {
                console.log('BOOK', {
                  asset,
                  from: filters.fromDate,
                  to: filters.toDate,
                });
              }}
            >
              Book
            </Button>
          </div>

          {/* Implement checking asset status, if available user can book asset, else button book is disabled 
          <div className="flex items-start gap-3 text-sm">
            <span className="bg---color-status-active-bg flex h-5 w-5 items-center justify-center text-(--color-status-active-text)">
              ✓
            </span>

            <p>
              {/* If a date is not selected, the message is not displayed. Check availability - if available render AVAILABLE else UNAVAILABLE  }
              {asset.name} is{' '}
              <span className="rounded bg-(--color-status-active-bg) px-2 py-0.5 text-(--color-status-active-text)">
                available
              </span>{' '}
              from {selectedFrom} to {selectedTo}
            </p>
          </div> */}
        </div>

        {/* Dohvatit bookinge odredenog asseta i posalt ih u events kako bi se prikazali u kalendaru*/}
        <AvailabilityCalendar
          events={[
            {
              id: '1',
              title: `${asset.name} booked`,
              start: '2026-04-29T10:00:00',
              end: '2026-04-29T12:00:00',
            },
          ]}
        />
      </div>
    </LayoutColumn>
  );
}
