// External packages
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

// Components
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';

export default function NotFound() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  return (
    <Layout>
      <LayoutRow>
        <LayoutColumn className="flex h-screen flex-col items-center justify-center">
          <h1 className="mt-20 mb-10 text-center text-6xl font-black">
            {t('ui.notFound.title')}
          </h1>
          <Button onClick={() => navigate('/')}>{t('ui.notFound.goHome')}</Button>
        </LayoutColumn>
      </LayoutRow>
    </Layout>
  );
}
