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

export function AvailabilityCalendar({
  events,
  onDateClick,
  selectedDate,
}: Props) {
  const handleDateClick = React.useCallback(
    (info: any) => {
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
        height="auto"
        fixedWeekCount={false}
        showNonCurrentDates={false}
        events={events}
        dateClick={handleDateClick}
        eventClick={handleEventClick}
        dayCellClassNames={(arg) => {
          const date = arg.date.toLocaleDateString('sv-SE');
          const isSelected = selectedDate === date;

          return [
            'cursor-pointer transition-all duration-150',
            'hover:bg-blue-50 dark:hover:bg-blue-900/20',
            isSelected
              ? 'bg-blue-100 dark:bg-blue-900/40 ring-2 ring-blue-500'
              : '',
          ].join(' ');
        }}
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: '',
        }}
        eventDisplay="block"
      />
    </div>
  );
}
