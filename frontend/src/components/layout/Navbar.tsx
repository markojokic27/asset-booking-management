import { LayoutColumn } from './Layout';
import { NavLink } from 'react-router-dom';
import MonitorSharpIcon from '@mui/icons-material/MonitorSharp';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import PeopleSharpIcon from '@mui/icons-material/PeopleSharp';
import LogoutSharpIcon from '@mui/icons-material/LogoutSharp';
import DnsSharpIcon from '@mui/icons-material/DnsSharp';
import { AccountCircleSharp } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';

export const Navbar: React.FC = () => {
  const { t } = useTranslation();
  const navItems = [
    { to: '/assets', label: t('layout.navbar.assets'), icon: MonitorSharpIcon },
    { to: '/categories', label: t('layout.navbar.categories'), icon: DnsSharpIcon },
    { to: '/bookings', label: t('layout.navbar.bookings'), icon: CalendarTodaySharpIcon },
    { to: '/users', label: t('layout.navbar.users'), icon: PeopleSharpIcon },
  ];
  //TODO hover, new tab
  // Base for links
  const linkBase =
    'flex w-full hover:cursor-pointer items-center p-3 transition-all border-l-8 w-full';

  // Style when link iz active
  const activeStyle =
    'bg-(--color-bg)  border-(--color-primaryblue) text-black shadow-card dark:bg-bg-dark dark:text-white';

  // Style when link is not active
  const inactiveStyle = `
    border-transparent text-black 
    hover:bg-(--color-bg) hover:border-(--color-primaryblue)
    dark:text-white dark:hover:bg-bg-dark dark:hover:border-(--color-primaryblue)
  `;

  const getLinkClass = (isActive: boolean) =>
    `${linkBase} ${isActive ? activeStyle : inactiveStyle}`;

  return (
    <LayoutColumn
      mdSpan={3}
      className="text-text-light fixed left-0 z-20 hidden h-screen min-h-screen w-full flex-col bg-(--color-surface) px-0 pt-20 pb-10 text-base leading-11 tracking-[0.2em] shadow-md sm:text-lg sm:tracking-widest md:flex md:px-0 md:text-xl md:tracking-[0.15em] lg:px-0 lg:text-2xl dark:text-white dark:shadow-black/20"
    >
      <nav className="flex h-full w-full flex-col justify-between overflow-hidden pt-10">
        <div className="flex w-full flex-col gap-4">
          {navItems.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              className={({ isActive }) => getLinkClass(isActive)}
            >
              <Icon className="mr-4" />
              {label}
            </NavLink>
          ))}
        </div>

        <div className="flex w-full flex-col gap-4">
          <NavLink to="/account-info" className={({ isActive }) => getLinkClass(isActive)}>
            <AccountCircleSharp className='mr-3' sx={{ fontSize: 26 }} />
            {t('layout.navbar.account')}
          </NavLink>
          <NavLink to="/login" className={getLinkClass(false)}>
            <LogoutSharpIcon className="mr-4" />
            {t('layout.navbar.logout')}
          </NavLink>
        </div>
      </nav>
    </LayoutColumn>
  );
};
