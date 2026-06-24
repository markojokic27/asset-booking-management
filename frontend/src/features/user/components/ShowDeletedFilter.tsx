import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';

type ShowDeletedFilterProps = {
  checked: boolean;
  onToggle: () => void;
  className?: string;
  labelKey?: string;
};

export function ShowDeletedFilter({
  checked,
  onToggle,
  className,
  labelKey = 'users.filters.showDeleted',
}: ShowDeletedFilterProps) {
  const { t } = useTranslation();

  return (
    <label data-testid="toggle-deleted"
      className={twMerge(
        'relative inline-flex h-10 items-center gap-2.5 rounded-lg bg-white px-4 text-sm font-medium text-gray-900 shadow-sm ring-1 ring-black/5 transition-colors hover:bg-gray-50 hover:cursor-pointer focus-within:ring-2 focus-within:ring-black/10 dark:bg-gray-900 dark:text-gray-100 dark:ring-white/10 dark:hover:bg-gray-800 dark:focus-within:ring-white/20',
        className
      )}
    >
      <input data-testid="toggle-deleted-assets"
        type="checkbox"
        checked={checked}
        onChange={onToggle}
        className="peer sr-only"
      />
      <span
        aria-hidden="true"
        className="grid h-4 w-4 place-items-center rounded-full bg-white shadow ring-1 ring-black/10 transition-colors peer-checked:bg-(--color-primaryblue) peer-checked:ring-black/10 peer-checked:[&>svg]:opacity-100 dark:bg-gray-950 dark:ring-white/10 dark:peer-checked:ring-white/10"
      >
        <svg
          viewBox="0 0 20 20"
          fill="none"
          className="h-3 w-3 text-white opacity-0 transition-opacity"
          aria-hidden="true"
        >
          <path
            d="M16.667 5.833 8.333 14.167 3.333 9.167"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      </span>
      {t(labelKey as any)}
    </label>
  );
}
