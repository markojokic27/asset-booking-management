import * as React from 'react';
import { twMerge } from 'tailwind-merge';

type FormInputProps = Omit<React.InputHTMLAttributes<HTMLInputElement>, 'size'> & {
  label?: string;
  size?: 'sm' | 'md';
  error?: boolean;
  errorMessage?: string;
};

export const formFieldLabelClassName =
  'mb-2 block text-[10px] font-semibold uppercase tracking-[0.22em] text-(--color-table-head-text) opacity-60';

const fieldClassName =
  'w-full rounded-lg border border-(--color-table-border) bg-(--color-table-surface) px-4 py-3 text-sm font-medium text-(--color-text) shadow-(--shadow-card) outline-none transition duration-100 focus:border-(--color-primaryblue) focus:bg-(--color-surface-hover)';

export const FormInput = React.forwardRef<HTMLInputElement, FormInputProps>(
  (
    { id, label, size = 'md', error = false, errorMessage, className, ...props },
    ref
  ) => {
    return (
      <div className="w-full">
        {label && (
          <label htmlFor={id} className={formFieldLabelClassName}>
            {label}
          </label>
        )}
        <input
          ref={ref}
          id={id}
          aria-invalid={error || undefined}
          className={twMerge(
            fieldClassName,
            size === 'sm' && 'px-3 py-2 text-xs',
            size === 'md' && 'px-4 py-3 text-sm',
            error && 'border-red-500 focus:border-red-500',
            className
          )}
          {...props}
        />
        {error && errorMessage && (
          <p className="mt-2 text-sm text-red-500">{errorMessage}</p>
        )}
      </div>
    );
  }
);

FormInput.displayName = 'FormInput';
