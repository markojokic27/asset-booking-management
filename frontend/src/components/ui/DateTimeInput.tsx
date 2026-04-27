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

// Generates hour options from 07:00 to 19:00
export const hourOpt = Array.from({ length: 13 }, (_, index) =>
  `${String(index + 7).padStart(2, '0')}:00`
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

  return (
    <div className={className}>
      <p className="mb-1 text-sm font-medium text-(--color-table-text)">{label}</p>
      <div className="flex items-center gap-2">
        <div className="relative flex-1">
          <input
            id={id}
            type="date"
            value={value}
            onChange={(e) => onChange(e.target.value)}
            aria-label={label}
            className={`date-filter-control h-10 w-full rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) py-2 pl-3 pr-3 text-sm outline-none focus:outline-none ${value ? 'text-(--color-table-text)' : 'text-(--color-table-text)/60'}`}
          />
        </div>
        {onHourChange ? (
          <select
            value={hourValue ?? ''}
            onChange={(e) => onHourChange(e.target.value)}
            aria-label={`${label} ${t('ui.dateTimeInput.hourAriaSuffix')}`}
            className={`h-10 w-24 rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-2 text-sm outline-none focus:outline-none ${hasSelectedHour ? 'text-(--color-table-text)' : 'text-(--color-table-text)/60'}`}
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
















