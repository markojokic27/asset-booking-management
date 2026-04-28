// Components
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Header } from '../components/layout/Header';
import { Logo } from '../components/icons/Logo';
import { HeaderHero } from '../components/layout/HeaderHero';
import LoginForm from '../features/auth/components/LoginForm';

export default function Login() {
  return (
    <>
      <Header className="hidden md:flex" />
      <Layout>
        <LayoutRow>
          <LayoutColumn
            lgSpan={6}
            className="hidden min-h-screen items-start justify-center pt-20 lg:flex lg:flex-col"
          >
            <HeaderHero />
          </LayoutColumn>
          <LayoutColumn
            lgSpan={6}
            lgOffset={0}
            smOffset={2}
            smSpan={8}
            className="flex min-h-screen flex-col items-center justify-center sm:mb-0 md:pt-20"
          >
            <div className="mb-10 md:hidden">
              <Logo className="scale-150 dark:brightness-0 dark:invert" />
            </div>
            <LoginForm />
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
