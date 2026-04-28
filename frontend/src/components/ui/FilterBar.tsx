import * as React from 'react';
import { DateTimeInput } from './DateTimeInput';
import { SearchInput } from './SearchBar';
import { twMerge } from 'tailwind-merge';
import { useTranslation } from 'react-i18next';

type Filters = {
  search: string;
  fromDate: string;
  toDate: string;
  fromHour: string;
  toHour: string;
};

type Props = {
  filters: Filters;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
  showSearch?: boolean;
  className?: string;
};

export function FiltersBar({ filters, setFilters, showSearch = true, className }: Props) {
  const { t } = useTranslation();
  const update = (partial: Partial<Filters>) => {
    setFilters((prev) => ({ ...prev, ...partial }));
  };

  return (
    <div className={twMerge("mt-6 grid w-full grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3", className )}>
      <DateTimeInput
        id="from-date"
        label={t('ui.filters.fromTime')}
        value={filters.fromDate}
        onChange={(v) => update({ fromDate: v })}
        hourValue={filters.fromHour}
        onHourChange={(v) => update({ fromHour: v })}
        className="w-full"
      />

      <DateTimeInput
        id="to-date"
        label={t('ui.filters.toTime')}
        value={filters.toDate}
        onChange={(v) => update({ toDate: v })}
        hourValue={filters.toHour}
        onHourChange={(v) => update({ toHour: v })}
        className="w-full"
      />
      {showSearch && (
        <SearchInput
          value={filters.search}
          onChange={(v) => update({ search: v })}
          placeholder="Search assets..."
          className="col-span-1 w-full sm:col-span-2 lg:col-span-1 lg:mt-5 lg:ml-auto lg:max-w-60"
        />
      )}
    </div>
  );
}
