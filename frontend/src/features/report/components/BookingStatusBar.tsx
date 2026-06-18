import { BarChart } from '@mui/x-charts/BarChart';
import { useTranslation } from 'react-i18next';

import type { GeneralReportResponseDTO } from '../types';

export default function BookingStatusPie({
  report,
}: {
  report: GeneralReportResponseDTO | null;
}) {
  const { t } = useTranslation();

  const chartSetting = {
    yAxis: [
        {
          label: t('report.bookingStatusBarChart.yAxisLabel'),
          width: 60,
        },
    ],
    height: 300,
  };

  const data = (report?.monthlyStats ?? [])
  .slice()
  .sort((a, b) => {
    if (a.year !== b.year) {
      return a.year - b.year;
    }

    return a.month - b.month;
  })
  .map((m) => ({
    month: new Date(m.year, m.month - 1).toLocaleDateString(undefined, {
        month: 'short',
        year: 'numeric',
    }),
    completed: m.totalCompletedBookingCount,
    rejected: m.totalRejectedBookingCount,
    cancelled: m.totalCancelledBookingCount,
    pending: m.totalPendingBookingCount,
    approved: m.totalApprovedBookingCount,
    total: m.totalBookingsCount,
  }));

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-1">
        <h2 className="text-xl font-black tracking-wide text-black dark:text-white">
            { t('report.bookingStatusBarChart.title') }
        </h2>
      </div>

      <div data-testid="booking-by-month" className="flex w-full items-center justify-center overflow-hidden rounded-2xl bg-gray-50 px-4 py-6 dark:bg-white/5 dark:text-white">
        <BarChart 
            dataset={data}
            xAxis={[{ dataKey: 'month' }]}
            series={[
                { dataKey: 'completed', label: t('bookings.status.completed'), },
                { dataKey: 'rejected', label: t('bookings.status.rejected'), },
                { dataKey: 'cancelled', label: t('bookings.status.cancelled'), },
                { dataKey: 'approved', label: t('bookings.status.approved'), },
                { dataKey: 'pending', label: t('bookings.status.pending'), },
                { dataKey: 'total', label: 'Total', },
            ]}
            {...chartSetting}
        />
      </div>
    </div>
  );
}
