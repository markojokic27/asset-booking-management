// Utils
import { useTranslation } from 'react-i18next';

type Props = {
  selectedDays: number[];
  onChange: (days: number[]) => void;
};

export function RecurringDaysSelector({ selectedDays, onChange }: Props) {
  const { t } = useTranslation();

  const days: { value: number; label: string }[] = [
    { value: 1, label: t('bookings.recurringDays.monday') },
    { value: 2, label: t('bookings.recurringDays.tuesday') },
    { value: 3, label: t('bookings.recurringDays.wednesday') },
    { value: 4, label: t('bookings.recurringDays.thursday') },
    { value: 5, label: t('bookings.recurringDays.friday') },
  ];

  const toggleDay = (day: number) => {
    if (selectedDays.includes(day)) {
      onChange(selectedDays.filter((d) => d !== day));
    } else {
      onChange([...selectedDays, day]);
    }
  };

  return (
    <div className="mb-6">
      <p className="mb-2 text-sm font-medium text-(--color-table-text)">
        {t('bookings.recurringDays.bookEvery')}
      </p>

      <div className="flex flex-wrap gap-4">
        {days.map((day) => (
          <label
            key={day.value}
            data-testid="checkbox-days-label"
            className="flex items-center gap-2 hover:cursor-pointer"
          >
            <input
              data-testid="checkbox-days"
              type="checkbox"
              className="h-5 w-5 rounded-lg border-gray-300 text-blue-600 hover:cursor-pointer focus:ring-blue-500"
              checked={selectedDays.includes(day.value)}
              onChange={() => toggleDay(day.value)}
            />
            {day.label}
          </label>
        ))}
      </div>
    </div>
  );
}
