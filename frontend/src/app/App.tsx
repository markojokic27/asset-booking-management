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
          <LayoutColumn span={12} mdSpan={9} mdOffset={3} className="pt-20">
            <Outlet />
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
