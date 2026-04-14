import * as React from 'react';
import { twMerge } from 'tailwind-merge';

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
}) => {
  if (!isOpen) return null;

  const resolvedAriaLabel =
    ariaLabel ?? (typeof title === 'string' ? title : undefined) ?? 'Dialog';

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label={resolvedAriaLabel}
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div
        className={twMerge(
          'w-full overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)',
          sizeClassName[size],
          className
        )}
      >
        {(title != null || headerRight != null) && (
          <div className="flex items-center justify-between gap-4 px-8 pt-6 pb-4">
            <div className="min-w-0">{title}</div>
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
