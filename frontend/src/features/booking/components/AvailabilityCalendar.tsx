import * as React from 'react';

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

type CalendarEvent = {
  id: string;
  title: string;
  start: Date;
  end: Date;
  backgroundColor?: string;
  borderColor?: string;
  extendedProps?: {
    status: string;
    notes?: string;
  };
};

type Props = {
  events: CalendarEvent[];
  onDateClick?: (date: string) => void;
  selectedDate?: string;
};

const isPastDate = (date: Date) => {
  const today = new Date();

  today.setHours(0, 0, 0, 0);

  const compareDate = new Date(date);

  compareDate.setHours(0, 0, 0, 0);

  return compareDate < today;
};

export function AvailabilityCalendar({
  events,
  onDateClick,
  selectedDate,
}: Props) {
  const handleDateClick = React.useCallback(
    (info: any) => {
      if (isPastDate(info.date)) {
        return;
      }

      onDateClick?.(info.dateStr);
    },
    [onDateClick]
  );
  console.log('events', events);

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
        height="auto"
        firstDay={1}
        fixedWeekCount={false}
        showNonCurrentDates={false}
        events={events}
        dateClick={handleDateClick}
        eventClick={handleEventClick}
        displayEventTime={false} //fix for HOUR variant
        dayCellClassNames={(arg) => {
          const date = arg.date.toLocaleDateString('sv-SE');

          const isSelected = selectedDate === date;

          const isPast = isPastDate(arg.date);

          return [
            'transition-all duration-150',

            isPast
              ? ' bg-gray-100 text-gray-400 opacity-80 dark:bg-gray-900 dark:text-gray-600'
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
