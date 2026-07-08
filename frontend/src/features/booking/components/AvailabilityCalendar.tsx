// External packages
import * as React from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

// Types
import type { BookingWithRelations } from '../types';

// Internationalization
import hrLocale from '@fullcalendar/core/locales/hr';
import enGbLocale from '@fullcalendar/core/locales/en-gb';
import deLocale from '@fullcalendar/core/locales/de';
import { useTranslation } from 'react-i18next';

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
  maxBookingDate: Date;
  visibleMonth: Date;
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
  visibleMonth,
  availableRecurringDates = [],
  maxBookingDate,
  variant = 'DAY',
}: Props) {
  const { i18n } = useTranslation();

  const calendarLocale = React.useMemo(() => {
    switch (i18n.resolvedLanguage) {
      case 'hr':
        return hrLocale;
      case 'de':
        return deLocale;
      default:
        return enGbLocale;
    }
  }, [i18n.resolvedLanguage]);

  const isSelectableDate = (date: Date) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const max = new Date(maxBookingDate);
    max.setHours(23, 59, 59, 999);

    return date >= today && date <= max;
  };

  const handleDateClick = React.useCallback(
    (info: any) => {
      if (!isSelectableDate(info.date)) {
        return;
      }

      onDateClick?.(info.dateStr);
    },
    [onDateClick, maxBookingDate]
  );
  const handleEventClick = React.useCallback(
    (info: any) => {
      setSelectedBooking?.(info.event.extendedProps?.booking ?? null);
    },
    [setSelectedBooking]
  );

  const handleDateRangeSelect = React.useCallback(
    (info: any) => {
      const end = new Date(info.end);
      end.setDate(end.getDate() - 1);

      if (!isSelectableDate(info.start) || !isSelectableDate(end)) {
        return;
      }
      onRangeSelect(info.startStr, end.toLocaleDateString('sv-SE'));
    },
    [onRangeSelect, maxBookingDate]
  );

  const isDateInRange = (date: string, from?: string, to?: string) => {
    if (!from) return false;
    if (!to) {
      return date === from;
    }
    return date >= from && date <= to;
  };
  const calendarRef = React.useRef<FullCalendar>(null);
  React.useEffect(() => {
    const api = calendarRef.current?.getApi();

    if (!api || !visibleMonth) return;

    const current = api.getDate();

    if (
      current.getFullYear() !== visibleMonth.getFullYear() ||
      current.getMonth() !== visibleMonth.getMonth()
    ) {
      api.gotoDate(visibleMonth);
    }
  }, [visibleMonth]);

  return (
    <div className="rounded-xl border border-(--color-border) bg-(--color-bg) p-4">
      <FullCalendar
        ref={calendarRef}
        plugins={[dayGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        locale={calendarLocale}
        titleFormat={(date) => {
          const formatted = new Intl.DateTimeFormat(i18n.resolvedLanguage, {
            month: 'long',
            year: 'numeric',
          }).format(date.date.marker);

          return i18n.resolvedLanguage === 'hr'
            ? formatted.charAt(0).toUpperCase() + formatted.slice(1)
            : formatted;
        }}
        firstDay={1}
        height="auto"
        fixedWeekCount={false}
        showNonCurrentDates={false}
        displayEventTime={true}
        events={events}
        selectable={variant !== 'HOUR'}
        selectMirror={true}
        dateClick={handleDateClick}
        select={variant !== 'HOUR' ? handleDateRangeSelect : undefined}
        eventClick={handleEventClick}

        datesSet={(info) => {
          const current = info.view.currentStart;

          if (
            current.getFullYear() !== visibleMonth.getFullYear() ||
            current.getMonth() !== visibleMonth.getMonth()
          ) {
            onMonthChange?.(current);
          }
        }}
        selectAllow={(selectInfo) => {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          return (
            selectInfo.start >= today && selectInfo.start <= maxBookingDate
          );
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
          const isUnavailable = !isSelectableDate(arg.date);

          return [
            'transition-all duration-150',
            isUnavailable
              ? 'pointer-events-none bg-gray-100 text-gray-400 opacity-50'
              : 'cursor-pointer hover:bg-blue-50',

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
