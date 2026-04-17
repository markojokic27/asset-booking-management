import logoUrl from '../../assets/logo.png';
import { twMerge } from 'tailwind-merge';

export const Logo: React.FC<React.ComponentPropsWithoutRef<'svg'>> = ({
  ...rest
}) => (
  <svg
    {...rest}
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 93 32"
    className={twMerge('h-8 w-auto dark:brightness-0 dark:invert', rest.className)}
  >
    <image href={logoUrl} width="93" height="32" x="0" />
  </svg>
);
