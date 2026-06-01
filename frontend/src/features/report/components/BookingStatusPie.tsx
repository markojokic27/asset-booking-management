import { PieChart } from '@mui/x-charts/PieChart';
import { useTranslation } from 'react-i18next';

export default function BookingStatusPie() {
  const { t } = useTranslation();

  const data = [
    {
      id: 0,
      value: 1,
      label: t('bookings.status.pending'),
      color: '#f59e0b',
    },
    {
      id: 1,
      value: 2,
      label: t('bookings.status.approved'),
      color: '#10b981',
    },
    {
      id: 2,
      value: 3,
      label: t('bookings.status.cancelled'),
      color: '#ef4444',
    },
    {
      id: 3,
      value: 4,
      label: t('bookings.status.rejected'),
      color: '#8b5cf6',
    },
    {
      id: 4,
      value: 5,
      label: t('bookings.status.completed'),
      color: '#3b82f6',
    },
  ];

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-1">
        <h2 className="text-xl font-black tracking-wide text-black dark:text-white">
          {t('report.bookingStatusPieChart.title')}
        </h2>
      </div>

      <div data-testid="booking-by-status" className="flex w-full items-center justify-center overflow-hidden rounded-2xl bg-gray-50 px-4 py-6 dark:bg-white/5 dark:text-white">
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
      </div>
    </div>
  );
}
