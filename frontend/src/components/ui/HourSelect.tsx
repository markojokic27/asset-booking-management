import * as React from 'react';
import * as Select from '@radix-ui/react-select';
import { ChevronDown } from '../icons/ChevronDown';
import { twMerge } from 'tailwind-merge';

type Props = {
  value: string;
  onChange: (value: string) => void;
  hourOptions: readonly string[];
  hasSelectedHour: boolean;
};
// TODO: positioning of popup...
export function HourSelect({
  value,
  onChange,
  hourOptions,
  hasSelectedHour,
}: Props) {
  const [open, setOpen] = React.useState(false);

  return (
    <Select.Root value={value} onValueChange={onChange} onOpenChange={setOpen}>
      <Select.Trigger
        className={twMerge(
          'flex h-11 w-24 items-center justify-between rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-2 text-sm transition hover:border-(--color-table-text) focus:outline-none'
        )}
      >
        <Select.Value
          placeholder="07:00"
          className={
            hasSelectedHour
              ? 'text-(--color-table-text)'
              : 'text-(--color-table-text)/60'
          }
        />

        <Select.Icon asChild>
          <ChevronDown
            className={twMerge(
              'h-4 w-4 text-(--color-table-text) transition-transform duration-200',
              open && 'rotate-180'
            )}
          />
        </Select.Icon>
      </Select.Trigger>

      <Select.Portal>
        <Select.Content
          side="bottom"
          align="start"
          className="z-50 rounded-lg border border-(--color-table-border) bg-(--color-table-surface) shadow-lg"
        >
          <Select.Viewport className="p-1">
            {hourOptions.map((hour) => (
              <Select.Item
                key={hour}
                value={hour}
                className="flex cursor-pointer items-center rounded-md px-2 py-1 text-sm text-(--color-table-text) outline-none hover:bg-(--color-table-border)/20 focus:bg-(--color-table-border)/30"
              >
                <Select.ItemText>{hour}</Select.ItemText>
              </Select.Item>
            ))}
          </Select.Viewport>
        </Select.Content>
      </Select.Portal>
    </Select.Root>
  );
}
