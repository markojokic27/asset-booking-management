import { LayoutColumn } from './Layout';
import { Link } from 'react-router-dom';

export const Navbar: React.FC = () => {
  return (
    <LayoutColumn
      mdSpan={3}
      className="before:pulled-background-account hover:text-gray-900s absolute left-0 hidden min-h-screen w-full bg-gray-100 pt-20 text-gray-700 shadow-md md:flex dark:bg-gray-900 dark:text-white dark:shadow-black/20"
    >
      <nav className="flex flex-col gap-4 pt-10">
        <Link to="/" className="text-lg font-medium">
          Home
        </Link>
        <Link to="/assets" className="text-lg font-medium">
          Assets
        </Link>
        <Link to="/bookings" className="text-lg font-medium">
          Bookings
        </Link>
      </nav>
    </LayoutColumn>
  );
};
