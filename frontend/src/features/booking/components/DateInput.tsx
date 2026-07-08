import * as React from 'react';

type Props = {
  id: string;
  label: string;
  placeholder?: string;
  value: string;
  onChange: (value: string) => void;
  className?: string;
  testId?: string;
  max?: string;
};

export const DateInput: React.FC<Props> = ({
  id,
  label,
  placeholder,
  value,
  onChange,
  className,
  testId,
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

  const formatDisplayDate = (dateString: string) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    if (Number.isNaN(date.getTime())) return '';

    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();

    return `${day}.${month}.${year}.`;
  };

  return (
    <div className={className}>
      {label && (
        <p className="mb-1 text-sm font-medium text-(--color-table-text)">
          {label}
        </p>
      )}

      <button
        onClick={openDatePicker}
        className="relative w-full hover:cursor-pointer"
      >
        <input
          ref={dateRef}
          id={id}
          type="date"
          data-testid={testId}
          value={value}
          min={new Date().toISOString().split('T')[0]}
          max={max}
          onChange={(e) => onChange(e.target.value)}
          className="absolute inset-0 z-10 h-full w-full cursor-pointer opacity-0"
        />
        <div
          className={`date-filter-control flex h-11 w-full items-center rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-3 py-2 text-sm transition outline-none ${
            value ? 'text-(--color-table-text)' : 'text-(--color-table-text)/60'
          }`}
        >
          {value ? formatDisplayDate(value) : placeholder}
        </div>
      </button>
    </div>
  );
};

export const DateInputNoMin: React.FC<Props> = ({
  id,
  label,
  placeholder,
  value,
  onChange,
  className,
  testId,
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

  const formatDisplayDate = (dateString: string) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    if (Number.isNaN(date.getTime())) return '';

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

      <button onClick={openDatePicker} className="relative w-full">
        <input
          ref={dateRef}
          id={id}
          type="date"
          data-testid={testId}
          value={value}
          max={max}
          onChange={(e) => onChange(e.target.value)}
          className="absolute inset-0 z-10 h-full w-full cursor-pointer opacity-0"
        />
        <div
          className={`date-filter-control flex h-11 w-full items-center rounded-lg border-2 border-(--color-table-border) bg-(--color-table-surface) px-3 py-2 text-sm transition outline-none ${
            value ? 'text-(--color-table-text)' : 'text-(--color-table-text)/60'
          }`}
        >
          {value ? formatDisplayDate(value) : placeholder}
        </div>
      </button>
    </div>
  );
};
