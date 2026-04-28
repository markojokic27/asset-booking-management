import { LayoutColumn } from '../components/layout/Layout';
import { AssetCategoryGrid } from '../features/asset/components/AssetCategoryGrid';
import type { AssetDto } from '../features/asset/types';
import type { AssetCategoryDto } from '../features/asset-category/types';
import * as React from 'react';
import { FiltersBar } from '../components/ui/FilterBar';
import { Button } from '../components/ui/Button';
import type { Filters } from '../features/booking/types';
import { BookingTable } from '../features/booking/components/BookingTable';
import { getAllAssets } from '../features/asset/api/assetApi';
import { getAllCategories } from '../features/asset-category/api/categoryApi';
import { BookingModal } from '../features/booking/components/BookingModal';
import { useState } from 'react';

export default function Bookings() {
  const [assets, setAssets] = React.useState<AssetDto[]>([]);
  const [categories, setCategories] = React.useState<AssetCategoryDto[]>([]);
  const [selectedCategoryId, setSelectedCategoryId] = React.useState<
    number | null
  >(null);
  const [filters, setFilters] = React.useState<Filters>({
    search: '',
    fromDate: '',
    toDate: '',
    fromHour: '',
    toHour: '',
  });
  const [loading, setLoading] = React.useState(false);
  const [selectedAsset, setSelectedAsset] = useState<AssetDto | null>(null);
  const [openBookingModal, setOpenBookingModal] = useState(false);

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

        const laptops = categoryRes.content.find(
          (c) => c.name.toLowerCase() === 'laptops'
        );

        setSelectedCategoryId(
          laptops?.id ?? categoryRes.content[0]?.id ?? null
        );
      } catch (err) {
        console.error('Error fetching data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const filteredAssets = React.useMemo(() => {
    return assets
      .filter((a) =>
        selectedCategoryId ? a.categoryId === selectedCategoryId : true
      )
      .filter((asset) =>
        asset.name.toLowerCase().includes(filters.search.trim().toLowerCase())
      );
  }, [assets, selectedCategoryId, filters.search]);

  const selectedCategoryName =
    categories.find((c) => c.id === selectedCategoryId)?.name ?? '';

  const handleOpenBookingModal = (asset: AssetDto) => {
    setSelectedAsset(asset);
    setOpenBookingModal(true);
  };

  return (
    <LayoutColumn
      span={12}
      mdSpan={9}
      mdOffset={3}
      className="flex flex-col pt-35"
    >
      <AssetCategoryGrid
        categories={categories.map((c) => c.name)}
        selectedCategory={selectedCategoryName}
        onSelectCategory={(catName) => {
          const cat = categories.find((c) => c.name === catName);
          setSelectedCategoryId(cat?.id ?? null);
        }}
      />

      <div className="mt-12 flex w-full items-center justify-between gap-4">
        <h1 className="text-3xl leading-11 font-black tracking-[0.2em]">
          {selectedCategoryName}
        </h1>

        <Button
          className="border-gray-400 bg-gray-400 hover:border-gray-300 hover:bg-gray-300"
          onClick={() =>
            setFilters({
              search: '',
              fromDate: '',
              toDate: '',
              fromHour: '',
              toHour: '',
            })
          }
        >
          Reset filters
        </Button>
      </div>

      <div className="mt-6 h-px w-full bg-(--color-table-border)" />

      <FiltersBar filters={filters} setFilters={setFilters} />

      {loading ? (
        <div className="mt-6">Loading...</div>
      ) : (
        <BookingTable
          assets={filteredAssets}
          onBook={handleOpenBookingModal}
          className="mt-6"
        />
      )}
      <BookingModal
        open={openBookingModal}
        onClose={() => setOpenBookingModal(false)}
        asset={selectedAsset}
        filters={filters}
        setFilters={setFilters}
      />
    </LayoutColumn>
  );
}
