import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import UserMenu from '../../components/ui/UserMenu';

const mockNavigate = vi.fn();

vi.mock('react-router-dom', () => ({ useNavigate: () => mockNavigate }));
vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string) => ({
      'ui.userMenu.ariaLabel': 'User menu',
      'ui.userMenu.accountInfo': 'Account Info',
      'ui.userMenu.logout': 'Logout',
    }[key] ?? key),
  }),
}));
vi.mock('@mui/icons-material/AccountCircleOutlined', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/LogoutOutlined', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/VisibilityOutlined', () => ({ default: () => <svg /> }));
vi.mock('../icons/ChevronDown', () => ({ ChevronDown: () => <svg /> }));

const setup = () => {
  render(<UserMenu />);
  const trigger = screen.getByRole('button', { name: 'User menu' });
  const open = () => userEvent.click(trigger);
  return { trigger, open };
};

describe('UserMenu', () => {
  beforeEach(() => mockNavigate.mockClear());

  it('renders the trigger, menu closed by default', () => {
    const { trigger } = setup();
    expect(trigger).toBeInTheDocument();
    expect(screen.queryByText('Account Info')).not.toBeInTheDocument();
  });

  it('opens the menu on click and closes on outside click', async () => {
    const { open } = setup();
    await open();
    expect(screen.getByText('Account Info')).toBeVisible();
    expect(screen.getByText('Logout')).toBeVisible();

    await userEvent.click(document.body);
    await waitFor(() => expect(screen.queryByText('Account Info')).not.toBeInTheDocument());
  });

  it('opens with Enter/Space and closes with Escape', async () => {
    const { trigger } = setup();
    trigger.focus();

    await userEvent.keyboard('{Enter}');
    expect(screen.getByText('Account Info')).toBeVisible();

    await userEvent.keyboard('{Escape}');
    await waitFor(() => expect(screen.queryByText('Account Info')).not.toBeInTheDocument());

    trigger.focus();
    await userEvent.keyboard(' ');
    expect(screen.getByText('Account Info')).toBeVisible();
  });

  it.each([
    ['Account Info', '/account-info'],
    ['Logout', '/login'],
  ])('"%s" navigates to %s and closes the menu', async (label, path) => {
    const { open } = setup();
    await open();
    await userEvent.click(screen.getByText(label));
    expect(mockNavigate).toHaveBeenCalledWith(path);
    await waitFor(() => expect(screen.queryByText(label)).not.toBeInTheDocument());
  });

  it('sets data-state="open" on trigger when menu is open', async () => {
    const { trigger, open } = setup();
    expect(trigger).not.toHaveAttribute('data-state', 'open');
    await open();
    expect(trigger).toHaveAttribute('data-state', 'open');
  });
});