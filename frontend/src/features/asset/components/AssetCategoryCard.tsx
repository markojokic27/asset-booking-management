import * as React from 'react';
import { twMerge } from 'tailwind-merge';

export type AssetCategoryCardProps = {
  title: string;
  isSelected?: boolean;
  onClick?: () => void;
  className?: string;
};

export const AssetCategoryCard: React.FC<AssetCategoryCardProps> = ({
  title,
  isSelected = false,
  onClick,
  className,
}) => {
  return (
    <button
      type="button"
      onClick={onClick}
      className={twMerge(
        'group min-h-24 cursor-pointer overflow-hidden rounded-lg border border-[var(--color-table-border)] bg-[var(--color-table-surface)] text-left text-[var(--color-text)] shadow-(--shadow-card) transition duration-100',
        isSelected
          ? 'bg-[var(--color-surface-hover)]'
          : 'hover:-translate-y-0.5 hover:bg-[var(--color-surface-hover)]',
        className
      )}
    >
      <div className="flex h-full flex-col justify-between p-4">
        <span className="text-[10px] font-semibold uppercase tracking-[0.22em] text-[var(--color-table-head-text)] opacity-50">
          Category
        </span>
        <div>
          <span className="block text-base font-black tracking-[0.06em]">
            {title}
          </span>
        </div>
      </div>
    </button>
  );
};
