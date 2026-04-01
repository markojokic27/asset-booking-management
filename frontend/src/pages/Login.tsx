import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Header } from '../components/layout/Header';
import LoginForm from '../features/auth/components/LoginForm';
import { Logo } from '../components/icons/Logo';

export default function Login() {
  return (
    <>
      <Header className="hidden md:flex" />
      <Layout>
        <LayoutRow>
          <LayoutColumn
            lgSpan={6}
            className="hidden items-center justify-center pt-30 lg:flex lg:flex-col"
          >
            <h1 className="text-6xl font-bold tracking-[0.2em]">
              Asset Booking Management
            </h1>
            <div className="mt-10 flex w-full">
              <div className="h-2 w-1/3 bg-black" />
              <div className="h-2 w-1/3 bg-[#e60037]" />
              <div className="h-2 w-1/3 bg-[#ffc300]" />
            </div>
            <p className="mt-10 w-full text-3xl">
              Simple powerful web and mobile software
              <br /> for asset bookingmanagement of
              <br /> Maurer workplace assets
            </p>
          </LayoutColumn>
          <LayoutColumn
            lgSpan={6}
            lgOffset={0}
            smOffset={2}
            smSpan={8}
            className="flex h-screen flex-col items-center justify-center md:h-fit"
          >
            <Logo className="h-20 scale-250 md:hidden dark:brightness-0 dark:invert" />
            <LoginForm />
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}
