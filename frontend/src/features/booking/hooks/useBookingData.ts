// External packages
import * as React from 'react';

// Types
import type { AssetDto } from '../../asset/types';
import type { AssetCategoryDto } from '../../asset-category/types';

// API
import { getAllAssets } from '../../asset/api/assetApi';
import { getAllCategories } from '../../asset-category/api/categoryApi';
import { getAllCategoryBookings } from '../api/bookingApi';

export function useBookingData() {
  const [assets, setAssets] = React.useState<AssetDto[]>([]);
  const [categories, setCategories] = React.useState<AssetCategoryDto[]>([]);
  const [selectedCategory, setSelectedCategory] =
    React.useState<AssetCategoryDto | null>(null);
  const [loading, setLoading] = React.useState(false);

  React.useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const [assetRes, categoryRes] = await Promise.all([
          getAllAssets(0, 50),
          getAllCategories(0, 50),
        ]);

        setAssets(assetRes.content);
        setCategories(categoryRes.content);
        setSelectedCategory(categoryRes.content[0] ?? null);

        getAllCategoryBookings(0, 100, categoryRes.content[0]?.id ?? 0);
      } catch (err) {
        console.error('Error fetching data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const selectCategoryByName = (catName: string) => {
    const cat = categories.find((c) => c.name === catName);
    setSelectedCategory(cat ?? null);
  };

  return {
    assets,
    categories,
    selectedCategory,
    setSelectedCategory,
    selectCategoryByName,
    loading,
  };
}
