// External packages
import { useEffect, useState } from 'react';
import { NavLink } from 'react-router-dom';
import MonitorSharpIcon from '@mui/icons-material/MonitorSharp';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import PeopleSharpIcon from '@mui/icons-material/PeopleSharp';
import LogoutSharpIcon from '@mui/icons-material/LogoutSharp';
import DnsSharpIcon from '@mui/icons-material/DnsSharp';
import AssessmentSharpIcon from '@mui/icons-material/AssessmentSharp';
import HowToRegSharpIcon from '@mui/icons-material/HowToRegSharp';
import EventNoteSharpIcon from '@mui/icons-material/EventNoteSharp';
import { AccountCircleSharp } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';

// Components
import { LayoutColumn } from './Layout';

// Types
import type { UserDto } from '../../features/user/types';

// API
import { useAuth } from '../../features/auth/context/AuthContext';
import { getUserById } from '../../features/user/api/users';
import { getFullName, isAdmin, isEmployee, isManager } from '../../features/user/utils/users';

export const Navbar: React.FC = () => {
  const { t } = useTranslation();
  const { user } = useAuth();

  const [userDto, setUserDto] = useState<UserDto | undefined>();

  useEffect(() => {
    if (!user) return;

    getUserById(user.id).then(setUserDto).catch(console.error);
  }, [user]);

  const navItems = [
    ...(user && !isEmployee(user)
      ? [
          { to: '/assets', label: t('layout.navbar.assets'), icon: MonitorSharpIcon },
          {
            to: '/categories',
            label: t('layout.navbar.categories'),
            icon: DnsSharpIcon,
          },
        ]
      : []),
    {
      to: '/bookings',
      label: t('layout.navbar.bookings'),
      icon: CalendarTodaySharpIcon,
    },
    {
      to: '/my-bookings',
      label: isAdmin(user)
        ? t('layout.navbar.allBookings')
        : t('layout.navbar.myBookings'),
      icon: EventNoteSharpIcon,
    },
    ...(isAdmin(user)
      ? [
          {
            to: '/users',
            label: t('layout.navbar.users'),
            icon: PeopleSharpIcon,
          },
        ]
      : []),
    {
      to: '/report',
      label: t('layout.navbar.report'),
      icon: AssessmentSharpIcon,
    },
    ...(isManager(user)
      ? [
          {
            to: '/approvals',
            label: t('layout.navbar.approvals'),
            icon: HowToRegSharpIcon,
          },
        ]
      : []),
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
      className="text-text-light fixed left-0 z-20 hidden h-screen min-h-screen w-full flex-col bg-(--color-surface) px-0 pt-20 pb-10 text-base leading-11 tracking-[0.2em] shadow-md sm:text-lg sm:tracking-widest md:flex md:w-50 md:max-w-75 md:px-0 md:text-xl md:tracking-[0.15em] lg:px-0 lg:text-2xl dark:text-white dark:shadow-black/20"
    >
      <nav className="flex h-full w-full flex-col overflow-y-auto overscroll-contain pt-10">
        <div className="flex w-full shrink-0 flex-col gap-4">
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

        <div className="mt-auto flex w-full shrink-0 flex-col gap-4 pt-4">
          <NavLink
            to="/account-info"
            className={({ isActive }) => getLinkClass(isActive)}
          >
            <AccountCircleSharp className="mr-3" sx={{ fontSize: 26 }} />
            {user ? (
              <div className="flex flex-col leading-tight">
                <div className="tracking-normal">{getFullName(user)}</div>
                <div className="text-xs tracking-normal text-gray-500 dark:text-gray-400">
                  {userDto?.role}
                </div>
              </div>
            ) : (
              t('layout.navbar.account')
            )}
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
