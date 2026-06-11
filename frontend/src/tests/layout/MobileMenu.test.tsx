import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import MobileMenu from '../../components/layout/MobileMenu';
import { useCurrentUser } from '../../features/user/hooks/useCurrentUser';
import { isAdmin, isManager } from '../../features/user/utilis/users';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return { ...actual, useNavigate: () => mockNavigate };
});

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

vi.mock('../../components/icons/Logo', () => ({
  Logo: ({ className }: React.SVGProps<SVGSVGElement>) => (
    <svg className={className} aria-label="Logo" />
  ),
}));

vi.mock('../../components/ui/LanguageSwitcher', () => ({
  default: () => <div>LanguageSwitcher</div>,
}));

vi.mock('../../components/ui/ThemeToggle', () => ({
  default: () => <button>ThemeToggle</button>,
}));

vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick, className }: React.ButtonHTMLAttributes<HTMLButtonElement>) => (
    <button onClick={onClick} className={className}>{children}</button>
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

vi.mock('@mui/icons-material', () => ({
  MonitorSharp: () => <svg />,
  DnsSharp: () => <svg />,
  CalendarTodaySharp: () => <svg />,
  PeopleSharp: () => <svg />,
  LogoutSharp: () => <svg />,
  AccountCircleSharp: () => <svg />,
  HowToRegSharp: () => <svg />,
  EventNoteSharp: () => <svg />,
}));

const renderMenu = () =>
  render(
    <MemoryRouter>
      <MobileMenu />
    </MemoryRouter>
  );

describe('MobileMenu', () => {
  afterEach(() => {
    vi.mocked(isAdmin).mockReturnValue(false);
    vi.mocked(isManager).mockReturnValue(false);
    vi.mocked(useCurrentUser).mockReturnValue({ user: null, isLoading: false, error: null });
  });

  it('renders the trigger button', () => {
    renderMenu();
    expect(screen.getByRole('button', { name: '' })).toBeInTheDocument();
  });

  it('opens the menu when trigger is clicked', async () => {
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.getByRole('navigation')).toBeInTheDocument();
  });

  it('renders nav links after opening', async () => {
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.getByText('layout.navbar.assets')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.categories')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.bookings')).toBeInTheDocument();
    expect(screen.getByText('layout.navbar.myBookings')).toBeInTheDocument();
  });

  it('does not render users link for non-admin', async () => {
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.queryByText('layout.navbar.users')).not.toBeInTheDocument();
  });

  it('does not render approvals link for non-manager', async () => {
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.queryByText('layout.navbar.approvals')).not.toBeInTheDocument();
  });

  it('renders users link for admin', async () => {
    vi.mocked(isAdmin).mockReturnValue(true);
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.getByText('layout.navbar.users')).toBeInTheDocument();
  });

  it('renders approvals link for manager', async () => {
    vi.mocked(isManager).mockReturnValue(true);
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.getByText('layout.navbar.approvals')).toBeInTheDocument();
  });

  it('renders logout button after opening', async () => {
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.getByText('layout.navbar.logout')).toBeInTheDocument();
  });

  it('clears auth cookie and navigates to /login on logout', async () => {
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    await userEvent.click(screen.getByText('layout.navbar.logout'));
    expect(mockNavigate).toHaveBeenCalledWith('/login');
  });

  it('renders account-info link after opening', async () => {
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    const accountLink = screen.getByRole('link', { name: /layout\.navbar\.account/i });
    expect(accountLink).toHaveAttribute('href', '/account-info');
  });

  it('renders user full name when user is logged in', async () => {
    vi.mocked(useCurrentUser).mockReturnValue({
      user: { id: 1, role: 'admin', firstName: 'Test', lastName: 'User' } as any,
      isLoading: false,
      error: null,
    });
    renderMenu();
    await userEvent.click(screen.getByRole('button', { name: '' }));
    expect(screen.getByText('Test User')).toBeInTheDocument();
  });
});