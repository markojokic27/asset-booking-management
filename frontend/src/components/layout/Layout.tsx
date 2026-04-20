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
    className={twMerge('container mx-auto flex-1 px-4 md:px-6', className)}
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

const buildClasses = (
  span?: number,
  offset?: number,
  breakpoint?: 'sm' | 'md' | 'lg' | 'xl'
) => {
  const classes: string[] = [];

  if (span !== undefined) {
    classes.push(
      breakpoint ? `${breakpoint}:w-column-${span}` : `w-column-${span}`
    );
  }

  if (offset !== undefined) {
    classes.push(
      breakpoint ? `${breakpoint}:offset-${offset}` : `offset-${offset}`
    );
  }

  return classes.join(' ');
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
  const baseClasses = buildClasses(span, offset);
  const smClasses = buildClasses(smSpan, smOffset, 'sm');
  const mdClasses = buildClasses(mdSpan, mdOffset, 'md');
  const lgClasses = buildClasses(lgSpan, lgOffset, 'lg');
  const xlClasses = buildClasses(xlSpan, xlOffset, 'xl');
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
