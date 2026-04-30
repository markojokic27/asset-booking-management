// External packages
import * as React from 'react';
import { twMerge } from 'tailwind-merge';
import { useTranslation } from 'react-i18next';

// Components
import { SearchInput } from './SearchBar';
import { DateTimeInput } from './DateTimeInput';

type Filters = {
  search: string;
  fromDate: string;
  toDate: string;
  fromHour: string;
  toHour: string;
};

type Variant = 'DAY' | 'HOUR';

type Props = {
  filters: Filters;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
  showSearch?: boolean;
  variant: Variant;
  className?: string;
};

export function FiltersBar({
  filters,
  setFilters,
  showSearch = true,
  variant,
  className,
}: Props) {
  const { t } = useTranslation();
  const update = (partial: Partial<Filters>) => {
    setFilters((prev) => ({ ...prev, ...partial }));
  };

  return (
    <div
      className={twMerge(
        'mt-6 grid w-full grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3',
        className
      )}
    >
      {variant === 'HOUR' ? (
        <>
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
        </>
      ) : (
        <DateTimeInput
          id="date"
          label={t('ui.filters.date')}
          value={filters.fromDate}
          onChange={(v) => update({ fromDate: v })}
          className="col-span-1 w-full sm:col-span-2 md:col-span-1 lg:col-start-1"
        />
      )}

      {showSearch && (
        <SearchInput
          value={filters.search}
          onChange={(v) => update({ search: v })}
          placeholder="Search assets..."
          className="col-span-1 w-full sm:col-span-2 lg:col-start-3 lg:mt-5 lg:ml-auto lg:max-w-60"
        />
      )}
    </div>
  );
}
