// External packages
import * as React from 'react';
import { useParams } from 'react-router-dom';

// Hooks
import { useBookingsByAsset } from '../features/booking/hooks/useBookingByAsset';
import { useBookingFilters } from '../features/booking/hooks/useBookingFilters';
import { useBookingAvailability } from '../features/booking/hooks/useBookingAvailability';
import { useCreateBooking } from '../features/booking/hooks/useCreateBooking';

// Utils
import { mapBookingsToCalendarEvents } from '../features/booking/utilis/bookingLogic';

// Components
import { LayoutColumn } from '../components/layout/Layout';
import { FiltersBar } from '../features/booking/components/FilterBar';
import { AvailabilityCalendar } from '../features/booking/components/AvailabilityCalendar';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { BookingDetailsModal } from '../features/booking/components/BookingDetailsModal';

// Types
import type { BookingWithRelations } from '../features/booking/types';

// const STATUS_COLORS: Record<string, string> = {
//   APPROVED: '#22c55e',
//   PENDING: '#f59e0b',
//   REJECTED: '#ef4444',
//   CANCELLED: '#6b7280',
// };

export default function BookingsByAsset() {
  const { assetId } = useParams();

  const { filters, setFilters, handleCalendarDateClick } = useBookingFilters();

  const { bookings, loading, error, refetch } = useBookingsByAsset(assetId!);

  const asset = bookings?.[0]?.asset;

  const [notes, setNotes] = React.useState('');
  const [selectedBooking, setSelectedBooking] =
    React.useState<BookingWithRelations | null>(null);

  const calendarEvents = React.useMemo(
    () => mapBookingsToCalendarEvents(bookings),
    [bookings]
  );

  const isButtonDisabled = useBookingAvailability({
    assetStatus: asset?.status,
    filters,
    bookings,
  });

  const { isCreating, handleCreateBooking } = useCreateBooking({
    assetId: Number(assetId),
    notes,
    setNotes,
    filters,
    refetch,
  });

  if (loading) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35">Loading...</div>
      </LayoutColumn>
    );
  }

  if (error) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35 text-red-500">
          Greška pri dohvaćanju bookinga.
        </div>
      </LayoutColumn>
    );
  }

  if (!asset) {
    return (
      <LayoutColumn span={12} mdSpan={9} mdOffset={3}>
        <div className="pt-35">Asset nema booking history.</div>
      </LayoutColumn>
    );
  }
  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <div className="mb-6 flex items-baseline justify-between">
        <div className="flex items-center gap-6">
          <h1 className="text-3xl font-black text-black dark:text-white">
            {asset.name}
          </h1>

          <span
            className={`rounded px-3 py-1 text-sm font-medium ${
              asset.status === 'ACTIVE'
                ? 'bg-green-100 text-green-700'
                : 'bg-gray-200 text-gray-700'
            }`}
          >
            {asset.status}
          </span>
        </div>

        <p>Location: {asset.location}</p>
      </div>

      <div className="mb-6 h-px w-full bg-(--color-table-border)" />

      <div className="mb-2 flex w-full items-end justify-between gap-4">
        <FiltersBar
          variant={asset.category.bookingPeriod === 'HOUR' ? 'HOUR' : 'DAY'}
          filters={filters}
          setFilters={setFilters}
          showSearch={false}
          className="mt-0 grid-cols-1 sm:grid-cols-2 lg:max-w-[80%] lg:grid-cols-2"
        />

        <Button
          data-testid="book-asset-button"
          variant="solid"
          className="h-fit"
          size="md"
          disabled={isButtonDisabled || isCreating}
          onClick={handleCreateBooking}
        >
          {isCreating ? 'Booking...' : 'Book'}
        </Button>
      </div>

      <p className="mb-1 text-sm font-medium text-(--color-table-text)">
        Notes
      </p>
      <Input
        placeholder="Notes..."
        className="mb-6 w-full border shadow-none"
        value={notes}
        onChange={(e) => setNotes(e.target.value)}
      />

      <AvailabilityCalendar
        events={calendarEvents}
        selectedDate={filters.fromDate}
        onDateClick={handleCalendarDateClick}
        setSelectedBooking={setSelectedBooking}
        variant={asset.category.bookingPeriod === 'HOUR' ? 'HOUR' : 'DAY'}
      />

      <BookingDetailsModal
        booking={selectedBooking}
        onClose={() => setSelectedBooking(null)}
      />
    </LayoutColumn>
  );
}
