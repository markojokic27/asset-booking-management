import { PieChart } from '@mui/x-charts/PieChart';
import { useTranslation } from 'react-i18next';

import type { GeneralReportResponseDTO } from '../types';

export default function TopUserBookings({
  report,
  selectedAssetName
}: {
  report: GeneralReportResponseDTO | null;
  selectedAssetName: string
}) {
  const { t } = useTranslation();

  const data =
    report?.topUsers?.map((u) => ({
      id: u.userId,
      value: u.bookingCount,
      label: u.fullName,
    })) ?? [];

  const total = data.reduce((sum, d) => sum + d.value, 0);

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-1">
        <h2 className="text-xl font-black tracking-wide text-black dark:text-white">
          { t('report.topUsersChart.title') } { selectedAssetName }
        </h2>
      </div>

      <div
        data-testid="top-users"
        className="flex w-full items-center justify-center overflow-hidden rounded-2xl bg-gray-50 px-4 py-6 dark:bg-white/5 dark:text-white"
      >
        {total === 0 ? (
          <div className="flex h-[320px] items-center justify-center text-gray-500">
            { t('report.noData') }
          </div>
        ) : (
          <PieChart
            series={[
              {
                data,
                innerRadius: 55,
                outerRadius: 110,
                paddingAngle: 2,
                cornerRadius: 5,
                faded: {
                  innerRadius: 50,
                  additionalRadius: -5,
                  color: '#9ca3af',
                },
              },
            ]}
            height={320}
          />
        )}
      </div>
    </div>
  );
}