import { PieChart } from '@mui/x-charts/PieChart';
// import { useTranslation } from 'react-i18next';

export default function TopUserBookings() {
  // const { t } = useTranslation();

  const data = [
    {
      userId: 1,
      value: 11,
      label: "John Doe",
    },
    {
      userId: 2,
      value: 7,
      label: "Jane Doe",
    },
    {
      userId: 3,
      value: 6,
      label: "Mark Jones",
    },
  ];

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-col gap-1">
        <h2 className="text-xl font-black tracking-wide text-black dark:text-white">
          Top Users by number of Bookings
        </h2>
      </div>

      <div data-testid="top-users" className="flex w-full items-center justify-center overflow-hidden rounded-2xl bg-gray-50 px-4 py-6 dark:bg-white/5 dark:text-white">
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
