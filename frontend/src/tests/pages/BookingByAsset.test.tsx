import * as React from 'react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import type { BookingWithRelations } from '../../features/booking/types';

const mockHandleCreateBooking = vi.fn();
const mockSetFilters = vi.fn();
const mockHandleCalendarDateClick = vi.fn();
let capturedCalendarProps: Record<string, unknown> = {};

const hookState = {
  bookings: [] as BookingWithRelations[],
  loading: false,
  error: null as Error | null,
  refetch: vi.fn(),
  isButtonDisabled: false,
  isCreating: false,
};

vi.mock('react-router-dom', () => ({ useParams: () => ({ assetId: '42' }) }));
vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (k: string) => k,
    i18n: { language: 'en' },
  }),
}));
vi.mock('../../features/booking/hooks/useBookingByAsset', () => ({
  useBookingsByAsset: () => ({
    bookings: hookState.bookings,
    loading: hookState.loading,
    error: hookState.error,
    refetch: hookState.refetch,
  }),
}));
vi.mock('../../features/booking/hooks/useBookingFilters', () => ({
  useBookingFilters: () => ({
    filters: { search: '', fromDate: '', toDate: '', fromHour: '', toHour: '', selectedWeekdays: [] },
    setFilters: mockSetFilters,
    handleCalendarDateClick: mockHandleCalendarDateClick,
  }),
}));
vi.mock('../../features/booking/hooks/useBookingAvailability', () => ({
  useBookingAvailability: () => hookState.isButtonDisabled,
}));
vi.mock('../../features/booking/hooks/useCreateBooking', () => ({
  useCreateBooking: () => ({
    isCreating: hookState.isCreating,
    handleCreateBooking: mockHandleCreateBooking,
  }),
}));
vi.mock('../../features/booking/utils/bookingLogic', () => ({ mapBookingsToCalendarEvents: () => [] }));
vi.mock('../../features/booking/utils/getDatesForWeekdays', () => ({ getDatesForWeekdays: () => [] }));
vi.mock('../../features/booking/utils/getAvailableRecurringDates', () => ({ getAvailableRecurringDates: () => [] }));
vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}));
vi.mock('../../features/booking/components/AvailabilityCalendar', () => ({
  AvailabilityCalendar: (props: Record<string, unknown>) => {
    capturedCalendarProps = props;
    return <div data-testid="availability-calendar" />;
  },
}));
vi.mock('../../features/booking/components/FilterBar', () => ({ FiltersBar: () => <div data-testid="filters-bar" /> }));
vi.mock('../../features/booking/components/RecurringDaysSelector', () => ({ RecurringDaysSelector: () => <div data-testid="recurring-days-selector" /> }));
vi.mock('../../features/booking/components/BookingDetailsModal', () => ({
  BookingDetailsModal: ({ onClose }: { onClose: () => void }) => (
    <div data-testid="booking-details-modal">
      <button onClick={onClose}>Close</button>
    </div>
  ),
}));
vi.mock('../../features/booking/components/BookingModal', () => ({
  BookingModal: ({ open, handleCreateBooking }: { open: boolean; handleCreateBooking: () => Promise<boolean> }) =>
    open ? (
      <div data-testid="booking-modal">
        <button onClick={() => void handleCreateBooking()}>confirm-booking</button>
      </div>
    ) : null,
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick, disabled, ...props }: React.ButtonHTMLAttributes<HTMLButtonElement> & { children: React.ReactNode }) => (
    <button onClick={onClick} disabled={disabled} {...props}>{children}</button>
  ),
}));
vi.mock('../../components/ui/Input', () => ({
  Input: (props: React.InputHTMLAttributes<HTMLInputElement>) => <input {...props} />,
}));

import BookingsByAsset from '../../pages/BookingByAsset';

const makeBooking = (assetOverrides: Partial<BookingWithRelations['asset']> = {}): BookingWithRelations => ({
  id: '1',
  userId: 1,
  assetId: 42,
  bookingStart: new Date('2025-06-01T09:00:00'),
  bookingEnd: new Date('2025-06-01T17:00:00'),
  status: 'APPROVED',
  notes: '',
  createdAt: new Date('2025-05-01'),
  lastModifiedAt: new Date('2025-05-01'),
  userName: 'Test User',
  assetName: 'Test Asset',
  assetCategory: 'Office',
  user: { id: 1, name: 'Test', surname: 'User', email: 'test@example.com', role: 'USER', managerEmail: 'manager@example.com' },
  asset: {
    id: 42,
    name: 'Test Asset',
    categoryId: 1,
    status: 'ACTIVE',
    description: 'Test description',
    location: 'Building A',
    category: { id: 1, name: 'Office', bookingPeriod: 'DAY', approval: false },
    ...assetOverrides,
  },
});

const defaultBooking = makeBooking();
const parkingBooking = makeBooking({ category: { id: 1, name: 'Parking', bookingPeriod: 'DAY', approval: false } });
const hourBooking = makeBooking({ category: { id: 1, name: 'Office', bookingPeriod: 'HOUR', approval: false } });
const inactiveBooking = makeBooking({ status: 'INACTIVE' });

const defaultHookState = {
  bookings: [defaultBooking],
  loading: false,
  error: null,
  isButtonDisabled: false,
  isCreating: false,
};

