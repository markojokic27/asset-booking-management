import { BarChart } from '@mui/x-charts/BarChart';
import { useTranslation } from 'react-i18next';
import { useTheme } from '../../../app/ThemeProvider';

export default function BookingStatusPie() {
  const { t } = useTranslation();
  const { theme } = useTheme();

  const isDark = theme === 'dark';

  const chartSetting = {
    yAxis: [
        {
            label: t('report.bookingStatusBarChart.yAxisLabel'),
            width: 60,
        },
    ],
    height: 300,
    sx: {
        '& .MuiChartsLegend-label': {
            color: isDark ? '#ffffff !important' : '#111111 !important',
        },
        '& .MuiChartsAxis-tickLabel': {
            color: isDark ? '#ffffff !important' : '#111111 !important',
        },
        '& text': {
            color: isDark ? '#ffffff !important' : '#111111 !important',
        },

        '& tspan': {
            stroke: isDark ? '#ffffff !important' : '#111111 !important',
            fontWeight: "lighter",
        },
        '& .MuiChartsAxis-line': {
            stroke: isDark ? '#ffffff !important' : '#111111 !important',
        },
        '& .MuiChartsAxis-tick': {
            stroke: isDark ? '#ffffff !important' : '#111111 !important',
        },
    },
  };

  const data = [
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Jan',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Feb',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Mar',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Apr',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'May',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'June',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'July',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Aug',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Sept',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Oct',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Nov',
    },
    {
        completed: 1,
        rejected: 2,
        cancelled: 3,
        total: 6,
        month: 'Dec',
    },
  ];

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-1">
        <h2 className="text-xl font-black tracking-wide text-black dark:text-white">
            { t('report.bookingStatusBarChart.title') }
        </h2>
      </div>

      <div className="flex w-full items-center justify-center overflow-hidden rounded-2xl bg-gray-50 px-4 py-6 dark:bg-white/5 dark:text-white">
        <BarChart
            dataset={data}
            xAxis={[{ dataKey: 'month' }]}
            series={[
                { dataKey: 'completed', label: t('bookings.status.completed'), },
                { dataKey: 'rejected', label: t('bookings.status.rejected'), },
                { dataKey: 'cancelled', label: t('bookings.status.cancelled'), },
                { dataKey: 'total', label: 'Total', },
            ]}
            {...chartSetting}
        />
      </div>
    </div>
  );
}
