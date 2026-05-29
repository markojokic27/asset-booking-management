import * as Dialog from '@radix-ui/react-dialog';
import * as VisuallyHidden from '@radix-ui/react-visually-hidden';
import { NavLink, useNavigate } from 'react-router-dom';
import { Button } from '../ui/Button';
import { Logo } from '../icons/Logo';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import ThemeToggle from '../ui/ThemeToggle';
import { useTranslation } from 'react-i18next';
import {
  MonitorSharp,
  DnsSharp,
  CalendarTodaySharp,
  PeopleSharp,
  LogoutSharp,
  AccountCircleSharp,
  HowToRegSharp,
} from '@mui/icons-material';
import { useCurrentUser } from '../../features/user/hooks/useCurrentUser';
import { getFullName, isAdmin, isManager } from '../../features/user/utilis/users';

export default function MobileMenu() {
  const { t } = useTranslation();
  const { user } = useCurrentUser();
  const links = [
    { to: '/assets', label: t('layout.navbar.assets'), icon: MonitorSharp },
    { to: '/categories', label: t('layout.navbar.categories'), icon: DnsSharp },
    {
      to: '/bookings',
      label: t('layout.navbar.bookings'),
      icon: CalendarTodaySharp,
    },
    ...(isAdmin(user)
      ? [{ to: '/users', label: t('layout.navbar.users'), icon: PeopleSharp }]
      : []),
    ...(isManager(user)
      ? [
          {
            to: '/approvals',
            label: t('layout.navbar.approvals'),
            icon: HowToRegSharp,
          },
        ]
      : []),
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
            <Dialog.Description>
              {t('layout.mobileMenu.description')}
            </Dialog.Description>
          </VisuallyHidden.Root>

          <div className="flex h-20 w-full items-center justify-center px-6">
            <Logo className="mt-2 h-10 w-auto" />
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
          <div className="flex w-full items-center justify-between px-6">
            <LanguageSwitcher variant="mobileMenu" />
            <ThemeToggle />
          </div>
          <div className="mt-auto flex w-full flex-col gap-3 p-6">
            <Dialog.Close asChild>
              <NavLink
                to="/account-info"
                className="flex w-full items-center justify-center gap-3 rounded-lg border border-(--color-table-border) py-3 text-lg font-medium"
              >
                <AccountCircleSharp sx={{ fontSize: 26 }} />
                {user ? (
                  <div className="flex flex-col items-start leading-tight">
                    <div>{getFullName(user)}</div>
                    <div className="text-xs font-normal text-gray-500 dark:text-gray-400">
                      {user.role}
                    </div>
                  </div>
                ) : (
                  t('layout.navbar.account')
                )}
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
