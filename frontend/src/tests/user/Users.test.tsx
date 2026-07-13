import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { MemoryRouter, Routes, Route } from 'react-router-dom';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string, opts?: Record<string, unknown>) =>
      opts?.name ? `${key} ${opts.name}` : key,
  }),
}));

vi.mock('../../features/user/hooks/useUsers', () => ({ useUsers: vi.fn() }));

vi.mock('../../features/user/utils/users', () => ({
  getFullName: (u: { name: string; surname: string }) => `${u.name} ${u.surname}`,
  isAdmin: (user: { role?: string } | null | undefined) => user?.role === 'ADMIN',
}));

vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: any) => <div>{children}</div>,
}));

vi.mock('../../components/ui/SearchBar', () => ({
  SearchInput: ({ value, onChange }: any) => (
    <input data-testid="search-input" value={value} onChange={(e) => onChange(e.target.value)} />
  ),
}));

vi.mock('../../components/ui/FormDropdown', () => ({
  FormDropdown: ({
    value,
    onChange,
    options,
    'aria-label': ariaLabel,
    'data-testid': testId,
  }: any) => (
    <select
      data-testid={testId}
      aria-label={ariaLabel}
      value={value}
      onChange={onChange}
    >
      {options.map((option: { value: string | number; label: string }) => (
        <option key={option.value} value={option.value}>
          {option.label}
        </option>
      ))}
    </select>
  ),
}));

vi.mock('../../features/department/hooks/useDepartments', () => ({
  useDepartments: () => ({
    getDepartmentName: (id: number) => `Department ${id}`,
    departmentOptions: [
      { value: 1, label: 'Department 1' },
      { value: 2, label: 'Department 2' },
    ],
  }),
}));

vi.mock('../../components/ui/Pagination', () => ({
  Pagination: ({ page, totalPages, onPageChange }: any) => (
    <nav aria-label="ui.pagination.ariaLabel">
      <button onClick={() => onPageChange(page - 1)}>ui.pagination.previous</button>
      <span>{page}/{totalPages}</span>
      <button onClick={() => onPageChange(page + 1)}>ui.pagination.next</button>
    </nav>
  ),
}));

vi.mock('../../features/user/components/ShowDeletedFilter', () => ({
  ShowDeletedFilter: ({ checked, onToggle }: any) => (
    <input type="checkbox" aria-label="show-deleted" checked={checked} onChange={onToggle} />
  ),
}));

vi.mock('../../features/user/components/UsersTable', () => ({
  UsersTable: ({ onView, onEdit, onDelete, onBookings, onReport, emptyMessage, data }: any) => (
    <div>
      {data.map((u: any) => (
        <div key={u.id}>
          <button onClick={() => onView(u)}>view</button>
          <button onClick={() => onEdit(u)}>edit</button>
          <button onClick={() => onBookings(u)}>bookings</button>
          <button onClick={() => onReport(u)}>report</button>
          <button onClick={() => onDelete(u)}>delete</button>
        </div>
      ))}
      {data.length === 0 && <div>{emptyMessage}</div>}
    </div>
  ),
}));

vi.mock('../../features/user/components/UserModal', () => ({
  UserModal: ({ isOpen, onClose }: any) =>
    isOpen ? <div role="dialog" aria-label="view-modal"><button onClick={onClose}>close</button></div> : null,
}));

vi.mock('../../features/user/components/UserFormModal', () => ({
  UserFormModal: ({ isOpen, mode, onClose, onSave, onCreate }: any) =>
    isOpen ? (
      <div role="dialog" aria-label={`form-modal-${mode}`}>
        <button onClick={onClose}>close</button>
        <button onClick={() => onCreate?.({ username: 'test' })}>trigger-create</button>
        <button onClick={() => onSave?.({ id: 1, name: 'Alice', surname: 'Smith' })}>trigger-save</button>
      </div>
    ) : null,
}));

vi.mock('../../features/user/components/UserBookingsModal', () => ({
  UserBookingsModal: ({ isOpen, onClose }: any) =>
    isOpen ? <div role="dialog" aria-label="bookings-modal"><button onClick={onClose}>close</button></div> : null,
}));

vi.mock('../../features/user/components/UserReportModal', () => ({
  UserReportModal: ({ isOpen, onClose, user }: any) =>
    isOpen ? (
      <div role="dialog" aria-label="report-modal">
        {user && <span aria-label="report-user-name">{user.name}</span>}
        {user && <span aria-label="report-user-surname">{user.surname}</span>}
        <button onClick={onClose}>close</button>
      </div>
    ) : null,
}));

