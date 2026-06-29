// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
import {
  CATEGORY_ICON_DEFAULT_SRC,
  getCategoryIconSrc,
} from '../../asset-category/utils/categoryIcon';

export type AssetCategoryCardProps = {
  title: string;
  isSelected?: boolean;
  onClick?: () => void;
  className?: string;
  showBackgroundImage?: boolean;
  'data-testid'?: string;
};

export const AssetCategoryCard: React.FC<AssetCategoryCardProps> = ({
  title,
  isSelected = false,
  onClick,
  className,
  showBackgroundImage = true,
  'data-testid': dataTestId,
}) => {
  const { t } = useTranslation();
  const isPlainCard = !showBackgroundImage;

  let variantClassName: string;
  if (isPlainCard) {
    variantClassName = twMerge(
      'bg-(--color-surface) text-black dark:text-white',
      !isSelected && 'hover:-translate-y-0.5 hover:bg-(--color-bg)'
    );
  } else {
    const imageCardStateClassName = isSelected
      ? 'bg-(--color-surface-hover)'
      : 'hover:-translate-y-0.5 hover:bg-(--color-surface-hover)';
    variantClassName = twMerge(
      'bg-(--color-table-surface) text-white dark:text-(--color-text)',
      imageCardStateClassName
    );
  }

  return (
    <button 
      type="button"
      onClick={onClick}
      data-testid={dataTestId ?? `category-card-${title.toLowerCase()}`}
      className={twMerge(
        'group min-h-24 cursor-pointer overflow-hidden rounded-lg border border-(--color-table-border) text-left shadow-(--shadow-card) transition duration-100',
        variantClassName,
        className
      )}
    >
      <div className="relative flex h-full p-4">
        {showBackgroundImage && (
          <>
            <img
              src={getCategoryIconSrc(title)}
              alt=""
              className="pointer-events-none absolute inset-0 h-full w-full object-cover opacity-70 dark:opacity-40"
              onError={(e) => {
                const img = e.currentTarget;
                img.onerror = null;
                img.src = CATEGORY_ICON_DEFAULT_SRC;
              }}
            />
            <div className="pointer-events-none absolute inset-0 bg-black/25 dark:bg-(--color-table-surface)/25" />
          </>
        )}

        <div className="relative z-10 flex flex-1 flex-col justify-between">
          <span
            className={twMerge(
              'text-[10px] font-semibold tracking-[0.22em] uppercase',
              isPlainCard
                ? 'invisible text-black/70 dark:text-white/70'
                : 'text-white/70 dark:text-(--color-table-head-text) dark:opacity-50'
            )}
            aria-hidden={isPlainCard}
          >
            {t('assets.categoryCard.badge')}
          </span>
          <div>
            <span className="block text-base font-black tracking-[0.06em]">
              {title}
            </span>
          </div>
        </div>
      </div>
    </button>
  );
};
