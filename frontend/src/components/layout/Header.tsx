import { Logo } from '../icons/Logo';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import ThemeToggle from '../ui/ThemeToggle';
import { Layout, LayoutRow, LayoutColumn } from './Layout';

export const Header: React.FC = () => {
  return (
    <div className="fixed top-0 z-40 mx-auto h-20 w-full bg-gray-100 shadow-md dark:bg-gray-900 dark:shadow-black/20">
      <Layout className="h-full">
        <LayoutRow className="flex h-full items-center">
          <LayoutColumn className="flex items-center justify-between">
            <Logo className="h-8 w-auto" />
            <div className="flex gap-4">
              <ThemeToggle />
              <LanguageSwitcher />
            </div>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </div>
  );
};
