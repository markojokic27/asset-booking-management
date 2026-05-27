import * as React from 'react';

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

//import hrLocale from '@fullcalendar/core/locales/hr';

type CalendarEvent = {
  id: string;
  title: string;
  start: string;
  end: string;
  backgroundColor?: string;
  borderColor?: string;
  extendedProps?: {
    status: string;
    notes?: string;
  };
};

type Props = {
  events: CalendarEvent[];
  selectedDate?: string;
  onDateClick?: (date: string) => void;
  variant?: 'HOUR' | 'DAY';
};

export function AvailabilityCalendar({
  events,
  selectedDate,
  onDateClick,
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

  const handleEventClick = React.useCallback((info: any) => {
    const { status, notes } = info.event.extendedProps;

    console.log({
      bookedBy: info.event.title,
      status,
      notes,
    });
  }, []);

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
        dateClick={handleDateClick}
        eventClick={handleEventClick}
        eventContent={(eventInfo) => {
          const start = eventInfo.event.start?.toLocaleTimeString([], {
            hour: 'numeric',
          });

          const end = eventInfo.event.end?.toLocaleTimeString([], {
            hour: 'numeric',
          });

          return (
            <div className="text-xs">
              {variant === 'HOUR' && (
                <div className="font-semibold">
                  {start} - {end}
                </div>
              )}
              <div>{eventInfo.event.title}</div>
            </div>
          );
        }}
        dayCellClassNames={(arg) => {
          const date = arg.date.toLocaleDateString('sv-SE');

          const isSelected = selectedDate === date;

          const isPast = isPastDate(arg.date);

          return [
            'transition-all duration-150',

            isPast
              ? 'bg-gray-100 text-gray-400 opacity-60 dark:bg-gray-900 dark:text-gray-600'
              : 'cursor-pointer hover:bg-blue-50 dark:hover:bg-blue-900/20',

            isSelected
              ? 'bg-blue-100 ring-2 ring-blue-500 dark:bg-blue-900/40'
              : '',
          ].join(' ');
        }}
        eventDisplay="block"
      />
    </div>
  );
}
