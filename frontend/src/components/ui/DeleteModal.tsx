import { Button } from './Button';
import { useTranslation } from 'react-i18next';

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
  title,
  description,
}: DeleteModalProps<T>) {
  const { t } = useTranslation();
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
          {title ?? t('ui.deleteModal.defaultTitle')}
        </h2>

        <p className="mt-3 text-sm text-(--color-modal-label)">
          {description ??
            t('ui.deleteModal.defaultDescription', { name: getItemName(item) })}
        </p>

        <div className="mt-6 flex justify-end gap-3">
          <Button type="button" variant="secondary" onClick={onClose}>
            {t('ui.deleteModal.cancel')}
          </Button>

          <Button type="button" variant="danger" onClick={onConfirm}>
            {t('ui.deleteModal.confirmDelete')}
          </Button>
        </div>
      </div>
    </div>
  );
}