import { Outlet, Link } from 'react-router-dom';

export default function App() {
  return (
    <>
      <nav className="fixed top-0 z-10 flex w-full gap-4 bg-gray-800 p-4 text-white">
        <Link to="/">Home</Link>
        <Link to="/manager">Manager</Link>
      </nav>

      <main className="h-full bg-gray-200 pt-12">
        <Outlet />
      </main>
    </>
  );
}
