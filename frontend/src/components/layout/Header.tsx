import { twMerge } from 'tailwind-merge';
import { Logo } from '../icons/Logo';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import ThemeToggle from '../ui/ThemeToggle';
import { Layout, LayoutRow, LayoutColumn } from './Layout';
import { Link } from 'react-router-dom';
import MobileMenu from './MobileMenu';

interface HeaderProps {
  className?: string;
  variant?: 'public' | 'app';
}

export const Header: React.FC<HeaderProps> = ({
  className,
  variant = 'public',
}) => {
  return (
    <div
      className={twMerge(
        'fixed top-0 z-40 mx-auto h-20 w-full bg-(--color-surface) shadow-md',
        className
      )}
    >
      {/* Keep the public (auth) header centered, but align the app header with the left sidebar layout after login. */}
      {variant === 'app' ? (
        <div className="flex h-full items-center justify-between pl-0 pr-4 md:pr-6">
          <Link to="/" className="-ml-4 block">
            <Logo className="h-20 w-auto" />
          </Link>
          <div className="hidden gap-6 md:flex">
            <ThemeToggle />
            <LanguageSwitcher />
          </div>
          <MobileMenu />
        </div>
      ) : (
        <Layout className="h-full">
          <LayoutRow className="flex h-full items-center">
            <LayoutColumn className="flex items-center justify-between">
              <Link to="/">
                <Logo className="-ml-10 h-20 w-auto dark:brightness-0 dark:invert" />
              </Link>
              <div className="hidden gap-6 md:flex">
                <ThemeToggle />
                <LanguageSwitcher />
              </div>
              <MobileMenu />
            </LayoutColumn>
          </LayoutRow>
        </Layout>
      )}
    </div>
  );
};
