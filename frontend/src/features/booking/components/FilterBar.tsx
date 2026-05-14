// External packages
import * as React from 'react';
import { twMerge } from 'tailwind-merge';
import { useTranslation } from 'react-i18next';

// Components
import { SearchInput } from '../../../components/ui/SearchBar';
import { DateInput } from './DateInput';
import { HourSelect } from './HourSelect';

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
    setFilters((prev) => {
      const next = { ...prev, ...partial };

      if (next.fromHour && next.toHour && next.toHour <= next.fromHour) {
        next.toHour = '';
      }
      return next;
    });
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
          <DateInput
            id="date"
            label={t('ui.filters.date')}
            value={filters.fromDate}
            onChange={(v) =>
              update({
                fromDate: v,
                toDate: v,
              })
            }
            className="w-full"
          />
          <div className="flex gap-3">
            <HourSelect
              label={t('ui.filters.fromTime')}
              value={filters.fromHour}
              onChange={(v) => update({ fromHour: v })}
              selectedDate={filters.fromDate}
              className="w-1/2"
            />

            <HourSelect
              label={t('ui.filters.toTime')}
              value={filters.toHour}
              onChange={(v) => update({ toHour: v })}
              selectedDate={filters.fromDate}
              minHour={filters.fromHour}
              className="w-1/2"
            />
          </div>
        </>
      ) : (
        <DateInput
          id="date"
          label={t('ui.filters.date')}
          value={filters.fromDate}
          onChange={(v) => update({ fromDate: v })}
          className="col-span-1 w-full sm:col-span-2 md:col-span-2 lg:col-start-1 lg:w-1/2"
        />
      )}

      {showSearch && (
        <SearchInput
          value={filters.search}
          onChange={(v) => update({ search: v })}
          placeholder={t('ui.search.assetsPlaceholder')}
          className="col-span-1 mt-auto w-full sm:col-span-2 md:col-span-1 lg:ml-auto lg:max-w-60"
        />
      )}
    </div>
  );
}
