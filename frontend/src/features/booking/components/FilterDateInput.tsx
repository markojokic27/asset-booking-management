import * as React from 'react';

type Props = {
  id: string;
  label: string;
  value: string;
  onChange: (value: string) => void;
  className?: string;
  min?: string;
  max?: string;
};

export const FilterDateInput: React.FC<Props> = ({
  id,
  label,
  value,
  onChange,
  className,
  min,
  max,
}) => {
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
      <label
        htmlFor={id}
        className="mb-1 block text-sm font-medium text-(--color-table-text)"
      >
        {label}
      </label>

      <div className="relative">
        <input
          ref={dateRef}
          id={id}
          type="date"
          value={value}
          min={min}
          max={max}
          onChange={(e) => onChange(e.target.value)}
          onClick={openDatePicker}
          className={`date-filter-control h-11 w-full cursor-pointer rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-3 py-2 text-sm transition outline-none focus:outline-none dark:[&::-webkit-calendar-picker-indicator]:invert ${
            value
              ? 'text-(--color-table-text)'
              : 'text-(--color-table-text)/60'
          }`}
        />
      </div>
    </div>
  );
};
