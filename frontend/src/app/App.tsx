import { Outlet } from 'react-router-dom';
import { Header } from '../components/layout/Header';
import { Layout, LayoutRow } from '../components/layout/Layout';
import { Navbar } from '../components/layout/Navbar';
import { Footer } from '../components/layout/Footer';

export default function App() {
  return (
    <>
      <Header variant="app" />
      <Navbar />
      <Layout className="pb-28">
        <LayoutRow className="relative">
          <Outlet />
        </LayoutRow>
      </Layout>
      <Footer />
    </>
  );
}
