import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { UserFormModal } from '../../features/user/components/UserFormModal';
import type { UserDto } from '../../features/user/types';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

vi.mock('../../features/department/hooks/useDepartments', () => ({
  useDepartments: () => ({
    getDepartmentName: (id: number) => `Department ${id}`,
    departmentOptions: [{ value: 1, label: 'Department 1' }],
  }),
}));

vi.mock('@radix-ui/react-form', async () => vi.importActual('@radix-ui/react-form'));

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
  notes: '',
};

const defaultProps = {
  isOpen: true,
  mode: 'create' as const,
  user: null,
  onClose: vi.fn(),
  onCreate: vi.fn().mockResolvedValue(undefined),
  onSave: vi.fn().mockResolvedValue(undefined),
};

// --- Helpers ---

const renderModal = (props = {}) =>
  render(<UserFormModal {...defaultProps} {...props} />);

const validCreateValues = {
  username: 'newuser',
  name: 'Alice',
  surname: 'Smith',
  email: 'alice@example.com',
  password: 'password123',
  departmentId: '1',
  managerEmail: 'manager@example.com',
};

const fillCreateForm = (overrides: Partial<typeof validCreateValues> = {}) => {
  const v = { ...validCreateValues, ...overrides };
  const fields: [string, string][] = [
    ['user-username', v.username],
    ['user-name', v.name],
    ['user-surname', v.surname],
    ['user-email', v.email],
    ['user-password', v.password],
    ['user-department-id', v.departmentId],
    ['user-manager-email', v.managerEmail],
  ];
  fields.forEach(([testId, value]) =>
    fireEvent.change(screen.getByTestId(testId), { target: { value } }),
  );
};

const submitCreate = () => fireEvent.click(screen.getByTestId('create-user-button'));
const submitEdit = () => fireEvent.click(screen.getByTestId('button-save'));


describe('UserFormModal', () => {
  beforeEach(() => vi.clearAllMocks());

  describe('visibility', () => {
    it.each([
      ['isOpen is false', { isOpen: false }],
      ['edit mode with no user', { mode: 'edit' as const, user: null }],
    ])('renders nothing when %s', (_, props) => {
      renderModal(props);
      expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });

    it.each([
      ['create mode', {}],
      ['edit mode with user', { mode: 'edit' as const, user: activeUser }],
    ])('renders modal in %s', (_, props) => {
      renderModal(props);
      expect(screen.getByRole('dialog')).toBeInTheDocument();
    });
  });

  describe('create mode', () => {
    it('renders all fields', () => {
      renderModal();
      const testIds = [
        'user-username', 'user-password', 'user-name', 'user-surname',
        'user-email', 'user-role', 'user-status', 'user-department-id',
        'user-manager-email', 'user-note',
      ];
      testIds.forEach((id) => expect(screen.getByTestId(id)).toBeInTheDocument());
    });

    it('calls onClose when close button is clicked', () => {
      renderModal();
      fireEvent.click(screen.getByTestId('close-button'));
      expect(defaultProps.onClose).toHaveBeenCalledTimes(1);
    });

    it('calls onCreate and onClose with valid data', async () => {
      renderModal();
      fillCreateForm();
      submitCreate();
      await waitFor(() => {
        expect(defaultProps.onCreate).toHaveBeenCalledTimes(1);
        expect(defaultProps.onClose).toHaveBeenCalledTimes(1);
      });
    });

    it('shows submit error when onCreate throws', async () => {
      defaultProps.onCreate.mockRejectedValueOnce(new Error());
      renderModal();
      fillCreateForm();
      submitCreate();
      await waitFor(() =>
        expect(screen.getByText('users.modals.create.submitError')).toBeInTheDocument(),
      );
    });

    it('disables save button while saving', async () => {
      let resolve!: () => void;
      defaultProps.onCreate.mockImplementationOnce(
        () => new Promise<void>((res) => { resolve = res; }),
      );
      renderModal();
      fillCreateForm();
      submitCreate();
      await waitFor(() => expect(screen.getByTestId('create-user-button')).toBeDisabled());
      resolve();
    });
  });

  describe('create mode — field validation', () => {
    it.each([
      ['username too short', { username: 'ab' }],
      ['name too short', { name: 'ab' }],
      ['surname too short', { surname: 'ab' }],
      ['email invalid', { email: 'not-an-email' }],
      ['password too short', { password: '123' }],
      ['managerEmail invalid', { managerEmail: 'not-an-email' }],
      ['departmentId not positive', { departmentId: '-1' }],
    ])('does not call onCreate when %s', async (_, overrides) => {
      renderModal();
      fillCreateForm(overrides);
      submitCreate();
      await waitFor(() => expect(defaultProps.onCreate).not.toHaveBeenCalled());
    });
  });

  describe('edit mode', () => {
    it('does not render username and password fields', () => {
      renderModal({ mode: 'edit', user: activeUser });
      expect(screen.queryByTestId('user-username')).not.toBeInTheDocument();
      expect(screen.queryByTestId('user-password')).not.toBeInTheDocument();
    });

    it('pre-fills fields with user data', () => {
      renderModal({ mode: 'edit', user: activeUser });
      expect(screen.getByTestId('user-name')).toHaveValue('Alice');
      expect(screen.getByTestId('user-surname')).toHaveValue('Smith');
      expect(screen.getByTestId('user-email')).toHaveValue('alice@example.com');
      expect(screen.getByTestId('user-manager-email')).toHaveValue('manager@example.com');
    });

    it('disables save button until a field is changed', () => {
      renderModal({ mode: 'edit', user: activeUser });
      expect(screen.getByTestId('button-save')).toBeDisabled();
      fireEvent.change(screen.getByTestId('user-name'), {
        target: { value: 'Alicia' },
      });
      expect(screen.getByTestId('button-save')).not.toBeDisabled();
    });

    it('calls onSave and onClose with valid data', async () => {
      renderModal({ mode: 'edit', user: activeUser });
      fireEvent.change(screen.getByTestId('user-name'), {
        target: { value: 'Alicia' },
      });
      submitEdit();
      await waitFor(() => {
        expect(defaultProps.onSave).toHaveBeenCalledTimes(1);
        expect(defaultProps.onClose).toHaveBeenCalledTimes(1);
      });
    });

    it('shows submit error when onSave throws', async () => {
      defaultProps.onSave.mockRejectedValueOnce(new Error());
      renderModal({ mode: 'edit', user: activeUser });
      fireEvent.change(screen.getByTestId('user-name'), {
        target: { value: 'Alicia' },
      });
      submitEdit();
      await waitFor(() =>
        expect(screen.getByText('users.modals.edit.submitError')).toBeInTheDocument(),
      );
    });

    it('calls onClose when close button is clicked', () => {
      renderModal({ mode: 'edit', user: activeUser });
      fireEvent.click(screen.getByRole('button', { name: /users.modals.common.closeAria/i }));
      expect(defaultProps.onClose).toHaveBeenCalledTimes(1);
    });
  });
});