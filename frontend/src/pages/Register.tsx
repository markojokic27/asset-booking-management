import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Header } from '../components/layout/Header';
import RegisterForm from '../features/auth/components/RegisterForm';
import { Logo } from '../components/icons/Logo';
import { HeaderHero } from '../components/layout/HeaderHero';

export default function Register() {
  return (
    <>
      <Header className="hidden md:flex" />
      <Layout>
        <LayoutRow>
          <LayoutColumn
            lgSpan={6}
            className="mt-30 hidden items-center justify-center lg:flex lg:flex-col"
          >
            <HeaderHero />
          </LayoutColumn>
          <LayoutColumn
            lgSpan={6}
            lgOffset={0}
            smOffset={2}
            smSpan={8}
            className="flex h-screen flex-col items-center justify-center md:h-fit"
          >
            <div className="mb-10">
              <Logo className="scale-150 md:hidden dark:brightness-0 dark:invert" />
            </div>
            <RegisterForm />
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
