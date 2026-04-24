import { Button } from './Button';

type DeleteModalProps<T> = {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  item: T | null;
  getItemName: (item: T) => string;
  title?: string;
  description?: string;
};

export function DeleteModal<T>({
  isOpen,
  onClose,
  onConfirm,
  item,
  getItemName,
  title = 'Delete item?',
  description,
}: DeleteModalProps<T>) {
  if (!isOpen || !item) return null;

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-md rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) p-6 shadow-(--shadow-card)">
        <h2 className="text-xl font-bold text-(--color-text)">
          {title}
        </h2>

        <p className="mt-3 text-sm text-(--color-modal-label)">
          {description ??
            `Are you sure you want to delete "${getItemName(item)}"?`}
        </p>

        <div className="mt-6 flex justify-end gap-3">
          <Button type="button" onClick={onClose}>
            Cancel
          </Button>

          <Button type="button" onClick={onConfirm}>
            Delete
          </Button>
        </div>
      </div>
    </div>
  );
}