// External packages
import * as React from 'react';
import { twMerge } from 'tailwind-merge';
import {
  CATEGORY_ICON_DEFAULT_SRC,
  CATEGORY_ICON_FALLBACK_SRC,
  getCategoryIconSrc,
} from '../../asset-category/utils/categoryIcon';

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
        'group min-h-24 cursor-pointer overflow-hidden rounded-lg border border-(--color-table-border) bg-(--color-table-surface) text-left text-(--color-text) shadow-(--shadow-card) transition duration-100',
        isSelected
          ? 'bg-(--color-surface-hover)'
          : 'hover:-translate-y-0.5 hover:bg-(--color-surface-hover)',
        className
      )}
    >
      <div className="relative flex h-full p-4">
        <img
          src={getCategoryIconSrc(title)}
          alt=""
          className="pointer-events-none absolute inset-0 h-full w-full object-cover opacity-40"
          onError={(e) => {
            const img = e.currentTarget;
            if (img.src.endsWith(CATEGORY_ICON_DEFAULT_SRC)) {
              img.src = CATEGORY_ICON_FALLBACK_SRC;
              return;
            }
            img.src = CATEGORY_ICON_DEFAULT_SRC;
          }}
        />
        <div className="pointer-events-none absolute inset-0 bg-(--color-table-surface)/25" />

        <div className="relative z-10 flex flex-1 flex-col justify-between">
          <span className="text-[10px] font-semibold tracking-[0.22em] text-(--color-table-head-text) uppercase opacity-50">
            Category
          </span>
          <div>
            <span data-testid="asset-category-card-title" className="block text-base font-black tracking-[0.06em]">
              {title}
            </span>
          </div>
        </div>
      </div>
    </button>
  );
};
