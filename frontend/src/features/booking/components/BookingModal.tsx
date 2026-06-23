// External packages
import { useState } from 'react';

// Components
import { Button } from '../../../components/ui/Button';
import { Modal } from '../../../components/ui/Modal';

// Types
import type { Filters } from '../types';
import type { AssetDto } from '../../asset/types';

type BookingModalProps = {
  open: boolean;
  onClose: () => void;
  asset?: AssetDto | null;
  filters: Filters;
};

export function BookingModal({
  open,
  onClose,
  asset,
  filters,
}: BookingModalProps) {
  const selectedFrom = `${filters.fromDate || '-'} ${filters.fromHour || ''}`;
  const selectedTo = `${filters.toDate || '-'} ${filters.toHour || ''}`;

  if (!open || !asset) return null;

  return (
    <Modal
      isOpen={true}
      onClose={onClose}
      title={<h2 className="text-xl font-semibold">Book {asset.name}</h2>}
    >
      <div className="mb-5 text-sm">
        <p className="font-semibold">{asset.name}</p>
        <p>Model: {asset.name ?? '-'}</p>
        <p>Location: {asset.location ?? '-'}</p>
      </div>

      <div className="space-y-6">
        <div className="flex items-start gap-3 text-sm">
          <span className="bg---color-status-active-bg flex h-5 w-5 items-center justify-center text-(--color-status-active-text)">
            ✓
          </span>

          <p>
            {asset.name} is
            <span
              data-testid="availability-badge"
              className="rounded bg-(--color-status-active-bg) px-2 py-0.5 text-(--color-status-active-text)"
            >
              available
            </span>
            from {selectedFrom} to {selectedTo}
          </p>
        </div>
      </div>

      <div className="mt-10 flex justify-end gap-4">
        <Button
          data-testid="cancel-button"
          variant="outline"
          size="md"
          onClick={onClose}
        >
          Cancel
        </Button>

        <Button
          data-testid="book-now-button"
          variant="solid"
          size="md"
          onClick={() => {
            console.log('BOOK', {
              asset,
              from: filters.fromDate,
              to: filters.toDate,
            });
          }}
        >
          Book now
        </Button>
      </div>
    </Modal>
  );
}
