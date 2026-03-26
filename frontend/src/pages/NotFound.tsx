import { useNavigate } from 'react-router-dom';
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <Layout>
      <LayoutRow>
        <LayoutColumn className="flex h-screen flex-col items-center justify-center">
          <h1 className="mt-20 mb-10 text-center text-6xl font-black">
            404 - Not Found
          </h1>
          <button
            onClick={() => navigate('/')}
            className="rounded bg-blue-500 px-4 py-2 text-white"
          >
            Go to Home
          </button>
        </LayoutColumn>
      </LayoutRow>
    </Layout>
  );
}
