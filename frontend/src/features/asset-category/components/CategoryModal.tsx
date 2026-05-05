import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { Modal } from '../../../components/ui/Modal';
import { IconButton } from '../../../components/ui/IconButton';
import type { AssetCategoryDto } from '../types';
import { CATEGORY_ICON_DEFAULT_SRC, getCategoryIconSrc } from '../utils/categoryIcon';

export type CategoryModalProps = {
  isOpen: boolean;
  onClose: () => void;
  category: AssetCategoryDto | null;
};

export const CategoryModal: React.FC<CategoryModalProps> = ({ isOpen, onClose, category }) => {
  if (!isOpen || !category) return null;

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel="User details"
      headerRight={
        <IconButton onClick={onClose} aria-label="Close">
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={<div />}
    >
      <div className="space-y-5">
        <div>
          <p className="text-sm text-(--color-modal-label)">Icon</p>
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
          <p className="text-sm text-(--color-modal-label)">Name</p>
          <p data-testid="category-name" className="font-medium text-(--color-text)">
            {category.name}
          </p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">Description</p>
          <p data-testid="category.description" className="font-medium text-(--color-text)">
            {category.description}
          </p>
        </div>
        <div>
          <p className="text-sm text-(--color-modal-label)">Booking Period</p>
          <p data-testid="category-bookingPeriod" className="font-medium text-(--color-text)">{category.bookingPeriod}</p>
        </div>

        <div>
          <p className="text-sm text-(--color-modal-label)">Need Manager approval?</p>
          <p data-testid="category-approval" className="font-medium text-(--color-text)">{category.approval ? 'Yes' : 'No'}</p>
        </div>
      </div>
    </Modal>
  );
};

