import * as React from 'react';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';
import type { AssetCategoryDto, BookingPeriod } from '../types';
import { CATEGORY_ICON_DEFAULT_SRC, getCategoryIconSrc } from '../utils/categoryIcon';

export type CategoryModalProps = {
  isOpen: boolean;
  onClose: () => void;
  category: AssetCategoryDto | null;
};

export const CategoryModal: React.FC<CategoryModalProps> = ({ isOpen, onClose, category }) => {
  const { t } = useTranslation();

  if (!isOpen || !category) return null;

  const bookingPeriodLabelKeys = {
    HOUR: 'assetCategories.bookingPeriod.hour',
    DAY: 'assetCategories.bookingPeriod.day',
  } as const satisfies Record<BookingPeriod, string>;

  const bookingPeriodLabel = t(bookingPeriodLabelKeys[category.bookingPeriod]);

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('assetCategories.modals.view.ariaLabel')}
      title={<h2 className="text-2xl font-bold">{category.name}</h2>}
      headerRight={
        <IconButton data-testid="category-close-button" onClick={onClose} aria-label={t('assetCategories.modals.common.closeAria')}>
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <div>
          <p className="text-sm text-(--color-modal-label)">{t('assetCategories.modals.view.fields.icon')}</p>
          <img
            src={getCategoryIconSrc(category.name)}
            alt=""
            className="mt-2 h-24 w-24 rounded-lg border border-(--color-table-border) bg-white object-cover shadow-(--shadow-card)"
            onError={(e) => {
              const img = e.currentTarget;
              img.onerror = null;
              img.src = CATEGORY_ICON_DEFAULT_SRC;
            }}
          />
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">{t('assetCategories.modals.view.fields.name')}</p>
          <p data-testid="category-name" className="font-medium text-(--color-text)">
            {category.name}
          </p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">{t('assetCategories.modals.view.fields.description')}</p>
          <p data-testid="category.description" className="font-medium text-(--color-text)">
            {category.description}
          </p>
        </div>
        <div>
          <p className="text-sm text-(--color-modal-label)">{t('assetCategories.modals.view.fields.bookingPeriod')}</p>
          <p data-testid="category-bookingPeriod" className="font-medium text-(--color-text)">{bookingPeriodLabel}</p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">{t('assetCategories.modals.view.fields.approval')}</p>
          <p data-testid="category-approval" className="font-medium text-(--color-text)">
            {category.approval ? t('assetCategories.modals.view.approval.yes') : t('assetCategories.modals.view.approval.no')}
          </p>
        </div>
      </div>
    </Modal>
  );
};

