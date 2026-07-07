import { render, screen, cleanup } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return { ...actual, useNavigate: () => mockNavigate };
});
vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../components/icons/Logo', () => ({
  Logo: ({ className }: React.SVGProps<SVGSVGElement>) => <svg className={className} aria-label="Logo" />,
}));
vi.mock('../../components/ui/LanguageSwitcher', () => ({ default: () => <div>LanguageSwitcher</div> }));
vi.mock('../../components/ui/ThemeToggle', () => ({ default: () => <button>ThemeToggle</button> }));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick, className }: React.ButtonHTMLAttributes<HTMLButtonElement>) => (
    <button onClick={onClick} className={className}>{children}</button>
  ),
}));
vi.mock('../../features/user/utilis/users', async (importOriginal) => {
  const actual = await importOriginal<typeof import('../../features/user/utilis/users')>();
  return {
    ...actual,
    getFullName: vi.fn(() => 'Test User'),
    isAdmin: vi.fn(),
    isManager: vi.fn(),
  };
});
vi.mock('@mui/icons-material', () => ({
  MonitorSharp: () => <svg />, DnsSharp: () => <svg />, CalendarTodaySharp: () => <svg />,
  PeopleSharp: () => <svg />, LogoutSharp: () => <svg />, AccountCircleSharp: () => <svg />,
  HowToRegSharp: () => <svg />, EventNoteSharp: () => <svg />,
}));

import MobileMenu from '../../components/layout/MobileMenu';
import { authState, mockUseAuth } from '../mocks/auth';
import { isAdmin, isManager } from '../../features/user/utilis/users';

const adminUser = { id: 1, role: 'ADMIN', name: 'Test', surname: 'User' } as const;

const renderMenu = () => render(<MemoryRouter><MobileMenu /></MemoryRouter>);
const openMenu = () => userEvent.click(screen.getByRole('button', { name: '' }));

describe('MobileMenu', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(isAdmin).mockReturnValue(false);
    vi.mocked(isManager).mockReturnValue(false);
    mockUseAuth.mockReturnValue(authState({ user: adminUser as any }));
  });

  it('renders trigger button and opens menu on click', async () => {
    renderMenu();
    expect(screen.getByRole('button', { name: '' })).toBeInTheDocument();
    await openMenu();
    expect(screen.getByRole('navigation')).toBeInTheDocument();
  });

  it('renders common nav links, logout and account link after opening', async () => {
    renderMenu();
    await openMenu();
    for (const key of ['layout.navbar.assets', 'layout.navbar.categories', 'layout.navbar.bookings', 'layout.navbar.myBookings', 'layout.navbar.logout']) {
      expect(screen.getByText(key)).toBeInTheDocument();
    }
    expect(screen.getByRole('link', { name: /Test User/i })).toHaveAttribute('href', '/account-info');
  });

  it.each([
    ['users',     'admin',   () => vi.mocked(isAdmin).mockReturnValue(true),   'layout.navbar.users'],
    ['approvals', 'manager', () => vi.mocked(isManager).mockReturnValue(true), 'layout.navbar.approvals'],
  ])('renders %s link only for %s', async (_, __, setup, linkText) => {
    renderMenu();
    await openMenu();
    expect(screen.queryByText(linkText)).not.toBeInTheDocument();

    cleanup();
    setup();
    renderMenu();
    await openMenu();
    expect(screen.getByText(linkText)).toBeInTheDocument();
  });

  it('navigates to /login on logout click', async () => {
    renderMenu();
    await openMenu();
    await userEvent.click(screen.getByText('layout.navbar.logout'));
    expect(mockNavigate).toHaveBeenCalledWith('/login');
  });

  it('renders user full name when logged in', async () => {
    mockUseAuth.mockReturnValue(authState({
      user: { id: 1, role: 'ADMIN', name: 'Test', surname: 'User' } as any,
    }));
    renderMenu();
    await openMenu();
    expect(screen.getByText('Test User')).toBeInTheDocument();
  });
});