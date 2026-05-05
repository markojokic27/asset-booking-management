import * as React from 'react';
import { twMerge } from 'tailwind-merge';

export type ButtonOwnProps = {
  variant?: 'solid' | 'outline' | 'dark' | 'link' | 'danger' | 'secondary';
  size?: 'sm' | 'md';
  isVisuallyDisabled?: boolean;
  iconLeft?: React.ReactNode;
  iconRight?: React.ReactNode;
};

export type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> &
  ButtonOwnProps;

export const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      variant = 'solid',
      size = 'md',
      isVisuallyDisabled = false,
      iconLeft,
      iconRight,
      className,
      children,
      disabled,
      type = 'button',
      ...rest
    },
    ref
  ) => {
    const isDisabled = disabled || isVisuallyDisabled;

    return (
      <button
        ref={ref}
        type={type}
        disabled={disabled}
        aria-disabled={isDisabled}
        className={twMerge(
          // base
          'inline-flex items-center justify-center gap-2 rounded-lg border leading-none transition-colors outline-none hover:cursor-pointer active:scale-96',

          // variants
          variant === 'solid' &&
            'border-(--color-primaryblue) bg-(--color-primaryblue) text-white hover:border-blue-600 hover:bg-blue-600',
          variant === 'outline' &&
            'border-black bg-white text-black hover:border-(--color-primaryblue) hover:text-(--color-primaryblue) dark:border-white dark:bg-transparent dark:text-white dark:hover:border-(--color-primaryblue)',
          variant === 'dark' &&
            'hover:border-grayscale-200 hover:text-grayscale-200 border-white text-white',
          variant === 'link' &&
            'border-0 bg-transparent p-0 underline underline-offset-4 shadow-none hover:no-underline',
          variant === 'danger' &&
            'border-red-600 bg-red-600 text-white hover:border-red-700 hover:bg-red-700',
          variant === 'secondary' &&
            'border-gray-300 bg-gray-200 text-gray-700 hover:border-gray-400 hover:bg-gray-300',

          // sizes
          size === 'sm' && 'px-4 py-3',
          size === 'md' && 'px-6 py-4',

          // disabled (visual + functional)
          isDisabled && 'pointer-events-none cursor-default opacity-50',

          className
        )}
        {...rest}
      >
        {iconLeft}
        {children}
        {iconRight}
      </button>
    );
  }
);

Button.displayName = 'Button';
