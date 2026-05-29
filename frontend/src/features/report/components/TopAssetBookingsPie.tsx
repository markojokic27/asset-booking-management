import { PieChart } from '@mui/x-charts/PieChart';
// import { useTranslation } from 'react-i18next';
import { useTheme } from '../../../app/ThemeProvider';

export default function TopAssetBookings() {
  // const { t } = useTranslation();
  const { theme } = useTheme();

  const isDark = theme === 'dark';

  const data = [
    {
      assetId: 1,
      value: 3,
      label: "Parking spot 10",
    },
    {
      assetId: 2,
      value: 2,
      label: "Macbook Pro 16",
    },
  ];

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-1">
        <h2 className="text-xl font-black tracking-wide text-black dark:text-white">
          Top Assets by number of Bookings
        </h2>
      </div>

      <div data-testid="top-assets" className="flex w-full items-center justify-center overflow-hidden rounded-2xl bg-gray-50 px-4 py-6 dark:bg-white/5 dark:text-white">
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
          sx={{
            '& .MuiChartsLegend-label': {
              color: isDark ? '#ffffff' : '#111111',
            },
            '& .MuiChartsAxis-tickLabel': {
              color: isDark ? '#ffffff' : '#111111',
            },
            '& text': {
              color: isDark ? '#ffffff' : '#111111',
            },
          }}
        />
      </div>
    </div>
  );
}
