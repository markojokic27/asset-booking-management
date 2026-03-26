import { useNavigate } from 'react-router-dom';
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <Layout>
      <LayoutRow>
        <LayoutColumn className="flex h-screen flex-col items-center justify-center">
          <h1 className="mt-20 mb-10 text-center text-6xl font-black">
            Manager Page
          </h1>
          <Button onClick={() => navigate('/')}>Go to Home</Button>
        </LayoutColumn>
      </LayoutRow>
    </Layout>
  );
}
