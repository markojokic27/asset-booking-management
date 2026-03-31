import { LayoutColumn } from '../components/layout/Layout';
import { useNavigate } from 'react-router-dom';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { useTranslation } from 'react-i18next';

export default function Home() {
  const navigate = useNavigate();
  const { t } = useTranslation();

  const handleLogout = () => {
    document.cookie = 'auth=; Max-Age=0; path=/';
    navigate('/login');
  };

  return (
    <>
      <LayoutColumn span={12} mdSpan={9} mdOffset={3} className="flex pt-20">
        <Input
          placeholder="Search assets..."
          errorMessage="Please enter a search term"
          className="w-50"
        />
        <Button
          onClick={handleLogout}
          size="sm"
          className="border-red-500 bg-red-500 text-white hover:border-red-600 hover:bg-red-600"
        >
          Logout
        </Button>
      </LayoutColumn>

      <LayoutColumn span={12} mdSpan={9} mdOffset={3} className="flex pt-20">
        <h1>{t('common.save')}</h1>
      </LayoutColumn>
    </>
  );
}
