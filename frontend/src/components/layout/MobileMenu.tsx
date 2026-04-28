import * as Dialog from '@radix-ui/react-dialog';
import * as VisuallyHidden from '@radix-ui/react-visually-hidden';
import { NavLink } from 'react-router-dom';
import { Button } from '../ui/Button';
import { Logo } from '../icons/Logo';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import ThemeToggle from '../ui/ThemeToggle';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import {
  MonitorSharp,
  DnsSharp,
  CalendarTodaySharp,
  PeopleSharp,
  LogoutSharp,
  AccountCircleSharp,
} from '@mui/icons-material';

export default function MobileMenu() {
  const { t } = useTranslation();
  const links = [
    { to: '/assets', label: t('layout.navbar.assets'), icon: MonitorSharp },
    { to: '/categories', label: t('layout.navbar.categories'), icon: DnsSharp },
    { to: '/bookings', label: t('layout.navbar.bookings'), icon: CalendarTodaySharp },
    { to: '/users', label: t('layout.navbar.users'), icon: PeopleSharp },
  ];
  const navigate = useNavigate();
  const handleLogout = () => {
    document.cookie = 'auth=; Max-Age=0; path=/';
    navigate('/login');
  };
  return (
    <Dialog.Root>
      <Dialog.Trigger asChild>
        <button className="group relative flex h-10 w-10 cursor-pointer items-center justify-center md:hidden">
          <span className="absolute h-0.5 w-6 -translate-y-2 bg-current transition-all duration-300 ease-in-out group-data-[state=open]:translate-y-0 group-data-[state=open]:rotate-45" />
          <span className="absolute h-0.5 w-6 bg-current opacity-100 transition-all duration-300 ease-in-out group-data-[state=open]:opacity-0" />
          <span className="absolute h-0.5 w-6 translate-y-2 bg-current transition-all duration-300 ease-in-out group-data-[state=open]:translate-y-0 group-data-[state=open]:-rotate-45" />
        </button>
      </Dialog.Trigger>

      <Dialog.Portal>
        <Dialog.Overlay className="fixed inset-0 z-40 bg-black/50 data-[state=closed]:animate-[fadeOut_200ms] data-[state=open]:animate-[fadeIn_200ms]" />

        <Dialog.Content className="fixed top-0 left-0 z-50 flex h-full w-[calc(100vw-80px)] flex-col bg-(--color-surface) shadow-lg data-[state=closed]:animate-[slideOut_300ms_ease-in] data-[state=open]:animate-[slideIn_300ms_ease-out]">
          <VisuallyHidden.Root>
            <Dialog.Title>{t('layout.mobileMenu.title')}</Dialog.Title>
            <Dialog.Description>{t('layout.mobileMenu.description')}</Dialog.Description>
          </VisuallyHidden.Root>

          <div className="flex w-full flex-col items-center gap-3 px-6 py-4">
            <Logo className='scale-200'/>
            <div className="flex w-full items-center justify-center gap-4">
              <LanguageSwitcher />
              <ThemeToggle />
            </div>
          </div>
          <nav className="flex-1 overflow-y-auto overscroll-contain">
            {links.map(({ to, label, icon: Icon }) => (
              <Dialog.Close asChild key={label}>
                <NavLink
                  to={to}
                  className={'flex items-center gap-3 p-6 text-2xl'}
                >
                  <Icon />
                  {label}
                </NavLink>
              </Dialog.Close>
            ))}
          </nav>

          <div className="mt-auto flex w-full flex-col gap-3 p-6">
            <Dialog.Close asChild>
              <NavLink
                to="/account-info"
                className="flex w-full items-center justify-center gap-3 rounded-lg border border-(--color-table-border) py-3 text-lg font-medium"
              >
                <AccountCircleSharp sx={{ fontSize: 26 }} />
                {t('layout.navbar.account')}
              </NavLink>
            </Dialog.Close>

            <Dialog.Close asChild>
              <Button
                onClick={handleLogout}
                className="w-full border-none bg-red-500 hover:bg-red-600"
              >
                <LogoutSharp />
                {t('layout.navbar.logout')}
              </Button>
            </Dialog.Close>
          </div>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  );
}
