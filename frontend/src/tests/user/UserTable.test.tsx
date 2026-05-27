// src/tests/user/UserTable.test.tsx
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { UsersTable } from '../../features/user/components/UsersTable';
import type { UserDto } from '../../features/user/types';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string, opts?: Record<string, unknown>) => {
      if (opts?.direction) return `Sort by name ${opts.direction}`;
      return key;
    },
  }),
}));

vi.mock('../../features/user/utilis/users', () => ({
  getDisplayName: (user: UserDto) => `${user.surname} ${user.name}`.trim(),
}));

const activeUser: UserDto = {
  id: 1,
  name: 'Alice',
  surname: 'Smith',
  username: 'asmith',
  email: 'alice@example.com',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
  departmentId: 1,
  managerEmail: 'manager@example.com',
};

const deletedUser: UserDto = {
  id: 2,
  name: 'Bob',
  surname: 'Jones',
  username: 'bjones',
  email: 'bob@example.com',
  role: 'EMPLOYEE',
  status: 'DELETED',
  departmentId: 1,
  managerEmail: 'manager@example.com',
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

// --- Helpers ---

const getButtons = (ariaName: string) =>
  screen.getAllByRole('button', { name: new RegExp(ariaName, 'i') });

const renderTable = (props = {}) =>
  render(<UsersTable {...defaultProps} {...props} />);

describe('UsersTable', () => {
  beforeEach(() => vi.clearAllMocks());

  describe('rendering', () => {
    it('renders user full names', () => {
      renderTable();
      expect(screen.getByText('Smith Alice')).toBeInTheDocument();
      expect(screen.getByText('Jones Bob')).toBeInTheDocument();
    });

    it('renders user emails', () => {
      renderTable();
      expect(screen.getByText('alice@example.com')).toBeInTheDocument();
      expect(screen.getByText('bob@example.com')).toBeInTheDocument();
    });

    it('renders a bookings button for each user', () => {
      renderTable();
      expect(getButtons('users.table.bookingsCta')).toHaveLength(2);
    });

    it('renders empty message when data is empty', () => {
      renderTable({ data: [], emptyMessage: <p>No users found</p> });
      expect(screen.getByText('No users found')).toBeInTheDocument();
    });
  });

  describe('sort button', () => {
    it('shows ▲ active and ▼ dim when asc', () => {
      renderTable({ nameSortDir: 'asc' });
      const [up, down] = screen.getAllByText(/[▲▼]/);
      expect(up).toHaveClass('opacity-100');
      expect(down).toHaveClass('opacity-30');
    });

    it('shows ▼ active and ▲ dim when desc', () => {
      renderTable({ nameSortDir: 'desc' });
      const [up, down] = screen.getAllByText(/[▲▼]/);
      expect(up).toHaveClass('opacity-30');
      expect(down).toHaveClass('opacity-100');
    });

    it('calls onToggleNameSort when clicked', () => {
      renderTable();
      fireEvent.click(screen.getByRole('button', { name: /sort by name/i }));
      expect(defaultProps.onToggleNameSort).toHaveBeenCalledTimes(1);
    });
  });

  describe('row action callbacks', () => {
    it.each([
      ['onView', 'users.table.rowActions.viewAria', activeUser],
      ['onReport', 'users.table.rowActions.reportAria', activeUser],
      ['onEdit', 'users.table.rowActions.editAria', activeUser],
      ['onDelete', 'users.table.rowActions.deleteAria', activeUser],
      ['onBookings', 'users.table.bookingsCta', activeUser],
    ])('calls %s with the correct user', (handler, ariaName, user) => {
      renderTable();
      fireEvent.click(getButtons(ariaName)[0]);
      expect(defaultProps[handler as keyof typeof defaultProps]).toHaveBeenCalledWith(user);
    });
  });

  describe('deleted user', () => {
    it.each([
      ['edit', 'users.table.rowActions.editAria'],
      ['delete', 'users.table.rowActions.deleteAria'],
    ])('disables %s button for deleted user', (_, ariaName) => {
      renderTable();
      expect(getButtons(ariaName)[1]).toBeDisabled();
    });

    it.each([
      ['edit', 'users.table.rowActions.editAria'],
      ['delete', 'users.table.rowActions.deleteAria'],
    ])('does not disable %s button for active user', (_, ariaName) => {
      renderTable();
      expect(getButtons(ariaName)[0]).not.toBeDisabled();
    });

    it.each([
      ['onEdit', 'users.table.rowActions.editAria'],
      ['onDelete', 'users.table.rowActions.deleteAria'],
    ])('does not call %s when clicked for deleted user', (handler, ariaName) => {
      renderTable();
      fireEvent.click(getButtons(ariaName)[1]);
      expect(defaultProps[handler as keyof typeof defaultProps]).not.toHaveBeenCalled();
    });

    it('applies deleted row styling', () => {
      renderTable();
      const rows = screen.getAllByRole('row').slice(1);
      expect(rows[1].className).toMatch(/bg-slate-100/);
    });
  });
});