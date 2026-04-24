import { useEffect, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import { userRoleSchema, userStatusSchema, userValidationSchema } from '../validation';
import type { UserUpsertRequest } from '../types';

const userCreateSchema = userValidationSchema
  .pick({
    username: true,
    name: true,
    surname: true,
    email: true,
    password: true,
    role: true,
    status: true,
    departmentId: true,
    managerEmail: true,
    notes: true,
  })
  .extend({
    status: userStatusSchema.extract(['ACTIVE', 'INACTIVE']),
  });

export type UserCreateModalUser = Pick<
  UserUpsertRequest,
  | 'username'
  | 'name'
  | 'surname'
  | 'email'
  | 'password'
  | 'role'
  | 'status'
  | 'departmentId'
  | 'managerEmail'
  | 'notes'
>;

type UserCreateModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onCreate: (user: UserCreateModalUser) => Promise<void>;
};

type FormErrors = {
  username: string;
  name: string;
  surname: string;
  email: string;
  password: string;
  role: string;
  status: string;
  departmentId: string;
  managerEmail: string;
  notes: string;
};

const initialErrors: FormErrors = {
  username: '',
  name: '',
  surname: '',
  email: '',
  password: '',
  role: '',
  status: '',
  departmentId: '',
  managerEmail: '',
  notes: '',
};

const initialValues: UserCreateModalUser = {
  username: '',
  name: '',
  surname: '',
  email: '',
  password: '',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
  departmentId: 1,
  managerEmail: '',
  notes: '',
};

