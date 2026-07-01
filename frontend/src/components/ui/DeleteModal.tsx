import { useEffect } from 'react';
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
}: Readonly<DeleteModalProps<T>>) {
  const { t } = useTranslation();

  useEffect(() => {
    if (!isOpen || !item) return;
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, item, onClose]);

  useEffect(() => {
    if (!isOpen || !item) return;
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, item, onClose]);

  if (!isOpen || !item) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
      <button
        type="button"
        aria-label={t('ui.deleteModal.closeDialog')}
        className="absolute inset-0 z-0 cursor-default border-0 bg-(--color-modal-overlay) p-0"
        onClick={onClose}
      />
      <div className="relative z-10 w-full max-w-md rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) p-6 shadow-(--shadow-card)">
        <div className="fixed inset-0 z-50 flex items-center justify-center p-6">
          <button
            type="button"
            aria-label={t('ui.deleteModal.closeDialog')}
            className="absolute inset-0 z-0 cursor-default border-0 bg-(--color-modal-overlay) p-0"
            onClick={onClose}
          />
          <div className="relative z-10 w-full max-w-md rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) p-6 shadow-(--shadow-card)">
            <h2 className="text-2xl font-bold text-(--color-text)">
              {title ?? t('ui.deleteModal.defaultTitle')}
            </h2>

            <p className="mt-3 text-sm text-(--color-modal-label)">
              {description ??
                t('ui.deleteModal.defaultDescription', {
                  name: getItemName(item),
                })}
            </p>

            <div className="mt-6 flex justify-end gap-3">
              <Button
                data-testid="cancel-delete-button"
                type="button"
                variant="secondary"
                onClick={onClose}
              >
                {t('ui.deleteModal.cancel')}
              </Button>

              <Button
                data-testid="confirm-delete-button"
                type="button"
                variant="danger"
                onClick={onConfirm}
              >
                {t('ui.deleteModal.confirmDelete')}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
