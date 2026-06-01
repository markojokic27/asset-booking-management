import * as React from 'react';

type Props = {
  id: string;
  label: string;
  value: string;
  onChange: (value: string) => void;
  className?: string;
};

export const DateInput: React.FC<Props> = ({
  id,
  label,
  value,
  onChange,
  className,
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
      <p className="mb-1 text-sm font-medium text-(--color-table-text)">
        {label}
      </p>

      <div
        onClick={openDatePicker}
        className="relative cursor-pointer"
      >
        <input data-testid="from-date-input"
          ref={dateRef}
          id={id}
          type="date"
          value={value}
          min={new Date().toISOString().split('T')[0]}
          onChange={(e) => onChange(e.target.value)}
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