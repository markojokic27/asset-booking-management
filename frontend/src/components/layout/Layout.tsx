// External packages
import React from 'react';
import { twMerge } from 'tailwind-merge';

export const Layout: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  children,
  ...rest
}) => (
  <div
    {...rest}
    className={twMerge('container mx-auto px-4 md:px-6', className)}
  >
    {children}
  </div>
);

export const LayoutRow: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  children,
  ...rest
}) => (
  <div
    {...rest}
    className={twMerge('-mx-1 flex flex-wrap md:-mx-4 lg:-mx-6', className)}
  >
    {children}
  </div>
);

type LayoutColumnProps = React.ComponentPropsWithoutRef<'div'> & {
  span?: number;
  offset?: number;
  smSpan?: number;
  mdSpan?: number;
  lgSpan?: number;
  xlSpan?: number;
  smOffset?: number;
  mdOffset?: number;
  lgOffset?: number;
  xlOffset?: number;
};

const spanClasses = (span: number, prefix: string = '') => {
  return prefix ? `${prefix}:w-column-${span}` : `w-column-${span}`;
};

const offsetClasses = (offset: number, prefix: string = '') => {
  return prefix ? `${prefix}:offset-${offset}` : `offset-${offset}`;
};

export const LayoutColumn: React.FC<LayoutColumnProps> = ({
  span = 12,
  offset = 0,
  smSpan,
  mdSpan,
  lgSpan,
  xlSpan,
  smOffset,
  mdOffset,
  lgOffset,
  xlOffset,
  children,
  className,
  ...rest
}) => {
  const baseClasses = `${spanClasses(span)} ${offsetClasses(offset)}`;
  const smClasses =
    smSpan || smOffset
      ? `${smSpan ? spanClasses(smSpan, 'sm') : ''} ${smOffset ? offsetClasses(smOffset, 'sm') : ''}`
      : '';
  const mdClasses =
    mdSpan || mdOffset
      ? `${mdSpan ? spanClasses(mdSpan, 'md') : ''} ${mdOffset ? offsetClasses(mdOffset, 'md') : ''}`
      : '';
  const lgClasses =
    lgSpan || lgOffset
      ? `${lgSpan ? spanClasses(lgSpan, 'lg') : ''} ${lgOffset ? offsetClasses(lgOffset, 'lg') : ''}`
      : '';
  const xlClasses =
    xlSpan || xlOffset
      ? `${xlSpan ? spanClasses(xlSpan, 'xl') : ''} ${xlOffset ? offsetClasses(xlOffset, 'xl') : ''}`
      : '';
  return (
    <div
      {...rest}
      className={twMerge(
        `relative px-1 md:px-4 lg:px-6 ${baseClasses} ${smClasses} ${mdClasses} ${lgClasses} ${xlClasses}`,
        className
      )}
    >
      {children}
    </div>
  );
};