vi.mock('../../components/ui/DeleteModal', () => ({
  DeleteModal: ({ isOpen, onClose, onConfirm, title, description, item, getItemName }: any) =>
    isOpen ? (
      <div role="dialog" aria-label="delete-modal">
        <h2>{title}</h2>
        <p>{description}</p>
        {item && <span aria-label="delete-item-name">{getItemName(item)}</span>}
        <button onClick={onConfirm}>ui.deleteModal.confirmDelete</button>
        <button onClick={onClose}>ui.deleteModal.cancel</button>
      </div>
    ) : null,
}));

import Users from '../../pages/Users';
import { useUsers } from '../../features/user/hooks/useUsers';
import { authState, mockUseAuth } from '../mocks/auth';

// ── Fixtures ──────────────────────────────────────────────────────────────────

const activeUser = {
  id: 1,
  name: 'Alice',
  surname: 'Smith',
  username: 'asmith',
  email: 'alice@example.com',
  role: 'EMPLOYEE' as const,
  status: 'ACTIVE' as const,
  departmentId: 1,
  managerEmail: 'manager@example.com',
  notes: '',
};

const mockModals = { modal: null as any, activeUser: null as any, open: vi.fn(), close: vi.fn() };
const mockActions = {
  create: vi.fn().mockResolvedValue(undefined),
  update: vi.fn().mockResolvedValue(undefined),
  remove: vi.fn().mockResolvedValue(undefined),
  exportUsersCsv: vi.fn(),
};

const baseList = {
  users: [activeUser],
  filteredUsers: [activeUser],
  pagedUsers: [activeUser],
  isLoading: false,
  error: null,
  search: '',
  setSearch: vi.fn(),
  showDeleted: false,
  toggleShowDeleted: vi.fn(),
  selectedRole: '' as const,
  setSelectedRole: vi.fn(),
  selectedDepartment: '' as const,
  setSelectedDepartment: vi.fn(),
};

const buildUseUsers = (overrides: Record<string, any> = {}) => ({
  list: baseList,
  sorting: { nameSortDir: 'asc' as const, toggleNameSortDir: vi.fn() },
  pagination: { page: 1, totalPages: 1, pageSize: 10, paged: [activeUser], items: [1], setPage: vi.fn() },
  selection: { activeUser: null },
  modals: mockModals,
  actions: mockActions,
  meta: { deletingUserId: null },
  ...overrides,
});

const renderPage = (overrides: Record<string, any> = {}) => {
  vi.mocked(useUsers).mockReturnValue(buildUseUsers(overrides));
  return render(
    <MemoryRouter initialEntries={['/users']}>
      <Routes>
        <Route path="/users" element={<Users />} />
        <Route path="/bookings" element={<div>BookingsPage</div>} />
      </Routes>
    </MemoryRouter>
  );
};

const getDialog = (label: string) => screen.getByRole('dialog', { name: label });
const queryDialog = (label: string) => screen.queryByRole('dialog', { name: label });
const openDeleteModal = () => fireEvent.click(screen.getByText('delete'));

// ── Tests ─────────────────────────────────────────────────────────────────────

