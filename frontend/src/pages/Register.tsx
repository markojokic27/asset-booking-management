import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Header } from '../components/layout/Header';
import RegisterForm from '../features/auth/components/RegisterForm';
import { Logo } from '../components/icons/Logo';

export default function Register() {
  return (
    <>
      <Header className="hidden md:flex" />
      <Layout>
        <LayoutRow>
          <LayoutColumn
            lgSpan={6}
            lgOffset={3}
            smOffset={2}
            smSpan={8}
            className="flex h-screen flex-col items-center justify-center md:h-fit"
          >
            <Logo className="h-20 scale-250 md:hidden dark:brightness-0 dark:invert" />
            <RegisterForm />
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}