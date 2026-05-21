import { useTranslation } from 'react-i18next';

// Components
import { Layout, LayoutRow, LayoutColumn } from '../components/layout/Layout';
import { Header } from '../components/layout/Header';

import BookingStatusPie from '../features/report/components/BookingStatusPie';

export default function Report() {
  const { t } = useTranslation();

  return (
    <>
      <Header />

      <Layout>
        <LayoutRow>
          <LayoutColumn
            span={12}
            mdSpan={9}
            mdOffset={3}
            className="flex min-h-screen flex-col pt-35 pb-10"
          >
            <div className="flex w-full flex-col gap-4">
              <div className="flex flex-col gap-2">
                <h1 className="text-3xl font-black tracking-widest text-black dark:text-white">
                  {t('report.title')}
                </h1>
              </div>

              <div className="h-px w-full bg-(--color-table-border)" />
            </div>

            <div className="mt-8 flex flex-col gap-6">
              <div className="dark:bg-bg-dark rounded-2xl border border-(--color-table-border) p-6 shadow-sm dark:shadow-black/20">
                <BookingStatusPie />
              </div>
            </div>
          </LayoutColumn>
        </LayoutRow>
      </Layout>
    </>
  );
}