describe('Users page', () => {
  beforeEach(() => vi.clearAllMocks());

  describe('access control', () => {
    it('renders page for admin', () => {
      renderPage();
      expect(screen.getByText('users.title')).toBeInTheDocument();
    });

    it('redirects non-admin to /bookings', () => {
      mockUseAuth.mockReturnValue(authState({
        user: { id: 2, role: 'EMPLOYEE' } as any,
      }));
      renderPage();
      expect(screen.getByText('BookingsPage')).toBeInTheDocument();
    });

    it('does not redirect while loading', () => {
      mockUseAuth.mockReturnValue(authState({ user: null, isLoading: true }));
      renderPage();
      expect(screen.queryByText('BookingsPage')).not.toBeInTheDocument();
    });
  });

  describe('rendering', () => {
    it('renders key UI elements', () => {
      renderPage();
      expect(screen.getByText('users.title')).toBeInTheDocument();
      expect(screen.getByTestId('search-input')).toBeInTheDocument();
      expect(screen.getByRole('checkbox', { name: 'show-deleted' })).toBeInTheDocument();
      expect(screen.getByTestId('user-role-filter')).toBeInTheDocument();
      expect(screen.getByTestId('user-department-filter')).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /users.actions.new/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /users.actions.export/i })).toBeInTheDocument();
    });

    it.each([
      ['loading', { isLoading: true, pagedUsers: [] }, 'users.empty.loading'],
      ['error', { error: 'users.errors.loadUsers', pagedUsers: [] }, 'users.errors.loadUsers'],
      ['empty', { pagedUsers: [], filteredUsers: [] }, 'users.empty.none'],
    ])('shows correct message when %s', (_, listOverrides, expectedText) => {
      renderPage({ list: { ...baseList, ...listOverrides } });
      expect(screen.getByText(expectedText)).toBeInTheDocument();
    });

    it('renders pagination when filteredUsers exist', () => {
      renderPage();
      expect(screen.getByRole('navigation')).toBeInTheDocument();
    });

    it('hides pagination when no filteredUsers', () => {
      renderPage({ list: { ...baseList, filteredUsers: [] } });
      expect(screen.queryByRole('navigation')).not.toBeInTheDocument();
    });
  });

  describe('header actions', () => {
    it('calls exportUsersCsv on export click', () => {
      renderPage();
      fireEvent.click(screen.getByRole('button', { name: /users.actions.export/i }));
      expect(mockActions.exportUsersCsv).toHaveBeenCalledTimes(1);
    });

    it('opens create modal on add user click', () => {
      renderPage();
      fireEvent.click(screen.getByRole('button', { name: /users.actions.new/i }));
      expect(mockModals.open).toHaveBeenCalledWith('create');
    });
  });

  describe('row actions', () => {
    it.each(['view', 'edit', 'bookings', 'report'])(
      'clicking "%s" calls modals.open correctly',
      (action) => {
        renderPage();
        fireEvent.click(screen.getByText(action));
        expect(mockModals.open).toHaveBeenCalledWith(action, activeUser);
      },
    );

    it('clicking delete opens delete modal', () => {
      renderPage();
      openDeleteModal();
      expect(getDialog('delete-modal')).toBeInTheDocument();
    });
  });

  describe('modals render by state', () => {
    it.each([
      ['view', 'view-modal'],
      ['bookings', 'bookings-modal'],
      ['report', 'report-modal'],
      ['create', 'form-modal-create'],
      ['edit', 'form-modal-edit'],
    ])('renders correct modal for state "%s"', (modalType, ariaLabel) => {
      renderPage({ modals: { ...mockModals, modal: modalType } });
      expect(getDialog(ariaLabel)).toBeInTheDocument();
    });
  });

  describe('UserFormModal callbacks', () => {
    it('calls actions.update when onSave is triggered', async () => {
      renderPage({ modals: { ...mockModals, modal: 'edit' } });
      fireEvent.click(screen.getByText('trigger-save'));
      await waitFor(() => expect(mockActions.update).toHaveBeenCalledTimes(1));
    });

    it('calls actions.create when onCreate is triggered', async () => {
      renderPage({ modals: { ...mockModals, modal: 'create' } });
      fireEvent.click(screen.getByText('trigger-create'));
      await waitFor(() => expect(mockActions.create).toHaveBeenCalledTimes(1));
    });
  });

  describe('UserReportModal user prop', () => {
    it('passes correct shape when activeUser exists', () => {
      renderPage({ modals: { ...mockModals, modal: 'report' }, selection: { activeUser } });
      expect(screen.getByRole('generic', { name: 'report-user-name' })).toHaveTextContent('Alice');
      expect(screen.getByRole('generic', { name: 'report-user-surname' })).toHaveTextContent('Smith');
    });

    it('passes null when no activeUser', () => {
      renderPage({ modals: { ...mockModals, modal: 'report' }, selection: { activeUser: null } });
      expect(getDialog('report-modal')).toBeInTheDocument();
      expect(screen.queryByRole('generic', { name: 'report-user-name' })).not.toBeInTheDocument();
    });
  });

  describe('delete modal', () => {
    it('shows correct title, description and item name', () => {
      renderPage();
      openDeleteModal();
      expect(screen.getByText('users.delete.title')).toBeInTheDocument();
      expect(screen.getByText('users.delete.description Alice Smith')).toBeInTheDocument();
      expect(screen.getByRole('generic', { name: 'delete-item-name' })).toHaveTextContent('Alice Smith');
    });

    it('calls remove with correct id and closes modal on confirm', async () => {
      renderPage();
      openDeleteModal();
      fireEvent.click(screen.getByText('ui.deleteModal.confirmDelete'));
      await waitFor(() => {
        expect(mockActions.remove).toHaveBeenCalledWith(activeUser.id);
        expect(queryDialog('delete-modal')).not.toBeInTheDocument();
      });
    });

    it('closes without calling remove on cancel', () => {
      renderPage();
      openDeleteModal();
      fireEvent.click(screen.getByText('ui.deleteModal.cancel'));
      expect(queryDialog('delete-modal')).not.toBeInTheDocument();
      expect(mockActions.remove).not.toHaveBeenCalled();
    });

    it('does not show modal by default', () => {
      renderPage();
      expect(queryDialog('delete-modal')).not.toBeInTheDocument();
    });
  });
});