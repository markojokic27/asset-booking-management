import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';
import type { AssetDto } from '../types';

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
  if (!isOpen || !asset) return null;

  const statusLabel =
    asset.status === 'AVAILABLE' ? 'Available' : 'Unavailable';
  const statusClassName =
    asset.status === 'AVAILABLE'
      ? 'bg-(--color-status-available-bg) text-(--color-status-available-text)'
      : 'bg-(--color-status-unavailable-bg) text-(--color-status-unavailable-text)';

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
      <div className="w-full max-w-[800px] overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
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
          <div className="flex w-[260px] flex-col items-center justify-center">
            {/* Display asset image here when available */}
            <div className="flex h-[170px] w-full items-center justify-center rounded-lg border border-dashed border-(--color-table-border) bg-(--color-modal-placeholder-bg)">
              <span className="text-xs font-medium text-(--color-modal-label)">
                No image
              </span>
            </div>
          </div>

          <div className="flex flex-1 flex-col items-stretch space-y-5">
            <span
              className={[
                'inline-flex w-fit rounded-full px-3 py-1 text-sm font-medium',
                statusClassName,
              ].join(' ')}
            >
              {statusLabel}
            </span>

            <div>
              <p className="text-sm text-(--color-modal-label)">
                Asset category
              </p>
              <p className="font-medium text-(--color-text)">
                {asset.categoryName ?? '?'}
              </p>
            </div>

            <div>
              <p className="text-sm text-(--color-modal-label)">Name</p>
              <p className="font-medium text-(--color-text)">{asset.name}</p>
            </div>

            <div>
              <p className="text-sm text-(--color-modal-label)">Description</p>
              <p className="text-sm text-(--color-text)">
                {asset.description ?? '?'}
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
