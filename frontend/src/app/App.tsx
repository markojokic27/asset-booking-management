import { Outlet } from 'react-router-dom';
import { Header } from '../components/layout/Header';

export default function App() {
  return (
    <>
      <Header />

      <main className="min-h-screen bg-gray-200 pt-22 text-gray-900 dark:bg-gray-950 dark:text-gray-100">
        <Outlet />
      </main>
    </>
  );
}
