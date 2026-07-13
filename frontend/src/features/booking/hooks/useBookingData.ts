// External packages
import * as React from 'react';

// Types
import type { AssetDto } from '../../asset/types';
import type { AssetCategoryDto } from '../../asset-category/types';
import type { BookingWithRelations, Filters } from '../types';

// API
import { getAllAssets } from '../../asset/api/assetApi';
import { getAllCategories } from '../../asset-category/api/categoryApi';
import { getAllCategoryBookings } from '../api/bookingApi';

// Utilis
import { filterAvailableAssets } from '../utils/filterAvailableAssets';

type Props = {
  filters: Filters;
};

export function useBookingData({ filters }: Props) {
  const [assets, setAssets] = React.useState<AssetDto[]>([]);
  const [categories, setCategories] = React.useState<AssetCategoryDto[]>([]);
  const [bookings, setBookings] = React.useState<BookingWithRelations[]>([]);
  const [selectedCategory, setSelectedCategory] =
    React.useState<AssetCategoryDto | null>(null);
  const [loading, setLoading] = React.useState(false);

  React.useEffect(() => {
    const fetchInitialData = async () => {
      try {
        setLoading(true);
        const [assetRes, categoryRes] = await Promise.all([
          getAllAssets(0, 100),
          getAllCategories(0, 100),
        ]);
        setAssets(assetRes.content);
        setCategories(categoryRes.content);

        const savedCategory = localStorage.getItem('selectedBookingCategory');

        if (savedCategory) {
          const category = categoryRes.content.find(
            (c) => c.name === savedCategory
          );
          setSelectedCategory(category ?? categoryRes.content[0]);
        } else {
          if (categoryRes.content.length > 0) {
            setSelectedCategory(categoryRes.content[0]);
          }
        }
      } catch (err) {
        console.error('Error fetching initial booking data:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchInitialData();
  }, []);

  const fetchBookings = React.useCallback(async () => {
    if (!selectedCategory) return;
    try {
      setLoading(true);
      const bookingRes = await getAllCategoryBookings(
        0,
        100,
        selectedCategory.id
      );
      setBookings(bookingRes.content);
    } catch (err) {
      console.error('Error fetching category bookings:', err);
    } finally {
      setLoading(false);
    }
  }, [selectedCategory]);

  React.useEffect(() => {
    fetchBookings();
  }, [fetchBookings]);

  const selectCategoryByName = (categoryName: string) => {
    const category = categories.find((c) => c.name === categoryName);

    if (category) {
      localStorage.setItem('selectedBookingCategory', category.name);
    }

    setSelectedCategory(category ?? null);
  };

  const filteredAssets = React.useMemo(() => {
    return filterAvailableAssets({
      assets,
      bookings,
      selectedCategory,
      filters,
    });
  }, [assets, bookings, selectedCategory, filters]);

  return {
    assets: filteredAssets,
    categories,
    bookings,
    selectedCategory,
    setSelectedCategory,
    selectCategoryByName,
    loading,
    refetchBookings: fetchBookings,
  };
}
