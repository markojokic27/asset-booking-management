// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';

// Components
import type { AssetDto } from '../types';
import { AssetStatusBadge } from './AssetStatusBadge';

export type AssetModalProps = {
  isOpen: boolean;
  onClose: () => void;
  asset: AssetDto | null;
};

export const AssetModal: React.FC<AssetModalProps> = ({
  isOpen,
  onClose,
  asset,
}) => {
  const { t } = useTranslation();
  const dialogRef = React.useRef<HTMLDialogElement>(null);

  React.useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;

    if (isOpen && asset) {
      if (!dialog.open) dialog.showModal();
    } else if (dialog.open) {
      dialog.close();
    }
  }, [isOpen, asset]);

  if (!isOpen || !asset) return null;

  return (
    <dialog
      ref={dialogRef}
      data-testid="asset-view-modal"
      className="fixed inset-0 z-50 m-0 flex h-full max-h-full w-full max-w-full items-center justify-center border-0 bg-transparent p-6 backdrop:bg-transparent"
      aria-label={t('assets.modals.view.aria')}
      onCancel={(e) => {
        e.preventDefault();
        onClose();
      }}
    >
      <button
        type="button"
        data-testid="asset-view-modal-backdrop"
        className="fixed inset-0 cursor-default bg-(--color-modal-overlay)"
        aria-label={t('assets.modals.view.closeAria')}
        onClick={onClose}
      />
      <div className="relative z-10 w-full max-w-md overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-between gap-4 px-8 pt-6 pb-4">
          <h2 className="text-2xl font-bold">{asset.name}</h2>
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
            <AssetStatusBadge status={asset.status} />
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
    </dialog>
  );
};
