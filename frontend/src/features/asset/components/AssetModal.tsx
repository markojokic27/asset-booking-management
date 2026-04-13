import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';
import type { AssetDto, AssetStatus } from '../types';

export type AssetModalProps = {
  isOpen: boolean;
  onClose: () => void;
  asset: AssetDto | null;
};

const statusConfig: Record<AssetStatus, { label: string; className: string }> =
  {
    ACTIVE: {
      label: 'Active',
      className:
        'bg-(--color-status-active-bg) text-(--color-status-active-text)',
    },
    INACTIVE: {
      label: 'Inactive',
      className:
        'bg-(--color-status-inactive-bg) text-(--color-status-inactive-text)',
    },
    DAMAGED: {
      label: 'Damaged',
      className:
        'bg-(--color-status-damaged-bg) text-(--color-status-damaged-text)',
    },
  };

export const AssetModal: React.FC<AssetModalProps> = ({
  isOpen,
  onClose,
  asset,
}) => {
  if (!isOpen || !asset) return null;

  const { label: statusLabel, className: statusClassName } =
    statusConfig[asset.status];

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label="Asset details"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-end px-8 pt-6 pb-4">
          <button
            type="button"
            onClick={onClose}
            aria-label="Close"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
          >
            <CloseIcon className="pointer-events-none" />
          </button>
        </div>
        <div className="mx-8 h-px bg-(--color-table-border)" />
        <div className="flex gap-10 px-8 py-8">
          <div className="flex w-65 flex-col items-center justify-center">
            {asset.imageUrl ? (
              <img
                src={asset.imageUrl}
                alt={asset.name}
                className="h-42.5 w-full rounded-lg border border-(--color-table-border) object-cover shadow-(--shadow-card)"
              />
            ) : (
              <div className="flex h-42.5 w-full items-center justify-center rounded-lg border border-dashed border-(--color-table-border) bg-(--color-modal-placeholder-bg)">
                <span
                  data-testid="asset-no-image"
                  className="text-xs font-medium text-(--color-modal-label)"
                >
                  No image
                </span>
              </div>
            )}
          </div>
          <div className="flex flex-1 flex-col items-stretch space-y-5">
            <span
              data-testid="asset-status-badge"
              className={[
                'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
                statusClassName,
              ].join(' ')}
            >
              {statusLabel}
            </span>
            <div>
              <p
                data-testid="asset-category-label"
                className="text-sm text-(--color-modal-label)"
              >
                Asset category
              </p>
              <p
                data-testid="asset-category-value"
                className="font-medium text-(--color-text)"
              >
                {asset.categoryName ?? '?'}
              </p>
            </div>
            <div>
              <p
                data-testid="asset-name-label"
                className="text-sm text-(--color-modal-label)"
              >
                Name
              </p>
              <p
                data-testid="asset-name-value"
                className="font-medium text-(--color-text)"
              >
                {asset.name}
              </p>
            </div>
            <div>
              <p className="text-sm text-(--color-modal-label)">Description</p>
              <p
                data-testid="asset-description-value"
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
