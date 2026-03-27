import { useNavigate } from 'react-router-dom';
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Button } from '../components/ui/Button';
import { Header } from '../components/layout/Header';

export default function Login() {
  const navigate = useNavigate();

  /*   const handleLogin = async () => {
    await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      credentials: 'include',
    });

    navigate('/');
  };
 */

  const handleLogin = () => {
    document.cookie = 'auth=true; path=/';
    navigate('/');
  };

  return (
    <>
      <Header />
      <Layout>
        <LayoutRow>
          <LayoutColumn className="flex h-screen flex-col items-center justify-center">
            <h1 className="mt-20 mb-10 text-center text-6xl font-black text-gray-900 dark:text-gray-100">
              Login Page
            </h1>
            <Button onClick={handleLogin}>Login</Button>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
