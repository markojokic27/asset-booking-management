import { LayoutColumn } from './Layout';
import { NavLink } from 'react-router-dom';
import HomeSharpIcon from '@mui/icons-material/Home';
import MonitorSharpIcon from '@mui/icons-material/Monitor';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarToday';
import PeopleSharpIcon from '@mui/icons-material/People';
import LogoutSharpIcon from '@mui/icons-material/Logout';

export const Navbar: React.FC = () => {
  const navItems = [
    { to: '/', label: 'Home', icon: HomeSharpIcon },
    { to: '/assets', label: 'Assets', icon: MonitorSharpIcon },
    { to: '/bookings', label: 'Bookings', icon: CalendarTodaySharpIcon },
    { to: '/users', label: 'Users', icon: PeopleSharpIcon },
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
      className="text-light absolute left-0 hidden h-screen min-h-screen w-full flex-col bg-(--color-surface) px-0 pt-20 pb-10 text-2xl leading-11 tracking-[0.2em] shadow-md md:flex md:px-0 lg:px-0 dark:bg-gray-900 dark:text-white dark:shadow-black/20"
    >
      <nav className="flex h-full w-full flex-col justify-between pt-10">
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

        <NavLink to="/login" className={`${linkBase} ${inactiveStyle}`}>
          <LogoutSharpIcon className="mr-4" />
          Logout
        </NavLink>
      </nav>
    </LayoutColumn>
  );
};
