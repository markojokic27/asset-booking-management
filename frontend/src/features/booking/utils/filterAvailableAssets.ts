// Types
import type { AssetDto } from '../../asset/types';
import type { BookingWithRelations, Filters } from '../types';
import type { AssetCategoryDto } from '../../asset-category/types';

type Props = {
  assets: AssetDto[];
  bookings: BookingWithRelations[];
  selectedCategory: AssetCategoryDto | null;
  filters: Filters;
};

export const filterAvailableAssets = ({
  assets,
  bookings,
  selectedCategory,
  filters,
}: Props) => {
  let filtered = assets;

  // CATEGORY FILTER
  if (selectedCategory) {
    filtered = filtered.filter((a) => a.categoryId === selectedCategory.id);
  }

  // SEARCH FILTER
  filtered = filtered.filter((a) =>
    a.name.toLowerCase().includes(filters.search.trim().toLowerCase())
  );

  if (!filters.fromDate && !filters.toDate) {
    return filtered;
  }

  const isDayVariant = selectedCategory?.bookingPeriod === 'DAY';

  const filterStart = new Date(
    `${filters.fromDate}T${filters.fromHour || '00:00'}`
  );
  const filterEnd = new Date(`${filters.toDate}T${filters.toHour || '23:59'}`);
  return filtered.filter((asset) => {
    if (Boolean(!isDayVariant && (!filters.fromHour || !filters.toHour))) {
      return true; // If it's hour variant and hours are selected, we skip date filtering here
    }
    const assetBookings = bookings.filter(
      (b) => b.asset.id === asset.id && b.status !== 'REJECTED'
    );

    const hasConflict = assetBookings.some((b) => {
      const start = new Date(b.bookingStart);
      const end = new Date(b.bookingEnd);

      if (isDayVariant) {
        return (
          start.getFullYear() === filterStart.getFullYear() &&
          start.getMonth() === filterStart.getMonth() &&
          start.getDate() === filterStart.getDate()
        );
      }

      return start < filterEnd && end > filterStart;
    });

    return !hasConflict;
  });
};
