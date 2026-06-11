// External packages
import * as React from 'react';
import { useTranslation } from 'react-i18next';

type Props = {
  label: string;
  value: string;
  onChange: (value: string) => void;
  selectedDate: string;
  minHour?: string;
  className?: string;
};

export const hourOptions = Array.from(
  { length: 17 },
  (_, index) => `${String(index + 6).padStart(2, '0')}:00`
);

const toMinutes = (time: string) => {
  const [h, m] = time.split(':').map(Number);
  return h * 60 + m;
};

export const HourSelect: React.FC<Props> = ({
  label,
  value,
  onChange,
  selectedDate,
  minHour,
  className,
}) => {
  const { t } = useTranslation();

  const today = new Date().toISOString().split('T')[0];
  const currentHour = new Date().getHours();

  const availableHours = hourOptions.filter((hour) => {
    const [hh] = hour.split(':').map(Number);

    if (selectedDate === today && hh < currentHour) {
      return false;
    }

    if (minHour) {
      return toMinutes(hour) > toMinutes(minHour);
    }

    return true;
  });

  return (
    <div className={className}>
      <p className="mb-1 text-sm font-medium text-(--color-table-text)">
        {label}
      </p>

      <select
        value={value}
        onChange={(e) => onChange(e.target.value)}
        aria-label={`${label} ${t('ui.dateTimeInput.hourAriaSuffix')}`}
        className={`h-11 w-full cursor-pointer rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-3 text-sm transition outline-none focus:outline-none ${
          value ? 'text-(--color-table-text)' : 'text-(--color-table-text)/60'
        }`}
      >
        <option value="" disabled hidden>
          {t('ui.dateTimeInput.selectHour')}
        </option>

        {availableHours.map((hour) => (
          <option key={hour} value={hour}>
            {hour}
          </option>
        ))}
      </select>
    </div>
  );
};
