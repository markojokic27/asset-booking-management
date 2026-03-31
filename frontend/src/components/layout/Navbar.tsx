import { LayoutColumn } from './Layout';
import { NavLink } from 'react-router-dom';
import HomeSharpIcon from '@mui/icons-material/HomeSharp';
import MonitorSharpIcon from '@mui/icons-material/MonitorSharp';
import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import PeopleSharpIcon from '@mui/icons-material/PeopleSharp';
import LogoutSharpIcon from '@mui/icons-material/LogoutSharp';



export const Navbar: React.FC = () => {
  // Base for links
  const linkBase = "flex items-center p-3 transition-all border-l-8 w-full";

  // Style when link iz active
  const activeStyle = "bg-[var(--color-bg)] border-[var(--color-primaryblue)] text-black shadow-card dark:bg-bg-dark dark:text-white";

  // Style when link is not active
  const inactiveStyle = `
    border-transparent text-black 
    hover:bg-[var(--color-bg)] hover:border-[var(--color-primaryblue)] 
    dark:text-white dark:hover:bg-bg-dark dark:hover:border-[var(--color-primaryblue)]
  `;

  return (
    <LayoutColumn
      mdSpan={3}
      className="px-0 md:px-0 lg:px-0 absolute left-0 hidden min-h-screen h-screen w-full bg-[var(--color-surface)] pt-20 text-text-light shadow-md md:flex flex-col dark:bg-gray-900 dark:text-white dark:shadow-black/20 tracking-[0.2em] leading-[44px] text-2xl pb-10"
    >
      <nav className="flex flex-col justify-between h-full pt-10 w-full">

        <div className="flex flex-col gap-4 w-full">
          <NavLink to="/" className={({ isActive }) =>
            `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
          }>
            <HomeSharpIcon className="mr-4" />
            Home
          </NavLink>

          <NavLink to="/assets" className={({ isActive }) =>
            `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
          }>
            <MonitorSharpIcon className="mr-4" />
            Assets
          </NavLink>

          <NavLink to="/bookings" className={({ isActive }) =>
            `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
          }>
            <CalendarTodaySharpIcon className="mr-4" />
            Bookings
          </NavLink>

          <NavLink to="/users" className={({ isActive }) =>
            `${linkBase} ${isActive ? activeStyle : inactiveStyle}`
          }>
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
