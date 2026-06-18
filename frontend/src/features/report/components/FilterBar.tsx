import * as React from 'react';
import { twMerge } from 'tailwind-merge';

import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';

import { DateInput, DateInputNoMin } from '../../booking/components/DateInput';
import { Button } from '../../../components/ui/Button';

import { useUsersData } from '../../user/hooks/useUsersData';
import { useAssetsData } from '../../asset/hooks/useAssetsData';

import type { Filter } from '../types';

type Props = {
  filters: Filter;
  setFilters: React.Dispatch<React.SetStateAction<Filter>>;
  onReset: () => void;
  className?: string;
  setSelectedUserName: React.Dispatch<React.SetStateAction<string>>;
  setSelectedAssetName: React.Dispatch<React.SetStateAction<string>>;
};

type Option = {
  id: number;
  label: string;
};

export default function FiltersBar({
  filters,
  setFilters,
  onReset,
  className,
  setSelectedUserName,
  setSelectedAssetName
}: Props) {
  const update = (partial: Partial<Filter>) => {
    setFilters((prev) => ({
      ...prev,
      ...partial,
    }));
  };

  const {
    users,
    loading: usersLoading,
  } = useUsersData();

  const {
    assets,
    loading: assetsLoading,
  } = useAssetsData();

  const userOptions = React.useMemo<Option[]>(
    () =>
      users.map((user) => ({
        id: user.id,
        label: `${user.name} ${user.surname}`,
      })),
    [users]
  );

  const assetOptions = React.useMemo<Option[]>(
    () =>
      assets.map((asset) => ({
        id: asset.id,
        label: asset.name,
      })),
    [assets]
  );

  return (
    <div
      className={twMerge(
        'rounded-2xl border border-(--color-table-border) bg-white p-5 shadow-sm dark:bg-(--color-bg-dark)',
        className
      )}
    >
      <div className="flex flex-col gap-4">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
          <DateInputNoMin
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
        </div>

        <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-[1fr_1fr_auto]">
          <Autocomplete<Option>
            options={userOptions}
            loading={usersLoading}
            getOptionLabel={(option) => option.label}
            value={
              userOptions.find((u) => u.id === filters.userId) ?? null
            }
            onChange={(_, value) => {
                update({
                  userId: value?.id ?? null,
                })

                setSelectedUserName(value?.label ?? '');
              }
            }
            renderInput={(params) => (
              <TextField
                {...params}
                label="User"
                size="small"
              />
            )}
          />

          <Autocomplete<Option>
            options={assetOptions}
            loading={assetsLoading}
            getOptionLabel={(option) => option.label}
            value={
              assetOptions.find((a) => a.id === filters.assetId) ?? null
            }
            onChange={(_, value) => {
                update({
                  assetId: value?.id ?? null,
                })

                setSelectedAssetName(value?.label ?? '');
              }
            }
            renderInput={(params) => (
              <TextField
                {...params}
                label="Asset"
                size="small"
              />
            )}
          />

          <div className="flex gap-2 md:justify-end xl:self-center">
            <Button
              variant="secondary"
              onClick={onReset}
              className="h-10 min-w-[100px]"
            >
              Reset
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}