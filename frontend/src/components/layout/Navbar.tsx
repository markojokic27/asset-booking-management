import { LayoutColumn } from './Layout';
import { Link } from 'react-router-dom';

export const Navbar: React.FC = () => {
  return (
    <LayoutColumn
      mdSpan={3}
      className="absolute left-0 z-20 hidden min-h-screen w-full bg-(--color-surface) pt-20 shadow-md md:flex"
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
