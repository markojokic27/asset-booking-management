import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { useNavigate } from 'react-router-dom';

export default function Home() {
  const navigate = useNavigate();
  const arr = Array.from({ length: 12 }, (_, i) => i);
  const boxClass =
    'mb-8 flex h-32 items-center justify-center rounded-lg bg-gray-200 text-2xl font-bold';

  const handleLogout = () => {
    document.cookie = 'auth=; Max-Age=0; path=/';
    navigate('/login');
  };

  return (
    <>
      <Layout>
        <LayoutRow>
          <LayoutColumn className="flex flex-col items-center justify-center">
            <h1 className="my-8 text-center text-5xl font-bold">
              Asset manager
            </h1>
            <button
              onClick={handleLogout}
              className="rounded bg-red-500 px-4 py-2 text-white"
            >
              Logout
            </button>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
      <Layout>
        <LayoutRow className="mt-8">
          {arr.map((i) => (
            <LayoutColumn key={i} span={1}>
              <div className={boxClass}>{i + 1}</div>
            </LayoutColumn>
          ))}
        </LayoutRow>
        <LayoutRow>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
          <LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3}>
            <div className={boxClass}>Responsive column</div>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
