// FiltersBar.tsx
import * as React from 'react';
import { DateTimeInput } from './DateTimeInput';
import { SearchInput } from './SearchBar';

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
};

export function FiltersBar({ filters, setFilters }: Props) {
  const update = (partial: Partial<Filters>) => {
    setFilters((prev) => ({ ...prev, ...partial }));
  };

  return (
    <div className="mt-6 grid w-full grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3">
      <DateTimeInput
        id="from-date"
        label="From time"
        value={filters.fromDate}
        onChange={(v) => update({ fromDate: v })}
        hourValue={filters.fromHour}
        onHourChange={(v) => update({ fromHour: v })}
        className="w-full"
      />

      <DateTimeInput
        id="to-date"
        label="To time"
        value={filters.toDate}
        onChange={(v) => update({ toDate: v })}
        hourValue={filters.toHour}
        onHourChange={(v) => update({ toHour: v })}
        className="w-full"
      />

      <SearchInput
        value={filters.search}
        onChange={(v) => update({ search: v })}
        placeholder="Search assets..."
        className="col-span-1 w-full sm:col-span-2 lg:col-span-1 lg:mt-5 lg:ml-auto"
      />
    </div>
  );
}
