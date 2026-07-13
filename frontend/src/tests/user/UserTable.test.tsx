import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { UserDto } from '../../features/user/types';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string, opts?: Record<string, unknown>) =>
      opts?.direction ? `Sort by name ${opts.direction}` : key,
  }),
}));
vi.mock('../../features/user/utils/users', () => ({
  getDisplayName: (user: UserDto) => `${user.surname} ${user.name}`.trim(),
}));

import { UsersTable } from '../../features/user/components/UsersTable';

const activeUser: UserDto = {
  id: 1, name: 'Alice', surname: 'Smith', username: 'asmith',
  email: 'alice@example.com', role: 'EMPLOYEE', status: 'ACTIVE',
  departmentId: 1, managerEmail: 'manager@example.com',
};

const deletedUser: UserDto = {
  id: 2, name: 'Bob', surname: 'Jones', username: 'bjones',
  email: 'bob@example.com', role: 'EMPLOYEE', status: 'DELETED',
  departmentId: 1, managerEmail: 'manager@example.com',
};

const defaultProps = {
  data: [activeUser, deletedUser],
  nameSortDir: 'asc' as const,
  onToggleNameSort: vi.fn(),
  onView: vi.fn(),
  onEdit: vi.fn(),
  onBookings: vi.fn(),
  onDelete: vi.fn(),
  onReport: vi.fn(),
};

const getButtons = (ariaName: string) =>
  screen.getAllByRole('button', { name: new RegExp(ariaName, 'i') });

const renderTable = (props = {}) => render(<UsersTable {...defaultProps} {...props} />);

describe('UsersTable', () => {
  beforeEach(() => vi.clearAllMocks());

  describe('rendering', () => {
    it('renders user names, emails and action buttons', () => {
      renderTable();
      expect(screen.getByText('Smith Alice')).toBeInTheDocument();
      expect(screen.getByText('Jones Bob')).toBeInTheDocument();
      expect(screen.getByText('alice@example.com')).toBeInTheDocument();
      expect(screen.getByText('bob@example.com')).toBeInTheDocument();
      expect(getButtons('users.table.bookingsCta')).toHaveLength(2);
    });

    it('renders empty message when data is empty', () => {
      renderTable({ data: [], emptyMessage: <p>No users found</p> });
      expect(screen.getByText('No users found')).toBeInTheDocument();
    });
  });

  describe('sort button', () => {
    it.each([
      ['asc',  'opacity-100', 'opacity-30'],
      ['desc', 'opacity-30',  'opacity-100'],
    ])('shows correct arrow opacity when %s', (dir, upClass, downClass) => {
      renderTable({ nameSortDir: dir });
      const [up, down] = screen.getAllByText(/[▲▼]/);
      expect(up).toHaveClass(upClass);
      expect(down).toHaveClass(downClass);
    });

    it('calls onToggleNameSort when clicked', () => {
      renderTable();
      fireEvent.click(screen.getByRole('button', { name: /sort by name/i }));
      expect(defaultProps.onToggleNameSort).toHaveBeenCalledTimes(1);
    });
  });

  describe('row action callbacks', () => {
    it.each([
      ['onView',     'users.table.rowActions.viewAria',   activeUser],
      ['onEdit',     'users.table.rowActions.editAria',   activeUser],
      ['onDelete',   'users.table.rowActions.deleteAria', activeUser],
      ['onBookings', 'users.table.bookingsCta',           activeUser],
    ])('calls %s with correct user', (handler, ariaName, user) => {
      renderTable();
      fireEvent.click(getButtons(ariaName)[0]);
      expect(defaultProps[handler as keyof typeof defaultProps]).toHaveBeenCalledWith(user);
    });
  });

  describe('deleted user', () => {
    it.each([
      ['onEdit',   'users.table.rowActions.editAria'],
      ['onDelete', 'users.table.rowActions.deleteAria'],
    ])('%s button is disabled and not called for deleted user', (handler, ariaName) => {
      renderTable();
      const btn = getButtons(ariaName)[1];
      expect(btn).toBeDisabled();
      fireEvent.click(btn);
      expect(defaultProps[handler as keyof typeof defaultProps]).not.toHaveBeenCalled();

      expect(getButtons(ariaName)[0]).not.toBeDisabled();
    });

    it('applies deleted row styling', () => {
      renderTable();
      const rows = screen.getAllByRole('row').slice(1);
      expect(rows[1].className).toMatch(/bg-slate-100/);
    });
  });
});