export const UserCreateModal = ({ isOpen, onClose, onCreate }: UserCreateModalProps) => {
  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      setSubmitError(null);
      setIsSaving(false);
    }
  }, [isOpen]);

  if (!isOpen) return null;
  const formId = 'user-create-form';

  const roleOptions = userRoleSchema.options.map((role) => ({
    value: role,
    label: role,
  }));

  const statusLabels: Record<UserCreateModalUser['status'], string> = {
    ACTIVE: 'Active',
    INACTIVE: 'Inactive',
  };

  const statusOptions = userStatusSchema.options
    .filter((s) => s === 'ACTIVE' || s === 'INACTIVE')
    .map((status) => ({
      value: status,
      label: statusLabels[status],
    }));

  const handleSubmit = async (data: FormData) => {
    const formValues = {
      username: data.get('username') as string,
      name: data.get('name') as string,
      surname: data.get('surname') as string,
      email: data.get('email') as string,
      password: data.get('password') as string,
      role: data.get('role') as string,
      status: data.get('status') as string,
      departmentId: data.get('departmentId') as string,
      managerEmail: data.get('managerEmail') as string,
      notes: data.get('notes') as string,
    };

    const result = userCreateSchema.safeParse(formValues);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;
      setErrors({
        username: fieldErrors.username?.[0] || '',
        name: fieldErrors.name?.[0] || '',
        surname: fieldErrors.surname?.[0] || '',
        email: fieldErrors.email?.[0] || '',
        password: fieldErrors.password?.[0] || '',
        role: fieldErrors.role?.[0] || '',
        status: fieldErrors.status?.[0] || '',
        departmentId: fieldErrors.departmentId?.[0] || '',
        managerEmail: fieldErrors.managerEmail?.[0] || '',
        notes: fieldErrors.notes?.[0] || '',
      });
      return;
    }

    setSubmitError(null);
    setIsSaving(true);
    try {
      await onCreate({
        username: result.data.username,
        name: result.data.name,
        surname: result.data.surname,
        email: result.data.email,
        password: result.data.password,
        role: result.data.role,
        status: result.data.status,
        departmentId: result.data.departmentId,
        managerEmail: result.data.managerEmail,
        notes: result.data.notes?.trim() || null,
      });
      onClose();
    } catch {
      setSubmitError('Failed to create user. Please try again.');
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel="Create user"
      headerRight={
        <IconButton onClick={onClose} aria-label="Close">
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={
        <div className="flex justify-end">
          <Button
            data-testid="create-user-button"
            type="submit"
            form={formId}
            className="shadow-none"
            disabled={isSaving}
          >
            {isSaving ? 'Saving…' : 'Save'}
          </Button>
        </div>
      }
    >
      <Form.Root
        noValidate
        id={formId}
        onSubmit={(event) => {
          event.preventDefault();
          const formData = new FormData(event.currentTarget);
          void handleSubmit(formData);
        }}
      >
        <div className="flex flex-col gap-5">
          {submitError && (
            <div className="rounded border border-red-300 bg-red-50 px-3 py-2 text-sm text-red-800">
              {submitError}
            </div>
          )}
          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <Form.Field name="role">
              <Form.Control asChild>
                <FormDropdown data-testid="user-role"
                  id="user-role"
                  name="role"
                  label="Role"
                  defaultValue={initialValues.role}
                  error={!!errors.role}
                  errorMessage={errors.role}
                  options={roleOptions}
                />
              </Form.Control>
            </Form.Field>

            <Form.Field name="status">
              <Form.Control asChild>
                <FormDropdown data-testid="user-status"
                  id="user-status"
                  name="status"
                  label="Status"
                  defaultValue={initialValues.status}
                  error={!!errors.status}
                  errorMessage={errors.status}
                  options={statusOptions}
                />
              </Form.Control>
            </Form.Field>
          </div>

          <Form.Field name="username">
            <Form.Control asChild>
              <FormInput data-testid="user-username"
                id="user-username"
                name="username"
                type="text"
                label="Username"
                defaultValue={initialValues.username}
                error={!!errors.username}
                errorMessage={errors.username}
              />
            </Form.Control>
          </Form.Field>

          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <Form.Field name="name">
              <Form.Control asChild>
                <FormInput data-testid="user-name"
                  id="user-first-name"
                  name="name"
                  type="text"
                  label="First name"
                  defaultValue={initialValues.name}
                  error={!!errors.name}
                  errorMessage={errors.name}
                />
              </Form.Control>
            </Form.Field>

            <Form.Field name="surname">
              <Form.Control asChild>
                <FormInput data-testid="user-surname"
                  id="user-last-name"
                  name="surname"
                  type="text"
                  label="Last name"
                  defaultValue={initialValues.surname}
                  error={!!errors.surname}
                  errorMessage={errors.surname}
                />
              </Form.Control>
            </Form.Field>
          </div>

          <Form.Field name="email">
            <Form.Control asChild>
              <FormInput data-testid="user-email"
                id="user-email"
                name="email"
                type="email"
                label="Email"
                defaultValue={initialValues.email}
                error={!!errors.email}
                errorMessage={errors.email}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="password">
            <Form.Control asChild>
              <FormInput
                data-testid="user-password"
                id="user-password"
                name="password"
                type="password"
                label="Password"
                defaultValue={initialValues.password}
                error={!!errors.password}
                errorMessage={errors.password}
              />
            </Form.Control>
          </Form.Field>

          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <Form.Field name="departmentId">
              <Form.Control asChild>
                <FormInput data-testid="user-department-id"
                  id="user-department"
                  name="departmentId"
                  type="number"
                  label="Department ID"
                  defaultValue={String(initialValues.departmentId)}
                  error={!!errors.departmentId}
                  errorMessage={errors.departmentId}
                />
              </Form.Control>
            </Form.Field>

            <Form.Field name="managerEmail">
              <Form.Control asChild>
                <FormInput data-testid="user-manager-email"
                  id="user-manager-email"
                  name="managerEmail"
                  type="email"
                  label="Manager email"
                  defaultValue={initialValues.managerEmail}
                  error={!!errors.managerEmail}
                  errorMessage={errors.managerEmail}
                />
              </Form.Control>
            </Form.Field>
          </div>

          <Form.Field name="notes">
            <Form.Control asChild>
              <FormInput data-testid="user-note"
                id="user-notes"
                name="notes"
                type="text"
                label="Notes"
                defaultValue={initialValues.notes ?? ''}
                error={!!errors.notes}
                errorMessage={errors.notes}
              />
            </Form.Control>
          </Form.Field>
        </div>
      </Form.Root>
    </Modal>
  );
};

