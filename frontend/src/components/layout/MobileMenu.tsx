import * as Dialog from '@radix-ui/react-dialog';
import * as VisuallyHidden from '@radix-ui/react-visually-hidden';
import { NavLink } from 'react-router-dom';
import { Button } from '../ui/Button';
import { Logo } from '../icons/Logo';
import LanguageSwitcher from '../ui/LanguageSwitcher';
import ThemeToggle from '../ui/ThemeToggle';
import { useNavigate } from 'react-router-dom';
import {
  HomeSharp,
  MonitorSharp,
  CalendarTodaySharp,
  PeopleSharp,
  LogoutSharp,
  AccountCircleSharp
} from '@mui/icons-material';

export default function MobileMenu() {
  const links = [
    { to: '/', label: 'Home', icon: HomeSharp },
    { to: '/assets', label: 'Assets', icon: MonitorSharp },
    { to: '/bookings', label: 'Bookings', icon: CalendarTodaySharp },
    { to: '/users', label: 'Users', icon: PeopleSharp },
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
            <Dialog.Title>Navigation menu</Dialog.Title>
            <Dialog.Description>Main navigation</Dialog.Description>
          </VisuallyHidden.Root>

          <div className="flex h-20 w-full items-center justify-center">
            <Logo />
          </div>
          <nav>
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

          <div className="flex w-full flex-wrap justify-between p-6">
            <LanguageSwitcher />
            <ThemeToggle />
          </div>
          <div className="mt-auto flex w-full flex-col gap-3 p-6">
            <Dialog.Close asChild>
              <NavLink
                to="/account-info"
                className="flex w-full items-center justify-center gap-3 rounded-lg border border-(--color-table-border) py-3 text-lg font-medium"
              >
                <AccountCircleSharp sx={{ fontSize: 26}}/>
                Account
              </NavLink>
            </Dialog.Close>

            <Dialog.Close asChild>
              <Button
                onClick={handleLogout}
                className="w-full border-none bg-red-500 hover:bg-red-600"
              >
                <LogoutSharp />
                Logout
              </Button>
            </Dialog.Close>
          </div>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  );
}
