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
};

export function AvailabilityCalendar({ events }: Props) {
  const handleDateClick = React.useCallback((info: any) => {
    console.log('Selected date:', info.dateStr);
  }, []);

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
