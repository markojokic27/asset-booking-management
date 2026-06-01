import * as React from 'react';
// import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';

import { DateInput } from '../../booking/components/DateInput';
import { Button } from '../../../components/ui/Button';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
// import { useTheme } from '../../../app/ThemeProvider';

import type { Filters } from '../types';
import { useUsersData } from '../../user/hooks/useUsersData';
import { useAssetsData } from '../../asset/hooks/useAssetsData';

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

  const { users } = useUsersData();

  const userOptions = users.map((user) => ({
    id: user.id,
    label: `${user.name} ${user.surname}`,
  }));

  const { assets } = useAssetsData();

  const assetOptions = assets.map((asset) => ({
    id: asset.id,
    label: asset.name,
  }));

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
        options={userOptions}
        getOptionLabel={(option) => option.label}
        value={userOptions.find((u) => u.id === filters.userId) ?? null}
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
        options={assetOptions}
        getOptionLabel={(option) => option.label}
        value={assetOptions.find((a) => a.id === filters.assetId) ?? null}
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