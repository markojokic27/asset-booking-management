import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Header } from '../components/layout/Header';
import LoginForm from '../features/auth/components/LoginForm';

export default function Login() {
  return (
    <>
      <Header />
      <Layout>
        <LayoutRow>
          <LayoutColumn
            mdSpan={6}
            mdOffset={3}
            smOffset={2}
            smSpan={8}
            className="mt-30 flex flex-col items-center justify-center rounded-3xl border-2 border-blue-500 bg-gray-100 px-6 py-6 md:px-6 lg:px-10 lg:py-10 dark:bg-gray-900"
          >
            <h1 className="mb-6 text-center text-6xl font-black text-gray-900 lg:mb-10 dark:text-gray-100">
              Login
            </h1>
            <LoginForm />
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
