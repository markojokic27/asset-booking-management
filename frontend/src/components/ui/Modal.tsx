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
  if (!isOpen) return null;

  const resolvedAriaLabel =
    ariaLabel ??
    (typeof title === 'string' ? title : undefined) ??
    t('ui.modal.dialogAria');

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center p-6"
      role="dialog"
      aria-modal="true"
      aria-label={resolvedAriaLabel}
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
          'relative z-10 w-full overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)',
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

        <div className="px-8 py-8">{children}</div>

        {footer != null && (
          <>
            <div className="mx-8 h-px bg-(--color-table-border)" />
            <div className="px-8 py-5">{footer}</div>
          </>
        )}
      </div>
    </div>
  );
};
