import * as React from 'react';
import FullCalendarComponent from '@fullcalendar/react';
import type { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

const FullCalendar = FullCalendarComponent as unknown as React.FC<CalendarOptions>;

// dodat da se u kalendru prikazuje booking history i omogucit pregled, te da se klikom na neki datum omoguci booking assseta

type CalendarEvent = {
  id: string;
  title: string;
  start: string;
  end: string;
};

type Props = {
  events: CalendarEvent[];
};

export function AvailabilityCalendar({ events }: Props) {
  const [isReady, setIsReady] = React.useState(false);

  React.useEffect(() => {
    const timeout = window.setTimeout(() => {
      setIsReady(true);
    }, 0);

    return () => {
      window.clearTimeout(timeout);
    };
  }, []);

  if (!isReady) {
    return null;
  }

  return (
    <div className="rounded-xl border border-(--color-border) bg-(--color-bg) p-4">
      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: '',
        }}
        events={events} // bookings koji se prikazuju u kalendaru
        height="auto"
        fixedWeekCount={false}
        showNonCurrentDates={false}
      />
    </div>
  );
}