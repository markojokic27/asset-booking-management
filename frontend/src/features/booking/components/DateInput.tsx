import * as React from 'react';

type Props = {
  id: string;
  label: string;
  value: string;
  onChange: (value: string) => void;
  className?: string;
  testId?: string;
};

export const DateInput: React.FC<Props> = ({
  id,
  label,
  value,
  onChange,
  className,
  testId,
}) => {
  const dateRef = React.useRef<HTMLInputElement>(null);

  const openDatePicker = () => {
    if (dateRef.current?.showPicker) {
      dateRef.current.showPicker();
    } else {
      dateRef.current?.focus();
    }
  };

  const formatDisplayDate = (dateString: string) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return '';

    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();

    return `${day}.${month}.${year}.`;
  };

  return (
    <div className={className}>
      <p className="mb-1 text-sm font-medium text-(--color-table-text)">
        {label}
      </p>

      <div onClick={openDatePicker} className="relative cursor-pointer">
        <input
          data-testid={testId}
          ref={dateRef}
          id={id}
          type="date"
          value={value}
          min={new Date().toISOString().split('T')[0]}
          onChange={(e) => onChange(e.target.value)}
          className="absolute inset-0 z-10 h-full w-full cursor-pointer opacity-0"
        />
        <div
          className={`date-filter-control flex h-11 w-full items-center rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-3 py-2 text-sm transition outline-none ${
            value ? 'text-(--color-table-text)' : 'text-(--color-table-text)/60'
          }`}
        >
          {value ? formatDisplayDate(value) : 'Odaberite datum'}
        </div>
      </div>
    </div>
  );
};
