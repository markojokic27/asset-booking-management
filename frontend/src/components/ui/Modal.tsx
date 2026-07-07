import * as React from 'react';
import { twMerge } from 'tailwind-merge';
import { useTranslation } from 'react-i18next';

export type ModalSize = 'md' | 'lg';

export type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  ariaLabel?: string;
  title?: React.ReactNode;
  size?: ModalSize;
  children: React.ReactNode;
  footer?: React.ReactNode;
  headerRight?: React.ReactNode;
  className?: string;
  testId?: string;
};

const sizeClassName: Record<ModalSize, string> = {
  md: 'max-w-200',
  lg: 'max-w-4xl',
};

export const Modal: React.FC<ModalProps> = ({
  isOpen,
  onClose,
  ariaLabel,
  title,
  size = 'md',
  children,
  footer,
  headerRight,
  className,
  testId,
}) => {
  const { t } = useTranslation();
  const dialogRef = React.useRef<HTMLDialogElement>(null);

  React.useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;

    if (isOpen) {
      if (!dialog.open) dialog.showModal();
    } else if (dialog.open) {
      dialog.close();
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const resolvedAriaLabel =
    ariaLabel ??
    (typeof title === 'string' ? title : undefined) ??
    t('ui.modal.dialogAria');

  return (
    <dialog
      ref={dialogRef}
      className="fixed inset-0 z-50 m-0 flex h-full max-h-full w-full max-w-full items-center justify-center border-0 bg-transparent p-6 backdrop:bg-transparent"
      aria-label={resolvedAriaLabel}
      onCancel={(e) => {
        e.preventDefault();
        onClose();
      }}
    >
      <button
        type="button"
        className="fixed inset-0 cursor-default bg-(--color-modal-overlay)"
        aria-label={t('ui.modal.closeAria')}
        onClick={onClose}
      />
      <div
        data-testid={testId ?? 'modal-dialog'}
        className={twMerge(
          'relative z-10 flex max-h-[95vh] w-full flex-col overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)',
          sizeClassName[size],
          className
        )}
      >
        {(title != null || headerRight != null) && (
          <div className="flex items-center justify-between gap-4 px-8 pt-6 pb-4">
            <div className="w-full min-w-0">{title}</div>
            {headerRight}
          </div>
        )}

        <div className="mx-8 h-px bg-(--color-table-border)" />

        <div className="min-h-0 flex-1 overflow-hidden px-8 py-8">
          {children}
        </div>

        {footer != null && (
          <>
            <div className="mx-8 h-px bg-(--color-table-border)" />
            <div className="px-8 py-5">{footer}</div>
          </>
        )}
      </div>
    </dialog>
  );
};
