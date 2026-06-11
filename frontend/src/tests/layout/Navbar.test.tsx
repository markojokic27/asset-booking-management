import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import { Navbar } from '../../components/layout/Navbar';
import { useCurrentUser } from '../../features/user/hooks/useCurrentUser';
import { isAdmin, isManager } from '../../features/user/utilis/users';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => (
    <div className={className}>{children}</div>
  ),
}));

vi.mock('../../features/user/hooks/useCurrentUser', () => ({
  useCurrentUser: vi.fn(() => ({ user: null, isLoading: false, error: null })),
}));

vi.mock('../../features/user/utilis/users', () => ({
  getFullName: vi.fn(() => 'Test User'),
  isAdmin: vi.fn(() => false),
  isManager: vi.fn(() => false),
}));

vi.mock('@mui/icons-material/MonitorSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/CalendarTodaySharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/PeopleSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/LogoutSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/DnsSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/AssessmentSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/HowToRegSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/EventNoteSharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material', () => ({
  AccountCircleSharp: () => <svg />,
}));

const renderNavbar = () =>
  render(
    <MemoryRouter>
      <Navbar />
    </MemoryRouter>
  );

describe('Navbar', () => {
  afterEach(() => {
    vi.mocked(isAdmin).mockReturnValue(false);
    vi.mocked(isManager).mockReturnValue(false);
    vi.mocked(useCurrentUser).mockReturnValue({ user: null, isLoading: false, error: null });
  });

  it('renders the navigation element', () => {
    renderNavbar();
    expect(screen.getByRole('navigation')).toBeInTheDocument();
  });

  it('renders default nav links', () => {
    renderNavbar();
    expect(screen.getByText('layout.navbar.assets')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.categories')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.bookings')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.myBookings')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.report')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.logout')).toBeInTheDocument();
  });

  it('does not render users link for non-admin', () => {
    renderNavbar();
    expect(screen.queryByText('layout.navbar.users')).not.toBeInTheDocument();
  });

  it('does not render approvals link for non-manager', () => {
    renderNavbar();
    expect(screen.queryByText('layout.navbar.approvals')).not.toBeInTheDocument();
  });

  it('renders users link for admin', () => {
    vi.mocked(isAdmin).mockReturnValue(true);
    renderNavbar();
    expect(screen.getByText('layout.navbar.users')).toBeInTheDocument();
  });

  it('renders allBookings label for admin', () => {
    vi.mocked(isAdmin).mockReturnValue(true);
    renderNavbar();
    expect(screen.getByText('layout.navbar.allBookings')).toBeInTheDocument();
    expect(screen.queryByText('layout.navbar.myBookings')).not.toBeInTheDocument();
  });

  it('renders approvals link for manager', () => {
    vi.mocked(isManager).mockReturnValue(true);
    renderNavbar();
    expect(screen.getByText('layout.navbar.approvals')).toBeInTheDocument();
  });

  it('renders account link pointing to /account-info', () => {
    renderNavbar();
    const accountLink = screen.getByRole('link', { name: /layout\.navbar\.account/i });
    expect(accountLink).toHaveAttribute('href', '/account-info');
  });

  it('renders logout link pointing to /login', () => {
    renderNavbar();
    const logoutLink = screen.getByRole('link', { name: /layout\.navbar\.logout/i });
    expect(logoutLink).toHaveAttribute('href', '/login');
  });

  it('renders user full name and role when logged in', () => {
    vi.mocked(useCurrentUser).mockReturnValue({
      user: { id: 1, role: 'admin', firstName: 'Test', lastName: 'User' } as any,
      isLoading: false,
      error: null,
    });
    renderNavbar();
    expect(screen.getByText('Test User')).toBeInTheDocument();
    expect(screen.getByText('admin')).toBeInTheDocument();
  });

  it('renders account translation key when no user', () => {
    renderNavbar();
    expect(screen.getByText('layout.navbar.account')).toBeInTheDocument();
  });

  it('applies inactive link styles by default', () => {
    renderNavbar();
    const link = screen.getByRole('link', { name: /layout\.navbar\.assets/i });
    expect(link).toHaveClass('border-transparent');
    expect(link).not.toHaveClass('shadow-card');
  });

  it('applies active link styles on current route', () => {
    render(
      <MemoryRouter initialEntries={['/assets']}>
        <Navbar />
      </MemoryRouter>
    );
    const link = screen.getByRole('link', { name: /layout\.navbar\.assets/i });
    expect(link).toHaveClass('shadow-card');
    expect(link).not.toHaveClass('border-transparent');
  });
});