import * as React from 'react';
import { twMerge } from 'tailwind-merge';

type InputProps = Omit<React.InputHTMLAttributes<HTMLInputElement>, 'size'> & {
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
            'relative w-full rounded-lg border-none bg-white leading-none tracking-normal text-gray-900 shadow-(--shadow-button) transition-colors outline-none placeholder:tracking-[0.2em] focus:border-black focus:ring-1 dark:bg-gray-900 dark:text-gray-100 dark:placeholder:text-gray-400',

            // sizeborder
            size === 'sm' && 'px-3 py-2 text-xs',
            size === 'md' && 'px-4 py-3 text-sm',

            // border color
            error
              ? 'border-red-500 focus:border-red-500 focus:ring-red-500'
              : '',

            // disabled
            disabled && 'cursor-not-allowed opacity-50',

            className
          )}
          {...props}
        />

        {error && errorMessage && (
          <p className="absolute mt-2 text-sm font-semibold tracking-normal text-red-500">
            {errorMessage}
          </p>
        )}
      </div>
    );
  }
);

Input.displayName = 'Input';
