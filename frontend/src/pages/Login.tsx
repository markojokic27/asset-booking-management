import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Header } from '../components/layout/Header';
import LoginForm from '../features/auth/components/LoginForm';
import { Logo } from '../components/icons/Logo';
import { HeaderHero } from '../components/layout/HeaderHero';

export default function Login() {
  return (
    <>
      <Header className="hidden md:flex" />
      <Layout>
        <LayoutRow>
          <LayoutColumn
            lgSpan={6}
            className="mt-20 hidden items-center justify-center lg:flex lg:flex-col"
          >
            <HeaderHero />
          </LayoutColumn>
          <LayoutColumn
            lgSpan={6}
            lgOffset={0}
            smOffset={2}
            smSpan={8}
            className="flex min-h-screen flex-col items-center justify-center md:h-fit"
          >
            <div className="mb-10">
              <Logo className="scale-150 md:hidden dark:brightness-0 dark:invert" />
            </div>
            <LoginForm />
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
