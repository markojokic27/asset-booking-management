// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';

// Components
import type { AssetDto, AssetStatus } from '../types';

export type AssetModalProps = {
  isOpen: boolean;
  onClose: () => void;
  asset: AssetDto | null;
};

const statusClassNames: Record<AssetStatus, string> = {
  ACTIVE:
    'bg-(--color-status-active-bg) text-(--color-status-active-text)',
  INACTIVE:
    'bg-(--color-status-inactive-bg) text-(--color-status-inactive-text)',
  DAMAGED:
    'bg-(--color-status-damaged-bg) text-(--color-status-damaged-text)',
  DELETED:
    'bg-(--color-status-deleted-bg) text-(--color-status-deleted-text)',
};

export const AssetModal: React.FC<AssetModalProps> = ({
  isOpen,
  onClose,
  asset,
}) => {
  const { t } = useTranslation();

  if (!isOpen || !asset) return null;

  const statusLabel = t(`assets.status.${asset.status}`);
  const statusClassName = statusClassNames[asset.status];

  return (
    <div data-testid="asset-view-modal"
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label={t('assets.modals.view.aria')}
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-md overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-end px-8 pt-6 pb-4">
          <button
            data-testid="asset-details-close-button"
            type="button"
            onClick={onClose}
            aria-label={t('assets.modals.close')}
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
          >
            <CloseIcon className="pointer-events-none" />
          </button>
        </div>
        <div className="mx-8 h-px bg-(--color-table-border)" />
        <div className="flex gap-10 px-8 py-8">
          <div className="flex flex-1 flex-col items-stretch space-y-5">
            <span
              data-testid="asset-status"
              className={[
                'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
                statusClassName,
              ].join(' ')}
            >
              {statusLabel}
            </span>
            <div>
              <p
                data-testid="asset-category"
                className="text-sm text-(--color-modal-label)"
              >
                {t('assets.modals.view.category')}
              </p>
              <p
                className="font-medium text-(--color-text)"
              >
                {asset.categoryName ?? '?'}
              </p>
            </div>
            <div>
              <p
                data-testid="asset-name"
                className="text-sm text-(--color-modal-label)"
              >
                {t('assets.modals.fields.name')}
              </p>
              <p
                className="font-medium text-(--color-text)"
              >
                {asset.name}
              </p>
            </div>
            <div>
              <p className="text-sm text-(--color-modal-label)">
                {t('assets.modals.fields.location')}
              </p>
              <p
                data-testid="asset-description"
                className="text-sm text-(--color-text)"
              >
                {asset.location ?? '-'}
              </p>
            </div>
            <div>
              <p className="text-sm text-(--color-modal-label)">
                {t('assets.modals.fields.description')}
              </p>
              <p
                data-testid="asset-description"
                className="text-sm text-(--color-text)"
              >
                {asset.description ?? '-'}
              </p>
            </div>
          </div>
        </div>
        <div className="mx-8 h-px bg-(--color-table-border)" />
        <div className="px-8 py-5" />
      </div>
    </div>
  );
};
