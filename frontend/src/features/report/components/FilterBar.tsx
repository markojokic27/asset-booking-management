import * as React from 'react';
// import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';

import { DateInput } from '../../booking/components/DateInput';
import { Button } from '../../../components/ui/Button';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
// import { useTheme } from '../../../app/ThemeProvider';

import type { Filters } from '../types';

type Props = {
  filters: Filters;
  setFilters: React.Dispatch<React.SetStateAction<Filters>>;
  onReset: () => void;
  className?: string;
};

export default function FiltersBar({
  filters,
  setFilters,
  onReset,
  className,
}: Props) {
  // const { t } = useTranslation();
  // const { theme } = useTheme();
  
  // const isDark = theme === 'dark';

  const update = (partial: Partial<Filters>) => {
    setFilters((prev) => ({
      ...prev,
      ...partial,
    }));
  };

  const users = [
    {
        id: 1,
        label: "John Doe",
    },
    {
        id: 2,
        label: "Jane Doe",
    },
    {
        id: 3,
        label: "Mark Jones",
    },
  ]

  const assets = [
    {
      id: 1,
      label: "Parking spot 10",
    },
    {
      id: 2,
      label: "Macbook Pro 16",
    },
  ];

  return (
    <div
      className={twMerge(
        'grid w-full grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-5',
        className
      )}
    >
      <DateInput
        id="fromDate"
        label="From"
        value={filters.fromDate}
        onChange={(v) => update({ fromDate: v })}
      />

      <DateInput
        id="toDate"
        label="To"
        value={filters.toDate}
        onChange={(v) => update({ toDate: v })}
      />

     <Autocomplete
        options={users}
        getOptionLabel={(option) => option.label}
        value={users.find((u) => u.id === filters.userId) ?? null}
        onChange={(_, value) =>
            update({
                userId: value?.id ?? null,
            })
        }
        renderInput={(params) => (
            <TextField {...params} label="Users" />
        )}
        />

      <Autocomplete
        options={assets}
        getOptionLabel={(option) => option.label}
        value={assets.find((a) => a.id === filters.assetId) ?? null}
        onChange={(_, value) =>
            update({
                assetId: value?.id ?? null,
            })
        }
        renderInput={(params) => (
            <TextField {...params} label="Assets" />
        )}
        />

    <div>
      <Button
        variant="solid"
        className="h-fit self-end"
      >
        Apply
      </Button>

      <Button
        variant="secondary"
        onClick={onReset}
        className="h-fit self-end"
      >
        Reset
      </Button>
    </div>

    </div>
  );
}