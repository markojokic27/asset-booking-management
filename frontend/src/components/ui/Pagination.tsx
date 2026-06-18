import { twMerge } from 'tailwind-merge';
import { useTranslation } from 'react-i18next';

export type PaginationItem = number | 'ellipsis';

type Props = {
  page: number;
  totalPages: number;
  items: PaginationItem[];
  onPageChange: (page: number) => void;
  ariaLabel?: string;
  className?: string;
};

export function Pagination({
  page,
  totalPages,
  items,
  onPageChange,
  ariaLabel,
  className,
}: Readonly<Props>) {
  const { t } = useTranslation();
  const canPrev = page > 1;
  const canNext = page < totalPages;

  return (
    <nav
      className={twMerge('mt-5 flex w-full items-center justify-center', className)}
      aria-label={ariaLabel ?? t('ui.pagination.ariaLabel')}
    >
      <div className="flex items-center gap-2 text-sm text-(--color-table-text)">
        <button
          type="button"
          onClick={() => onPageChange(Math.max(1, page - 1))}
          disabled={!canPrev}
          className="inline-flex cursor-pointer items-center gap-2 rounded px-2 py-1 transition-colors hover:bg-(--color-table-row-hover) disabled:cursor-not-allowed disabled:opacity-50"
        >
          <span aria-hidden="true">‹</span>
          <span>{t('ui.pagination.previous')}</span>
        </button>

        <div className="flex items-center gap-2">
          {items.map((item, idx) => {
            if (item === 'ellipsis') {
              const prev = items[idx - 1];
              const next = items[idx + 1];
              return (
                <span
                  key={`ellipsis-${String(prev)}-${String(next)}`}
                  className="text-(--color-table-text) select-none"
                  aria-hidden="true"
                >
                  …
                </span>
              );
            }

            const isActive = item === page;
            return (
              <button
                key={item}
                type="button"
                onClick={() => onPageChange(item)}
                aria-current={isActive ? 'page' : undefined}
                className={[
                  'inline-flex h-6 w-6 cursor-pointer items-center justify-center rounded border text-xs transition-colors',
                  isActive
                    ? 'border-(--color-table-border) bg-(--color-table-row-hover)'
                    : 'border-transparent hover:bg-(--color-table-row-hover)',
                ].join(' ')}
              >
                {item}
              </button>
            );
          })}
        </div>

        <button
          type="button"
          onClick={() => onPageChange(Math.min(totalPages, page + 1))}
          disabled={!canNext}
          className="inline-flex cursor-pointer items-center gap-2 rounded px-2 py-1 transition-colors hover:bg-(--color-table-row-hover) disabled:cursor-not-allowed disabled:opacity-50"
        >
          <span>{t('ui.pagination.next')}</span>
          <span aria-hidden="true">›</span>
        </button>
      </div>
    </nav>
  );
}
