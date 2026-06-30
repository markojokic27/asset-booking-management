import type { ReactNode } from 'react';

export type InfoRowProps = {
  label: string;
  value?: string | null;
  valueClassName?: string;
  emptyValue?: string;
  /** Renders instead of the text value (e.g. actions). */
  valueSlot?: ReactNode;
};

export function InfoRow({
  label,
  value,
  valueClassName = '',
  emptyValue = '-',
  valueSlot,
}: Readonly<InfoRowProps>) {
  return (
    <div className="flex flex-col gap-1 border-b border-(--color-table-border) py-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
      <span className="text-sm font-semibold tracking-wide text-(--color-table-text)">
        {label}
      </span>
      {valueSlot == null ? (
        <span className={`text-sm text-black sm:text-right dark:text-white ${valueClassName}`}>
          {value && value.trim() !== '' ? value : emptyValue}
        </span>
      ) : (
        <div className="flex w-full shrink-0 flex-col sm:w-auto sm:items-end">{valueSlot}</div>
      )}
    </div>
  );
}
