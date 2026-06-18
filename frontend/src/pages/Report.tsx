import { useTranslation } from 'react-i18next';
import { useState } from 'react';
// Components
import { LayoutColumn } from '../components/layout/Layout';

import FiltersBar from '../features/report/components/FilterBar';
import BookingStatusPie from '../features/report/components/BookingStatusPie';
import BookingStatusBar from '../features/report/components/BookingStatusBar';
import TopUserBookings from '../features/report/components/TopUserBookingsPie';
import TopAssetBookings from '../features/report/components/TopAssetBookingsPie';

// Types
import type { Filter } from '../features/report/types';

import { useGeneralReport } from '../features/report/hooks/useGeneralReport';

const defaultFilters: Filter = {
  fromDate: '',
  toDate: '',
  userId: null,
  assetId: null,
};

export default function Report() {
  const { t } = useTranslation();
  const [filters, setFilters] = useState<Filter>(defaultFilters);
  const [selectedUserName, setSelectedUserName] = useState<string>("")
  const [selectedAssetName, setSelectedAssetName] = useState<string>("")
  const { report } = useGeneralReport(filters);

  const handleResetFilters = () => {
    setFilters(defaultFilters);
    setSelectedUserName("");
    setSelectedAssetName("");
  };

  return (
    <>
      <LayoutColumn
        span={12}
        mdSpan={9}
        mdOffset={3}
        className="flex min-h-screen flex-col pt-35 pb-10"
      >
        <div className="flex w-full flex-col gap-4">
          <div className="flex flex-col gap-2">
            <h1 className="text-3xl font-black tracking-widest text-black dark:text-white">
              {t('report.title')}
            </h1>
          </div>
          <div className="h-px w-full bg-(--color-table-border)" />
        </div>

        <FiltersBar
          filters={filters}
          setFilters={setFilters}
          onReset={handleResetFilters}
          setSelectedUserName={setSelectedUserName}
          setSelectedAssetName={setSelectedAssetName}
          className="mt-6"
        />

        <div className="mt-8 flex flex-col gap-6">
          <div className="dark:bg-bg-dark rounded-2xl border border-(--color-table-border) p-6 shadow-sm dark:shadow-black/20">
            <BookingStatusPie report={report} />
          </div>
          <div className="dark:bg-bg-dark rounded-2xl border border-(--color-table-border) p-6 shadow-sm dark:shadow-black/20">
            <BookingStatusBar report={report} />
          </div>
          <div className="dark:bg-bg-dark rounded-2xl border border-(--color-table-border) p-6 shadow-sm dark:shadow-black/20">
            <TopUserBookings report={report} selectedAssetName={selectedAssetName} />
          </div>
          <div className="dark:bg-bg-dark rounded-2xl border border-(--color-table-border) p-6 shadow-sm dark:shadow-black/20">
            <TopAssetBookings report={report} selectedUserName={selectedUserName}/>
          </div>
        </div>
      </LayoutColumn>
    </>
  );
}
