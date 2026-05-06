// External packages
import { useParams } from 'react-router-dom';

// Hooks
import { useBookingsByAsset } from '../features/booking/hooks/useBookingByAsset';

// Components
import { LayoutColumn } from '../components/layout/Layout';

// Types
import type { BookingDto } from '../features/booking/types';

export default function BookingsByAsset() {
  const { id } = useParams();
  const { bookings, loading, error } = useBookingsByAsset(id!);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Greška</div>;

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      {' '}
      <h1>Bookings za asset {id}</h1>
      {bookings.map((b: BookingDto) => (
        <div key={b.id}>AAAAAAAAAAAAAAAAAA{b.assetId} </div>
      ))}
    </LayoutColumn>
  );
}
