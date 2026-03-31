import { Logo } from '../icons/Logo';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import ThemeToggle from '../ui/ThemeToggle';
import { Layout, LayoutRow, LayoutColumn } from './Layout';
import { Link } from 'react-router-dom';

export const Header: React.FC = () => {
  return (
    <div className="fixed top-0 z-40 mx-auto h-20 w-full bg-(--color-surface) shadow-md">
      <Layout className="h-full">
        <LayoutRow className="flex h-full items-center">
          <LayoutColumn className="flex items-center justify-between">
            <Link to="/">
              <Logo className="h-8 w-auto" />
            </Link>
            <div className="flex gap-6">
              <ThemeToggle />
              <LanguageSwitcher />
            </div>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </div>
  );
};