describe('BookingsByAsset', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    capturedCalendarProps = {};
    Object.assign(hookState, defaultHookState);
  });

  describe('conditional render states', () => {
    it.each([
      ['loading', { loading: true, bookings: [defaultBooking] }, 'bookings.buttons.loading'],
      ['error', { error: new Error('fail'), bookings: [defaultBooking] }, 'bookings.buttons.errorLoadingBookings'],
    ])('shows %s state', (_, state, expected) => {
      Object.assign(hookState, state);
      render(<BookingsByAsset />);
      expect(screen.getByText(expected)).toBeInTheDocument();
    });
  });

  describe('main view', () => {
    it('renders asset info and key UI elements', () => {
      render(<BookingsByAsset />);
      expect(screen.getByText('Test Asset')).toBeInTheDocument();
      expect(screen.getByText(/building a/i)).toBeInTheDocument();
      expect(screen.getByTestId('filters-bar')).toBeInTheDocument();
      expect(screen.getByTestId('availability-calendar')).toBeInTheDocument();
      expect(screen.getByTestId('booking-details-modal')).toBeInTheDocument();
      expect(screen.getByPlaceholderText('ui.notes.placeholder')).toBeInTheDocument();
      expect(screen.getByTestId('book-asset-button')).toBeInTheDocument();
    });

    it.each([
      ['ACTIVE', defaultBooking, 'bg-green-100', 'text-green-700'],
      ['INACTIVE', inactiveBooking, 'bg-gray-200', 'text-gray-700'],
    ])('renders %s badge with correct styling', (status, booking, bgClass, textClass) => {
      hookState.bookings = [booking];
      render(<BookingsByAsset />);
      const badge = screen.getByText(status);
      expect(badge.className).toContain(bgClass);
      expect(badge.className).toContain(textClass);
    });
  });

  describe('RecurringDaysSelector', () => {
    it('renders only for Parking category', () => {
      hookState.bookings = [parkingBooking];
      render(<BookingsByAsset />);
      expect(screen.getByTestId('recurring-days-selector')).toBeInTheDocument();
    });

    it('does not render for non-Parking category', () => {
      render(<BookingsByAsset />);
      expect(screen.queryByTestId('recurring-days-selector')).not.toBeInTheDocument();
    });
  });

  describe('Book button', () => {
    it.each([
      ['enabled by default',          { isButtonDisabled: false, isCreating: false }, false, 'bookings.buttons.book'],
      ['disabled when unavailable',   { isButtonDisabled: true,  isCreating: false }, true,  'bookings.buttons.book'],
      ['disabled and labeled while creating', { isButtonDisabled: false, isCreating: true  }, true,  'bookings.buttons.booking'],
    ])('is %s', (_, state, expectedDisabled, expectedLabel) => {
      Object.assign(hookState, state);
      render(<BookingsByAsset />);
      const btn = screen.getByTestId('book-asset-button');
      expect(btn).toHaveTextContent(expectedLabel);
      expectedDisabled ? expect(btn).toBeDisabled() : expect(btn).not.toBeDisabled();
    });

    it('calls handleCreateBooking on click', async () => {
      render(<BookingsByAsset />);
      const user = userEvent.setup();
      await user.click(screen.getByTestId('book-asset-button'));
      await user.click(screen.getByText('confirm-booking'));
      expect(mockHandleCreateBooking).toHaveBeenCalledOnce();
    });
  });

  describe('notes input', () => {
    it('starts empty and updates on input', async () => {
      render(<BookingsByAsset />);
      const input = screen.getByPlaceholderText('ui.notes.placeholder');
      expect(input).toHaveValue('');
      await userEvent.setup().type(input, 'My note');
      expect(input).toHaveValue('My note');
    });
  });

  describe('AvailabilityCalendar props', () => {
    it.each([
      ['DAY', defaultBooking],
      ['HOUR', hourBooking],
    ])('passes %s variant', (variant, booking) => {
      hookState.bookings = [booking];
      render(<BookingsByAsset />);
      expect(capturedCalendarProps.variant).toBe(variant);
    });

    it('passes correct initial props', () => {
      render(<BookingsByAsset />);
      expect(capturedCalendarProps.events).toEqual([]);
      expect(capturedCalendarProps.selectedFromDate).toBe('');
      expect(capturedCalendarProps.selectedToDate).toBe('');
      expect(capturedCalendarProps.availableRecurringDates).toEqual([]);
    });

    it('forwards onDateClick handler', () => {
      render(<BookingsByAsset />);
      (capturedCalendarProps.onDateClick as () => void)();
      expect(mockHandleCalendarDateClick).toHaveBeenCalledOnce();
    });

    it('onRangeSelect resets selectedWeekdays and sets dates', () => {
      render(<BookingsByAsset />);
      (capturedCalendarProps.onRangeSelect as (f: string, t: string) => void)('2025-06-01', '2025-06-05');

      const result = mockSetFilters.mock.calls[0][0]({
        search: '',
        fromDate: '',
        toDate: '',
        fromHour: '',
        toHour: '',
        selectedWeekdays: [1, 2],
      });

      expect(result).toEqual({
        search: '',
        fromDate: '2025-06-01',
        toDate: '2025-06-05',
        fromHour: '',
        toHour: '',
        selectedWeekdays: [],
      });
    });
  });
});