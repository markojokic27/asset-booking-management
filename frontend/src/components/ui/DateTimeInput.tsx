import * as React from 'react';
import { useTranslation } from 'react-i18next';

export type DateTimeInputProps = {
  id: string;
  label: string;
  value: string;
  onChange: (value: string) => void;
  className?: string;
  hourValue?: string;
  onHourChange?: (value: string) => void;
  hourOptions?: readonly string[];
};

export const hourOpt = Array.from(
  { length: 13 },
  (_, index) => `${String(index + 7).padStart(2, '0')}:00`
);

export const DateTimeInput: React.FC<DateTimeInputProps> = ({
  id,
  label,
  value,
  onChange,
  className,
  hourValue,
  onHourChange,
  hourOptions = hourOpt,
}) => {
  const hasSelectedHour = Boolean(hourValue);
  const { t } = useTranslation();

  const dateRef = React.useRef<HTMLInputElement>(null);

  const openDatePicker = () => {
    if (dateRef.current?.showPicker) {
      dateRef.current.showPicker();
    } else {
      dateRef.current?.focus();
    }
  };

  return (
    <div className={className}>
      <p className="mb-1 text-sm font-medium text-(--color-table-text)">
        {label}
      </p>

      <div className="flex items-center gap-2">
        <div
          onClick={openDatePicker}
          className="relative flex-1 cursor-pointer"
        >
          <input
            ref={dateRef}
            id={id}
            type="date"
            value={value}
            min={new Date().toISOString().split('T')[0]}
            onChange={(e) => onChange(e.target.value)}
            aria-label={label}
            className={`date-filter-control h-11 w-full cursor-pointer rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-3 py-2 text-sm transition outline-none focus:outline-none dark:[&::-webkit-calendar-picker-indicator]:invert ${
              value
                ? 'text-(--color-table-text)'
                : 'text-(--color-table-text)/60'
            }`}
          />
        </div>
        {onHourChange ? (
          <select
            value={hourValue ?? ''}
            onChange={(e) => onHourChange(e.target.value)}
            aria-label={`${label} ${t('ui.dateTimeInput.hourAriaSuffix')}`}
            className={`h-11 w-24 cursor-pointer rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-2 text-sm transition outline-none focus:outline-none ${
              hasSelectedHour
                ? 'text-(--color-table-text)'
                : 'text-(--color-table-text)/60'
            }`}
          >
            <option value="" disabled hidden>
              07:00
            </option>
            {hourOptions.map((hour) => (
              <option key={hour} value={hour}>
                {hour}
              </option>
            ))}
          </select>
        ) : null}
      </div>
    </div>
  );
};
