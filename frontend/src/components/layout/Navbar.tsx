import { LayoutColumn } from './Layout';
import { NavLink } from 'react-router-dom';
import HomeSharpIcon from '@mui/icons-material/Home';
import MonitorSharpIcon from '@mui/icons-material/Monitor';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarToday';
import PeopleSharpIcon from '@mui/icons-material/People';
import LogoutSharpIcon from '@mui/icons-material/Logout';

export const Navbar: React.FC = () => {
  // Base for links
  const linkBase = 'flex items-center p-3 transition-all border-l-8 w-full';

  // Style when link iz active
  const activeStyle =
    'bg-(--color-bg) border-(--color-primaryblue) text-black shadow-card dark:bg-bg-dark dark:text-white';

  // Style when link is not active
  const inactiveStyle = `
    border-transparent text-black 
    hover:bg-(--color-bg) hover:border-(--color-primaryblue)
    dark:text-white dark:hover:bg-bg-dark dark:hover:border-(--color-primaryblue)
  `;

  return (
    <LayoutColumn
      mdSpan={3}
      className="text-text-light absolute left-0 hidden h-screen min-h-screen w-full flex-col bg-(--color-surface) px-0 pt-20 pb-10 text-2xl leading-11 tracking-[0.2em] shadow-md md:flex md:px-0 lg:px-0 dark:bg-gray-900 dark:text-white dark:shadow-black/20"
    >
      <nav className="flex h-full w-full flex-col justify-between pt-10">
        <div className="flex w-full flex-col gap-4">
          <NavLink
            to="/"
            className={({ isActive }) =>
              `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
            }
          >
            <HomeSharpIcon className="mr-4" />
            Home
          </NavLink>

          <NavLink
            to="/assets"
            className={({ isActive }) =>
              `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
            }
          >
            <MonitorSharpIcon className="mr-4" />
            Assets
          </NavLink>

          <NavLink
            to="/bookings"
            className={({ isActive }) =>
              `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
            }
          >
            <CalendarTodaySharpIcon className="mr-4" />
            Bookings
          </NavLink>

          <NavLink
            to="/users"
            className={({ isActive }) =>
              `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
            }
          >
            <PeopleSharpIcon className="mr-4" />
            Users
          </NavLink>
        </div>

        <NavLink to="/login" className={`${linkBase} ${inactiveStyle}`}>
          <LogoutSharpIcon className="mr-4" />
          Logout
        </NavLink>
      </nav>
    </LayoutColumn>
  );
};
