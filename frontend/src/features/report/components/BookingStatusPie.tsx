import { PieChart } from '@mui/x-charts/PieChart';
import { useTranslation } from 'react-i18next';

import type { GeneralReportResponseDTO } from '../types';

export default function BookingStatusPie({
  report,
}: {
  report: GeneralReportResponseDTO | null;
}) {
  const { t } = useTranslation();

  const data = [
    {
      id: 0,
      value: report?.totalPendingBookingCount ?? 0,
      label: t('bookings.status.pending'),
      color: '#f59e0b',
    },
    {
      id: 1,
      value: report?.totalApprovedBookingCount ?? 0,
      label: t('bookings.status.approved'),
      color: '#10b981',
    },
    {
      id: 2,
      value: report?.totalCancelledBookingCount ?? 0,
      label: t('bookings.status.cancelled'),
      color: '#ef4444',
    },
    {
      id: 3,
      value: report?.totalRejectedBookingCount ?? 0,
      label: t('bookings.status.rejected'),
      color: '#8b5cf6',
    },
    {
      id: 4,
      value: report?.totalCompletedBookingCount ?? 0,
      label: t('bookings.status.completed'),
      color: '#3b82f6',
    },
  ];

  const total =
  (report?.totalPendingBookingCount ?? 0) +
  (report?.totalApprovedBookingCount ?? 0) +
  (report?.totalCancelledBookingCount ?? 0) +
  (report?.totalRejectedBookingCount ?? 0) +
  (report?.totalCompletedBookingCount ?? 0);

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-1">
        <h2 className="text-xl font-black tracking-wide text-black dark:text-white">
          {t('report.bookingStatusPieChart.title')}
        </h2>
      </div>

      <div
        data-testid="booking-by-status"
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