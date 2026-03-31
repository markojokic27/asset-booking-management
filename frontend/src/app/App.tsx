import { Outlet } from 'react-router-dom';
import { Header } from '../components/layout/Header';
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Navbar } from '../components/layout/Navbar';

export default function App() {
  return (
    <>
      <Header />
      <Layout>
        <LayoutRow className="relative">
          <Navbar />
          <Outlet />
        </LayoutRow>
      </Layout>
    </>
  );
}
