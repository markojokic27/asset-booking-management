import * as React from 'react';
import { twMerge } from 'tailwind-merge';

export type ButtonOwnProps = {
  variant?: 'solid' | 'outline' | 'dark' | 'link';
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
          'items-center black inline-flex justify-center gap-2 border leading-none shadow-(--shadow-button) transition-colors outline-none hover:cursor-pointer active:scale-96',

          // variants
          variant === 'solid' &&
            'border-blue-500 bg-blue-500 text-white hover:border-blue-600 hover:bg-blue-600',
          variant === 'outline' &&
            'border-blue-500 text-blue-500 hover:border-blue-600 hover:text-blue-600',
          variant === 'dark' &&
            'hover:border-grayscale-200 hover:text-grayscale-200 border-white text-white',
          variant === 'link' &&
            'border-0 bg-transparent p-0 underline underline-offset-4 shadow-none hover:no-underline',

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
