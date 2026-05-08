import * as React from 'react';
import { twMerge } from 'tailwind-merge';

export type FormDropdownOption = {
  value: string | number;
  label: string;
};

type FormDropdownProps = React.SelectHTMLAttributes<HTMLSelectElement> & {
  label?: string;
  error?: boolean;
  errorMessage?: string;
  options: readonly FormDropdownOption[];
};

const fieldLabelClassName =
  'mb-2 block text-[10px] font-semibold uppercase tracking-[0.22em] text-(--color-table-head-text) opacity-60';

const fieldClassName =
  'w-full rounded-lg border border-(--color-table-border) bg-(--color-table-surface) px-4 py-3 text-sm font-medium text-(--color-text) shadow-(--shadow-card) outline-none transition duration-100 focus:border-(--color-primaryblue) focus:bg-(--color-surface-hover)';

export const FormDropdown = React.forwardRef<
  HTMLSelectElement,
  FormDropdownProps
>(
  (
    { id, label, error = false, errorMessage, className, options, ...props },
    ref
  ) => {
    return (
      <div className="w-full">
        {label && (
          <label htmlFor={id} className={fieldLabelClassName}>
            {label}
          </label>
        )}
        <select
          ref={ref}
          id={id}
          aria-invalid={error || undefined}
          className={twMerge(
            fieldClassName,
            error && 'border-red-500 focus:border-red-500',
            className
          )}
          {...props}
        >
          {options.map((option) => (
            <option key={String(option.value)} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
        {error && errorMessage && (
          <p className="mt-2 text-sm text-red-500">{errorMessage}</p>
        )}
      </div>
    );
  }
);

FormDropdown.displayName = 'FormDropdown';
