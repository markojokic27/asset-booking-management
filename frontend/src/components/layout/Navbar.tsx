import { LayoutColumn } from './Layout';
import { Link } from 'react-router-dom';

export const Navbar: React.FC = () => {
  return (
    <LayoutColumn
      mdSpan={3}
      className="before:pulled-background-account absolute left-0 hidden min-h-screen w-full bg-gray-100 pt-20 md:flex"
    >
      <nav className="flex flex-col gap-4 pt-10">
        <Link
          to="/"
          className="text-lg font-medium text-gray-700 hover:text-gray-900"
        >
          Home
        </Link>
        <Link
          to="/assets"
          className="text-lg font-medium text-gray-700 hover:text-gray-900"
        >
          Assets
        </Link>
        <Link
          to="/bookings"
          className="text-lg font-medium text-gray-700 hover:text-gray-900"
        >
          Bookings
        </Link>
      </nav>
    </LayoutColumn>
  );
};
