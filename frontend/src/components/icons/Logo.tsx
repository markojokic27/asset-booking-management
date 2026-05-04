import logoUrl from '../../assets/logo27.png';
import { twMerge } from 'tailwind-merge';
//TODO: triba ga cutat u nekom svg editoru da se dobije vektorska verzija loga, ovo je trenutno raster i lose se skalira
export const Logo: React.FC<React.ComponentPropsWithoutRef<'img'>> = ({
  className,
  ...rest
}) => (
  <img
    src={logoUrl}
    alt="Logo"
    className={twMerge('h-8 w-auto dark:brightness-0 dark:invert', className)}
    {...rest}
  />
);
