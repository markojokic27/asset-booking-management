import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => (
    <div className={className}>{children}</div>
  ),
}));
vi.mock('../../features/user/hooks/useCurrentUser', () => ({ useCurrentUser: vi.fn() }));
vi.mock('../../features/user/utilis/users', () => ({
  getFullName: vi.fn(() => 'Test User'),
  isAdmin: vi.fn(),
  isManager: vi.fn(),
}));
vi.mock('@mui/icons-material/MonitorSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/CalendarTodaySharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/PeopleSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/LogoutSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/DnsSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/AssessmentSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/HowToRegSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/EventNoteSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material', () => ({ AccountCircleSharp: () => <svg /> }));

import { Navbar } from '../../components/layout/Navbar';
import { useCurrentUser } from '../../features/user/hooks/useCurrentUser';
import { isAdmin, isManager } from '../../features/user/utilis/users';

const renderNavbar = (initialEntries = ['/']) =>
  render(<MemoryRouter initialEntries={initialEntries}><Navbar /></MemoryRouter>);

describe('Navbar', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(isAdmin).mockReturnValue(false);
    vi.mocked(isManager).mockReturnValue(false);
    vi.mocked(useCurrentUser).mockReturnValue({ user: null, isLoading: false, error: null });
  });

  it('renders navigation with default links and correct hrefs', () => {
    renderNavbar();
    expect(screen.getByRole('navigation')).toBeInTheDocument();
    for (const key of ['assets', 'categories', 'bookings', 'myBookings', 'report', 'logout']) {
      expect(screen.getByText(`layout.navbar.${key}`)).toBeInTheDocument();
    }
    expect(screen.getByRole('link', { name: /layout\.navbar\.account/i })).toHaveAttribute('href', '/account-info');
    expect(screen.getByRole('link', { name: /layout\.navbar\.logout/i })).toHaveAttribute('href', '/login');
  });

  it.each([
    ['users',     'admin',   () => vi.mocked(isAdmin).mockReturnValue(true),   'layout.navbar.users'],
    ['approvals', 'manager', () => vi.mocked(isManager).mockReturnValue(true), 'layout.navbar.approvals'],
  ])('renders %s link only for %s', (_, __, setup, linkText) => {
    renderNavbar();
    expect(screen.queryByText(linkText)).not.toBeInTheDocument();
    setup();
    renderNavbar();
    expect(screen.getAllByText(linkText)[0]).toBeInTheDocument();
  });

  it('shows allBookings and hides myBookings for admin', () => {
    vi.mocked(isAdmin).mockReturnValue(true);
    renderNavbar();
    expect(screen.getByText('layout.navbar.allBookings')).toBeInTheDocument();
    expect(screen.queryByText('layout.navbar.myBookings')).not.toBeInTheDocument();
  });

  it('renders user full name and role when logged in, account key when not', () => {
    renderNavbar();
    expect(screen.getByText('layout.navbar.account')).toBeInTheDocument();

    vi.mocked(useCurrentUser).mockReturnValue({
      user: { id: 1, role: 'admin', firstName: 'Test', lastName: 'User' } as any,
      isLoading: false,
      error: null,
    });
    renderNavbar();
    expect(screen.getByText('Test User')).toBeInTheDocument();
    expect(screen.getByText('admin')).toBeInTheDocument();
  });

  it('applies inactive styles by default and active styles on current route', () => {
    renderNavbar();
    const inactive = screen.getByRole('link', { name: /layout\.navbar\.assets/i });
    expect(inactive).toHaveClass('border-transparent');
    expect(inactive).not.toHaveClass('shadow-card');

    renderNavbar(['/assets']);
    const active = screen.getAllByRole('link', { name: /layout\.navbar\.assets/i })[1];
    expect(active).toHaveClass('shadow-card');
    expect(active).not.toHaveClass('border-transparent');
  });
});