import { twMerge } from 'tailwind-merge';
import { Logo } from '../icons/Logo';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import ThemeToggle from '../ui/ThemeToggle';
import { Layout, LayoutRow, LayoutColumn } from './Layout';
import { Link } from 'react-router-dom';

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
        <div className="flex h-full items-center justify-between px-4 md:px-6">
          <Link to="/">
            <Logo className="h-8 w-auto dark:brightness-0 dark:invert" />
          </Link>
          <div className="flex gap-6">
            <ThemeToggle />
            <LanguageSwitcher />
          </div>
        </div>
      ) : (
        <Layout className="h-full">
          <LayoutRow className="flex h-full items-center">
            <LayoutColumn className="flex items-center justify-between">
              <Link to="/">
                <Logo className="h-8 w-auto dark:brightness-0 dark:invert" />
              </Link>
              <div className="flex gap-6">
                <ThemeToggle />
                <LanguageSwitcher />
              </div>
            </LayoutColumn>
          </LayoutRow>
        </Layout>
      )}
    </div>
  );
};
