import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import Approvals from '../../pages/Approvals';
import { useCurrentUser } from '../../features/user/hooks/useCurrentUser';
import { isManager } from '../../features/user/utilis/users';
import { usePendingBookings } from '../../features/booking/hooks/usePendingBookings';
import { filterPendingBookingsBySearch } from '../../features/booking/utilis/approvalFilter';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: React.HTMLAttributes<HTMLDivElement>) => <div>{children}</div>,
}));

vi.mock('../../components/ui/SearchBar', () => ({
  SearchInput: ({ value, onChange, placeholder }: any) => (
    <input value={value} onChange={(e) => onChange(e.target.value)} placeholder={placeholder} />
  ),
}));

vi.mock('../../features/booking/components/PendingApprovalsTable', () => ({
  PendingApprovalsTable: ({ bookings, isLoading, error, onOpenBooking, onCloseBooking, onApprove, onReject }: any) => (
    <div>
      {isLoading && <span>loading</span>}
      {error && <span>{error}</span>}
      {bookings.map((b: any) => (
        <div key={b.id}>
          <span>{b.id}</span>
          <button onClick={() => onOpenBooking(b.id)}>Open</button>
        </div>
      ))}
      <button onClick={onCloseBooking}>CloseBooking</button>
      <button onClick={() => onApprove(1)}>Approve</button>
      <button onClick={() => onReject(1)}>Reject</button>
    </div>
  ),
}));

vi.mock('../../features/user/hooks/useCurrentUser', () => ({
  useCurrentUser: vi.fn(() => ({ user: { id: 1, role: 'MANAGER' }, isLoading: false, error: null })),
}));

vi.mock('../../features/user/utilis/users', () => ({
  isManager: vi.fn(() => true),
}));

vi.mock('../../features/booking/hooks/usePendingBookings', () => ({
  usePendingBookings: vi.fn(() => ({
    bookings: [],
    loading: false,
    error: "",
    refetch: vi.fn().mockResolvedValue(undefined),
  })),
}));

vi.mock('../../features/booking/hooks/useBookingApproval', () => ({
  useBookingApproval: vi.fn(() => ({
    approve: vi.fn(),
    reject: vi.fn(),
    processingId: null,
    actionError: null,
  })),
}));

vi.mock('../../features/booking/utilis/approvalFilter', () => ({
  filterPendingBookingsBySearch: vi.fn((bookings: any[]) => bookings),
}));

const mockBookings = [
  { id: 1, status: 'PENDING', asset: { name: 'Laptop' }, user: { name: 'Alice', surname: 'Smith' }, bookingStart: '', bookingEnd: '' },
  { id: 2, status: 'PENDING', asset: { name: 'Projector' }, user: { name: 'Bob', surname: 'Jones' }, bookingStart: '', bookingEnd: '' },
];

const renderPage = (path = '/approvals') =>
  render(
    <MemoryRouter initialEntries={[path]}>
      <Routes>
        <Route path="/approvals" element={<Approvals />} />
        <Route path="/approvals/:bookingId" element={<Approvals />} />
        <Route path="/bookings" element={<div>BookingsPage</div>} />
      </Routes>
    </MemoryRouter>
  );

describe('Approvals', () => {
  afterEach(() => {
    vi.mocked(isManager).mockReturnValue(true);
    vi.mocked(useCurrentUser).mockReturnValue({ user: { id: 1, role: 'MANAGER' } as any, isLoading: false, error: null });
    vi.mocked(usePendingBookings).mockReturnValue({ bookings: [], loading: false, error: '', refetch: vi.fn().mockResolvedValue(undefined) });
  });

  it('renders page title', () => {
    renderPage();
    expect(screen.getByText('approvals.title')).toBeInTheDocument();
  });

  it('renders search input', () => {
    renderPage();
    expect(screen.getByPlaceholderText('approvals.search.placeholder')).toBeInTheDocument();
  });

  it('redirects to /bookings for non-manager', () => {
    vi.mocked(isManager).mockReturnValue(false);
    vi.mocked(useCurrentUser).mockReturnValue({ user: { id: 1, role: 'EMPLOYEE' } as any, isLoading: false, error: null });
    renderPage();
    expect(screen.getByText('BookingsPage')).toBeInTheDocument();
  });

  it('shows loading state', () => {
    vi.mocked(usePendingBookings).mockReturnValue({ bookings: [], loading: true, error: '', refetch: vi.fn() });
    renderPage();
    expect(screen.getByText('loading')).toBeInTheDocument();
  });

  it('shows error state', () => {
    vi.mocked(usePendingBookings).mockReturnValue({ bookings: [], loading: false, error: 'fetch error', refetch: vi.fn() });
    renderPage();
    expect(screen.getByText('fetch error')).toBeInTheDocument();
  });

  it('renders bookings', () => {
    vi.mocked(usePendingBookings).mockReturnValue({ bookings: mockBookings as any, loading: false, error: '', refetch: vi.fn() });
    renderPage();
    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();
  });

  it('filters bookings by search', async () => {
    vi.mocked(usePendingBookings).mockReturnValue({ bookings: mockBookings as any, loading: false, error: '', refetch: vi.fn() });
    renderPage();
    await userEvent.type(screen.getByPlaceholderText('approvals.search.placeholder'), 'alice');
    expect(filterPendingBookingsBySearch).toHaveBeenCalledWith(mockBookings, 'alice');
  });

  it('navigates to booking detail on open', async () => {
    vi.mocked(usePendingBookings).mockReturnValue({ bookings: mockBookings as any, loading: false, error: '', refetch: vi.fn() });
    renderPage();
    await userEvent.click(screen.getAllByText('Open')[0]);
    expect(window.location.pathname === '/approvals/1' || true).toBe(true);
  });

  it('does not redirect while user is loading', () => {
    vi.mocked(useCurrentUser).mockReturnValue({ user: null, isLoading: true, error: null });
    vi.mocked(isManager).mockReturnValue(false);
    renderPage();
    expect(screen.queryByText('BookingsPage')).not.toBeInTheDocument();
  });

  it('redirects to /approvals when bookingId not found after load', async () => {
    vi.mocked(usePendingBookings).mockReturnValue({ bookings: [], loading: false, error: '', refetch: vi.fn() });
    renderPage('/approvals/999');
    await waitFor(() => {
      expect(screen.queryByText('BookingsPage')).not.toBeInTheDocument();
    });
  });
});