import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { BookingWithRelations } from '../../features/booking/types';

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../features/booking/hooks/useMyBookings', () => ({ useMyBookings: vi.fn() }));
vi.mock('../../features/user/hooks/usePagination', () => ({
  usePagination: vi.fn((items: unknown[]) => ({
    paged: items, page: 1, totalPages: 1, items, setPage: vi.fn(),
  })),
}));
vi.mock('../../features/user/utils/users', () => ({ isAdmin: vi.fn() }));
vi.mock('../../features/booking/utils/approvalFilter', () => ({
  filterBookingsByAsset: vi.fn((b: unknown[]) => b),
  filterBookingsByDateRange: vi.fn((b: unknown[]) => b),
  filterBookingsByStatus: vi.fn((b: unknown[]) => b),
  filterPendingBookingsBySearch: vi.fn((b: unknown[]) => b),
}));
vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: any) => <div>{children}</div>,
}));
vi.mock('../../components/ui/SearchBar', () => ({
  SearchInput: ({ value, onChange, placeholder }: any) => (
    <input placeholder={placeholder} value={value} onChange={(e) => onChange(e.target.value)} />
  ),
}));
vi.mock('../../components/ui/FormDropdown', () => ({
  FormDropdown: ({ value, onChange, options, 'aria-label': ariaLabel }: any) => (
    <select aria-label={ariaLabel} value={value} onChange={onChange}>
      {options.map((o: any) => <option key={o.value} value={o.value}>{o.label}</option>)}
    </select>
  ),
}));
vi.mock('../../components/ui/Pagination', () => ({
  Pagination: ({ page, totalPages, onPageChange }: any) => (
    <nav>
      <span>{page}/{totalPages}</span>
      <button onClick={() => onPageChange(page + 1)}>next</button>
    </nav>
  ),
}));
vi.mock('../../features/booking/components/FilterDateInput', () => ({
  FilterDateInput: ({ id, label, value, onChange }: any) => (
    <input id={id} aria-label={label} value={value} onChange={(e) => onChange(e.target.value)} />
  ),
}));
vi.mock('../../features/booking/components/MyBookingsTable', () => ({
  MyBookingsTable: ({ bookings, isLoading, error }: any) => (
    <div data-testid="bookings-table">
      {isLoading && <span>loading</span>}
      {error && <span>{error}</span>}
      {bookings.map((b: any) => <div key={b.id}>{b.asset.name}</div>)}
    </div>
  ),
}));

import MyBookings from '../../pages/MyBookings';
import { authState, mockUseAuth } from '../mocks/auth';
import { useMyBookings } from '../../features/booking/hooks/useMyBookings';
import { isAdmin } from '../../features/user/utils/users';
import {
  filterBookingsByAsset,
  filterBookingsByDateRange,
  filterBookingsByStatus,
  filterPendingBookingsBySearch,
} from '../../features/booking/utils/approvalFilter';


const mockUser = { id: 1, role: 'EMPLOYEE' };

const makeBooking = (id: number, assetId: number, assetName: string): BookingWithRelations => ({
  id: String(id),
  userId: 1,
  assetId,
  status: 'APPROVED',
  notes: '',
  createdAt: new Date(),
  lastModifiedAt: new Date(),
  bookingStart: new Date('2025-06-01'),
  bookingEnd: new Date('2025-06-02'),
  userName: 'Alice',
  assetName,
  assetCategory: 'Office',
  user: { id: 1, name: 'Alice', surname: 'Smith', email: '', role: 'EMPLOYEE', managerEmail: '' },
  asset: {
    id: assetId,
    name: assetName,
    categoryId: 1,
    status: 'ACTIVE',
    description: '',
    location: '',
    category: { id: 1, name: 'Office', bookingPeriod: 'DAY', approval: false },
  },
});

const bookingA = makeBooking(1, 10, 'Laptop');
const bookingB = makeBooking(2, 20, 'Projector');

const setUser = (overrides = {}) =>
  mockUseAuth.mockReturnValue(authState({ user: { ...mockUser, ...overrides } as any }));

const setBookings = (bookings: BookingWithRelations[] = [bookingA, bookingB]) =>
  vi.mocked(useMyBookings).mockReturnValue({
    bookings,
    loading: false,
    error: null,
    refetch: vi.fn().mockResolvedValue(undefined),
  });

const renderPage = () => render(<MyBookings />);

// Tests 

