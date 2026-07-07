import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi, describe, it, expect, afterEach } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import AccountInfo from '../../pages/AccountInfo';
import { authState, mockUseAuth } from '../mocks/auth';
import { useDepartments } from '../../features/department/hooks/useDepartments';
import { getUserById } from '../../features/user/api/users';

// Mocks 

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: React.HTMLAttributes<HTMLDivElement>) => <div>{children}</div>,
}));
vi.mock('../../components/ui/BadgeRow', () => ({
  BadgeRow: ({ label, value, testId }: any) => (
    <div><span>{label}</span><span data-testid={testId}>{value}</span></div>
  ),
}));
vi.mock('../../components/ui/InfoRow', () => ({
  InfoRow: ({ label, value, valueSlot }: any) => (
    <div><span>{label}</span>{valueSlot ?? <span>{value}</span>}</div>
  ),
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick, ...props }: React.ButtonHTMLAttributes<HTMLButtonElement>) => (
    <button onClick={onClick} {...props}>{children}</button>
  ),
}));
vi.mock('../../features/department/hooks/useDepartments', () => ({
  useDepartments: vi.fn(),
}));
vi.mock('../../features/user/api/users', () => ({
  getUserById: vi.fn(),
}));
vi.mock('../../features/user/components/ChangePasswordModal', () => ({
  ChangePasswordModal: ({ isOpen, onClose }: any) =>
    isOpen ? <div><span>ChangePasswordModal</span><button onClick={onClose}>CloseModal</button></div> : null,
}));

// Helpers 

const mockUser = {
  id: 1,
  name: 'Alice',
  surname: 'Smith',
  username: 'asmith',
  email: 'alice@example.com',
  role: 'ADMIN' as const,
  status: 'ACTIVE' as const,
  departmentId: 1,
  managerEmail: 'manager@example.com',
  notes: 'Some notes',
};

const setUser = (overrides = {}) => {
  const user = { ...mockUser, ...overrides };
  mockUseAuth.mockReturnValue(authState({ user: user as any }));
  vi.mocked(getUserById).mockResolvedValue(user as any);
};

const renderPage = () => render(<MemoryRouter><AccountInfo /></MemoryRouter>);

// --- Tests ---

describe('AccountInfo', () => {
  beforeEach(() => {
    mockUseAuth.mockReturnValue(authState({ user: null }));
    vi.mocked(useDepartments).mockReturnValue({ getDepartmentName: vi.fn(() => 'Engineering') } as any);
  });

  afterEach(() => vi.clearAllMocks());

  it('renders page heading', () => {
    renderPage();
    expect(screen.getByText('account.heading')).toBeInTheDocument();
  });

  it('shows loading state', () => {
    mockUseAuth.mockReturnValue(authState({ user: null, isLoading: true }));
    renderPage();
    expect(screen.getByText('account.loading')).toBeInTheDocument();
  });

  it('shows error state', () => {
    mockUseAuth.mockReturnValue(authState({ user: null, error: 'fetch error' }));
    renderPage();
    expect(screen.getByText('fetch error')).toBeInTheDocument();
  });

  it('shows empty state when no user', () => {
    renderPage();
    expect(screen.getByText('account.empty')).toBeInTheDocument();
  });

  describe('with user', () => {
    it('renders name, email, department, manager and notes', async () => {
      setUser();
      renderPage();
      await waitFor(() => {
        expect(screen.getByText('Alice Smith')).toBeInTheDocument();
      });
      expect(screen.getAllByText('alice@example.com').length).toBeGreaterThan(0);
      expect(screen.getByText('Engineering')).toBeInTheDocument();
      expect(screen.getByText('manager@example.com')).toBeInTheDocument();
      expect(screen.getByText('Some notes')).toBeInTheDocument();
    });

    it('renders role and status badges', async () => {
      setUser();
      renderPage();
      await waitFor(() => {
        expect(screen.getByTestId('account-role')).toHaveTextContent('ADMIN');
      });
      expect(screen.getByTestId('account-status')).toHaveTextContent('ACTIVE');
    });

    it.each(['ADMIN', 'MANAGER', 'EMPLOYEE'])('renders %s role badge', async (role) => {
      setUser({ role });
      renderPage();
      await waitFor(() => {
        expect(screen.getByTestId('account-role')).toHaveTextContent(role);
      });
    });

    it('renders unknown status badge', async () => {
      setUser({ status: 'DELETED' });
      renderPage();
      await waitFor(() => {
        expect(screen.getByTestId('account-status')).toHaveTextContent('DELETED');
      });
    });

    it('shows emptyValue when notes is null', async () => {
      setUser({ notes: null });
      renderPage();
      await waitFor(() => {
        expect(screen.getAllByText('account.common.emptyValue').length).toBeGreaterThan(0);
      });
    });

    it('shows emptyValue when department not found', async () => {
      vi.mocked(useDepartments).mockReturnValue({ getDepartmentName: vi.fn(() => undefined) } as any);
      setUser();
      renderPage();
      await waitFor(() => {
        expect(screen.getAllByText('account.common.emptyValue').length).toBeGreaterThan(0);
      });
    });

    it('opens and closes change password modal', async () => {
      setUser();
      renderPage();
      const user = userEvent.setup();

      await waitFor(() => {
        expect(screen.getByTestId('account-open-change-password')).toBeInTheDocument();
      });
      await user.click(screen.getByTestId('account-open-change-password'));
      expect(screen.getByText('ChangePasswordModal')).toBeInTheDocument();

      await user.click(screen.getByText('CloseModal'));
      expect(screen.queryByText('ChangePasswordModal')).not.toBeInTheDocument();
    });
  });
});
