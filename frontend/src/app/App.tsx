import { Outlet } from 'react-router-dom';
import { Header } from '../components/layout/Header';
import { Layout, LayoutRow } from '../components/layout/Layout';
import { Navbar } from '../components/layout/Navbar';

export default function App() {
  return (
    <>
      <Header variant="app" />
      <Navbar />
      <Layout>
        <LayoutRow className="relative">
          <Outlet />
        </LayoutRow>
      </Layout>
    </>
  );
}
