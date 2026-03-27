import { Outlet } from 'react-router-dom';
import { Header } from '../components/layout/Header';
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Navbar } from '../components/layout/Navbar';

export default function App() {
  return (
    <>
      <Header />
      <main className="min-h-screen bg-gray-200 pt-22 text-gray-900 dark:bg-gray-950 dark:text-gray-100">
        <Layout>
          <LayoutRow className="relative">
            <Navbar />
            <LayoutColumn span={12} mdSpan={9} mdOffset={3} className="pt-20">
              <Outlet />
            </LayoutColumn>
          </LayoutRow>
        </Layout>
      </main>g-gray-200 min-h-screen
    </>
  );
}
