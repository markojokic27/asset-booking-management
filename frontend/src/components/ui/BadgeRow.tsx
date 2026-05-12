export type BadgeRowProps = {
  label: string;
  value: string;
  badgeClassName: string;
  testId?: string;
};

export function BadgeRow({
  label,
  value,
  badgeClassName,
  testId,
}: Readonly<BadgeRowProps>) {
  return (
    <div className="flex flex-col gap-1 border-b border-(--color-table-border) py-4 sm:flex-row sm:items-center sm:justify-between sm:gap-6">
      <span className="text-sm font-semibold tracking-wide text-(--color-table-text)">
        {label}
      </span>
      <span
        data-testid={testId}
        className={`inline-flex w-fit rounded-full px-2.5 py-1 text-xs font-semibold sm:ml-auto ${badgeClassName}`}
      >
        {value}
      </span>
    </div>
  );
}
