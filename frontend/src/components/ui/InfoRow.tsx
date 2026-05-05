export type InfoRowProps = {
  label: string;
  value?: string | null;
  valueClassName?: string;
  emptyValue?: string;
};

export function InfoRow({
  label,
  value,
  valueClassName = '',
  emptyValue = '-',
}: InfoRowProps) {
  return (
    <div className="flex flex-col gap-1 border-b border-(--color-table-border) py-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
      <span className="text-sm font-semibold tracking-wide text-(--color-table-text)">
        {label}
      </span>
      <span className={`text-sm text-black sm:text-right dark:text-white ${valueClassName}`}>
        {value && value.trim() !== '' ? value : emptyValue}
      </span>
    </div>
  );
}