describe('MyBookings', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    setUser();
    setBookings();
    vi.mocked(isAdmin).mockReturnValue(false);
  });

  describe('rendering', () => {
    it('renders title and filters', () => {
      renderPage();
      expect(screen.getByText('myBookings.title')).toBeInTheDocument();
      expect(screen.getByPlaceholderText('myBookings.search.placeholder')).toBeInTheDocument();
      expect(screen.getByLabelText('myBookings.filter.fromDate')).toBeInTheDocument();
      expect(screen.getByLabelText('myBookings.filter.toDate')).toBeInTheDocument();
      expect(screen.getByLabelText('myBookings.filter.asset')).toBeInTheDocument();
      expect(
        screen.queryByLabelText('myBookings.filter.status')
      ).not.toBeInTheDocument();
    });

    it('renders bookings in table', () => {
      renderPage();
      const table = screen.getByTestId('bookings-table');
      expect(table).toHaveTextContent('Laptop');
      expect(table).toHaveTextContent('Projector');
    });

    it('shows admin title for admin user', () => {
      vi.mocked(isAdmin).mockReturnValue(true);
      renderPage();
      expect(screen.getByText('myBookings.titleAdmin')).toBeInTheDocument();
    });

    it('shows status filter for admin user', () => {
      vi.mocked(isAdmin).mockReturnValue(true);
      renderPage();
      expect(screen.getByLabelText('myBookings.filter.status')).toBeInTheDocument();
    });

    it.each([
      ['loading', { loading: true }, 'loading'],
      ['error',   { error: 'fetch error' }, 'fetch error'],
    ])('shows %s state', (_, overrides, expectedText) => {
      vi.mocked(useMyBookings).mockReturnValue({
        bookings: [],
        loading: false,
        error: null,
        refetch: vi.fn().mockResolvedValue(undefined),
        ...overrides,
      });
      renderPage();
      expect(screen.getByText(expectedText)).toBeInTheDocument();
    });

    it('shows pagination when bookings exist', () => {
      renderPage();
      expect(screen.getByRole('navigation')).toBeInTheDocument();
    });

    it('hides pagination when no bookings', () => {
      setBookings([]);
      renderPage();
      expect(screen.queryByRole('navigation')).not.toBeInTheDocument();
    });
  });

  describe('asset dropdown', () => {
    it('renders all asset options sorted alphabetically', () => {
      renderPage();
      const options = screen.getAllByRole('option');
      expect(options[0]).toHaveTextContent('myBookings.filter.allAssets');
      expect(options[1]).toHaveTextContent('Laptop');
      expect(options[2]).toHaveTextContent('Projector');
    });

    it('calls filterBookingsByAsset with selected asset id', () => {
      renderPage();
      fireEvent.change(screen.getByLabelText('myBookings.filter.asset'), { target: { value: '10' } });
      expect(filterBookingsByAsset).toHaveBeenCalledWith(expect.any(Array), 10);
    });

    it('calls filterBookingsByAsset with null when all assets selected', () => {
      renderPage();
      fireEvent.change(screen.getByLabelText('myBookings.filter.asset'), { target: { value: '' } });
      expect(filterBookingsByAsset).toHaveBeenCalledWith(expect.any(Array), null);
    });
  });

  describe('date filters', () => {
    it('calls filterBookingsByDateRange when fromDate changes', () => {
      renderPage();
      fireEvent.change(screen.getByLabelText('myBookings.filter.fromDate'), { target: { value: '2025-06-01' } });
      expect(filterBookingsByDateRange).toHaveBeenCalledWith(expect.any(Array), '2025-06-01', '');
    });

    it('calls filterBookingsByDateRange when toDate changes', () => {
      renderPage();
      fireEvent.change(screen.getByLabelText('myBookings.filter.toDate'), { target: { value: '2025-06-30' } });
      expect(filterBookingsByDateRange).toHaveBeenCalledWith(expect.any(Array), '', '2025-06-30');
    });
  });

  describe('status filter', () => {
    beforeEach(() => {
      vi.mocked(isAdmin).mockReturnValue(true);
    });

    it('calls filterBookingsByStatus with selected status', () => {
      renderPage();
      fireEvent.change(screen.getByLabelText('myBookings.filter.status'), {
        target: { value: 'PENDING' },
      });
      expect(filterBookingsByStatus).toHaveBeenCalledWith(expect.any(Array), 'PENDING');
    });

    it('calls filterBookingsByStatus with empty string when all statuses selected', () => {
      renderPage();
      fireEvent.change(screen.getByLabelText('myBookings.filter.status'), {
        target: { value: '' },
      });
      expect(filterBookingsByStatus).toHaveBeenCalledWith(expect.any(Array), '');
    });
  });

  describe('search', () => {
    it('calls filterPendingBookingsBySearch with search term', () => {
      renderPage();
      fireEvent.change(screen.getByPlaceholderText('myBookings.search.placeholder'), { target: { value: 'lap' } });
      expect(filterPendingBookingsBySearch).toHaveBeenCalledWith(expect.any(Array), 'lap');
    });
  });
});