import * as React from 'react';
import { twMerge } from 'tailwind-merge';

type InputProps = React.InputHTMLAttributes<HTMLInputElement> & {
  size?: 'sm' | 'md';
  error?: boolean;
  errorMessage?: string;
};

export const Input = React.forwardRef<HTMLInputElement, InputProps>(
  (
    { size = 'md', error = false, errorMessage, className, disabled, ...props },
    ref
  ) => {
    return (
      <div className="w-full">
        <input
          ref={ref}
          disabled={disabled}
          aria-invalid={error || undefined}
          className={twMerge(
            // base
            'w-full rounded-xl border bg-white leading-none transition-colors outline-none focus:border-blue-500 focus:ring-1',

            // size
            size === 'sm' && 'px-3 py-2 text-xs',
            size === 'md' && 'px-4 py-3 text-sm',

            // border color
            error
              ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
              : 'border-blue-500',

            // disabled
            disabled && 'cursor-not-allowed opacity-50',

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

Input.displayName = 'Input';
