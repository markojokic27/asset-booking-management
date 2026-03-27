import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { useNavigate } from 'react-router-dom';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { useTranslation } from 'react-i18next';

export default function Home() {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const arr = Array.from({ length: 12 }, (_, i) => i);
  const boxClass =
    'mb-8 flex h-32 items-center justify-center rounded-lg bg-gray-400 text-2xl font-bold text-gray-900 dark:bg-gray-800 dark:text-gray-100';

  const handleLogout = () => {
    document.cookie = 'auth=; Max-Age=0; path=/';
    navigate('/login');
  };

  return (
    <>
      <Layout>
        <LayoutRow>
          <LayoutColumn className="flex flex-col items-center justify-center">
            <h1 className="my-8 text-center text-5xl font-bold text-gray-900 dark:text-gray-100">
              Asset manager
            </h1>
            <Button
              onClick={handleLogout}
              size="sm"
              className="border-red-500 bg-red-500 text-white hover:border-red-600 hover:bg-red-600"
            >
              Logout
            </Button>
          </LayoutColumn>
          <LayoutColumn span={3}>
            <Input
              placeholder="Search assets..."
              errorMessage="Please enter a search term"
            />
            <h1>{t('common.save')}</h1>
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
