// External packages
import * as React from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

// Types
import type { BookingWithRelations } from '../types';

//import hrLocale from '@fullcalendar/core/locales/hr';

type CalendarEvent = {
  id: string;
  title: string;
  start: string;
  end: string;
  backgroundColor?: string;
  borderColor?: string;
  extendedProps?: {
    booking: BookingWithRelations;
  };
};

type Props = {
  events: CalendarEvent[];
  selectedFromDate?: string;
  selectedToDate?: string;
  onDateClick?: (date: string) => void;
  variant?: 'HOUR' | 'DAY';
  setSelectedBooking?: (booking: BookingWithRelations | null) => void;
  onRangeSelect: (fromDate: string, toDate: string) => void;
  availableRecurringDates?: string[];
  onMonthChange?: (date: Date) => void;
};

export function AvailabilityCalendar({
  events,
  selectedFromDate,
  selectedToDate,
  onDateClick,
  setSelectedBooking,
  onRangeSelect,
  onMonthChange,
  availableRecurringDates = [],
  variant = 'DAY',
}: Props) {
  const isPastDate = (date: Date) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const compareDate = new Date(date);
    compareDate.setHours(0, 0, 0, 0);
    return compareDate < today;
  };

  const handleDateClick = React.useCallback(
    (info: any) => {
      if (isPastDate(info.date)) {
        return;
      }
      onDateClick?.(info.dateStr);
    },
    [onDateClick]
  );

  const handleEventClick = React.useCallback(
    (info: any) => {
      setSelectedBooking?.(info.event.extendedProps?.booking ?? null);
    },
    [setSelectedBooking]
  );

  const handleDateRangeSelect = React.useCallback(
    (info: any) => {
      const fromDate = info.startStr;

      const endDate = new Date(info.end);
      endDate.setDate(endDate.getDate() - 1);

      const toDate = endDate.toLocaleDateString('sv-SE');

      onRangeSelect?.(fromDate, toDate);
    },
    [onRangeSelect]
  );

  const isDateInRange = (date: string, from?: string, to?: string) => {
    if (!from) return false;
    if (!to) {
      return date === from;
    }
    return date >= from && date <= to;
  };

  return (
    <div className="rounded-xl border border-(--color-border) bg-(--color-bg) p-4">
      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        //locale={hrLocale}
        firstDay={1}
        height="auto"
        fixedWeekCount={false}
        showNonCurrentDates={false}
        displayEventTime={true}
        events={events}
        selectable={variant !== 'HOUR'}
        selectMirror={true}
        dateClick={variant === 'HOUR' ? handleDateClick : undefined}
        select={variant !== 'HOUR' ? handleDateRangeSelect : undefined}
        eventClick={handleEventClick}
        datesSet={(info) => {
          onMonthChange?.(info.view.currentStart);
        }}
        eventContent={(eventInfo) => {
          const start = eventInfo.event.start?.toLocaleTimeString([], {
            hour: 'numeric',
            //minute: '2-digit',
          });

          const end = eventInfo.event.end?.toLocaleTimeString([], {
            hour: 'numeric',
            //minute: '2-digit',
          });

          return (
            <div className="text-md font-semibold">
              {variant === 'HOUR' && (
                <div>
                  {start} - {end}
                </div>
              )}
              <div>{eventInfo.event.title}</div>
            </div>
          );
        }}
        dayCellClassNames={(arg) => {
          const date = arg.date.toLocaleDateString('sv-SE');

          const isSelected = isDateInRange(
            date,
            selectedFromDate,
            selectedToDate
          );
          const isRecurring = availableRecurringDates.includes(date);
          const isPast = isPastDate(arg.date);

          return [
            'transition-all duration-150',

            isPast
              ? 'bg-gray-100 text-gray-400 opacity-60 dark:bg-gray-900 dark:text-gray-600'
              : 'cursor-pointer hover:bg-blue-50 dark:hover:bg-blue-900/20',

            isSelected || isRecurring
              ? 'bg-blue-100 ring-2 ring-blue-500 dark:bg-blue-900/40'
              : '',
          ].join(' ');
        }}
        eventDisplay="block"
      />
    </div>
  );
}
