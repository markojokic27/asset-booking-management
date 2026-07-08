// External packages
import * as React from 'react';
import { twMerge } from 'tailwind-merge';
import { useTranslation } from 'react-i18next';

// Components
import { SearchInput } from '../../../components/ui/SearchBar';
import { DateInput } from './DateInput';
import { HourSelect } from './HourSelect';

// Types
import type { Filters } from '../types';

type Variant = 'DAY' | 'HOUR' | 'DAYS';

type Props = {
  filters: Filters;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
  showSearch?: boolean;
  variant: Variant;
  bookingLimit?: Date;
  setVisibleMonth?: React.Dispatch<React.SetStateAction<Date>>;
  className?: string;
};

export function FiltersBar({
  filters,
  setFilters,
  showSearch = true,
  variant,
  bookingLimit,
  setVisibleMonth,
  className,
}: Readonly<Props>) {
  const { t } = useTranslation();

  React.useEffect(() => {
    if (filters.fromDate) {
      setVisibleMonth?.(new Date(`${filters.fromDate}T00:00:00`));
    }
  }, [filters.fromDate, setVisibleMonth]);

  const maxDate = bookingLimit
    ? bookingLimit.toLocaleDateString('sv-SE')
    : undefined;

  const update = (partial: Partial<Filters>) => {
    setFilters((prev) => {
      const next = { ...prev, ...partial };

      if ('fromDate' in partial && !partial.toDate) {
        next.selectedWeekdays = [];
      }

      if ('fromDate' in partial && next.fromDate > next.toDate) {
        next.toDate = next.fromDate;
      }

      if ('toDate' in partial && next.toDate < next.fromDate) {
        next.fromDate = next.toDate;
        next.selectedWeekdays = [];
      }

      if ('toDate' in partial && !next.fromDate) {
        next.fromDate = next.toDate;
        next.selectedWeekdays = [];
      }

      if (next.fromHour && next.toHour && next.toHour <= next.fromHour) {
        next.toHour = '';
      }

      return next;
    });
  };

  return (
    <div
      className={twMerge(
        'mt-6 grid w-full grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3',
        className
      )}
    >
      {variant === 'HOUR' ? (
        <>
          <DateInput
            id="date"
            label={t('ui.filters.date')}
            placeholder={t('ui.filters.selectDate')}
            value={filters.fromDate}
            onChange={(v) =>
              update({
                fromDate: v,
                toDate: v,
              })
            }
            max={maxDate}
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
      ) : variant === 'DAY' ? (
        <DateInput
          id="date"
          label={t('ui.filters.date')}
          placeholder={t('ui.filters.selectDate')}
          value={filters.fromDate}
          onChange={(v) => update({ fromDate: v })}
          max={maxDate}
          className="col-span-1 w-full sm:col-span-2 md:col-span-2 lg:col-start-1 lg:w-1/2"
        />
      ) : (
        <>
          <DateInput
            id="date"
            label={t('ui.filters.fromDate')}
            placeholder={t('ui.filters.selectDate')}
            value={filters.fromDate}
            testId="from-date-input"
            onChange={(v) => update({ fromDate: v })}
            max={maxDate}
            className="col-span-1 w-full sm:col-span-2 md:col-span-2 lg:col-span-1"
          />
          <DateInput
            id="date"
            label={t('ui.filters.toDate')}
            placeholder={t('ui.filters.selectDate')}
            value={filters.toDate}
            testId="to-date-input"
            onChange={(v) => update({ toDate: v })}
            max={maxDate}
            className="col-span-1 w-full sm:col-span-2 md:col-span-2 lg:col-span-1"
          />
        </>
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
