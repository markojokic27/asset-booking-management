import { useEffect, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import { userRoleSchema, userStatusSchema, userValidationSchema } from '../validation';
import type { UserDto } from '../types';

const userEditSchema = userValidationSchema
  .pick({
    name: true,
    surname: true,
    email: true,
    username: true,
    role: true,
    status: true,
    departmentId: true,
    managerEmail: true,
    notes: true,
  })
  .extend({
    status: userStatusSchema.extract(['ACTIVE', 'INACTIVE']),
  });

type UserEditModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserDto | null;
  onSave: (user: UserDto) => Promise<void>;
};

type FormErrors = {
  name: string;
  surname: string;
  email: string;
  username: string;
  role: string;
  status: string;
  departmentId: string;
  managerEmail: string;
  notes: string;
};

const initialErrors: FormErrors = {
  name: '',
  surname: '',
  email: '',
  username: '',
  role: '',
  status: '',
  departmentId: '',
  managerEmail: '',
  notes: '',
};

export const UserEditModal = ({ isOpen, onClose, user, onSave }: UserEditModalProps) => {
  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      setSubmitError(null);
      setIsSaving(false);
    }
  }, [isOpen, user]);

  if (!isOpen || !user) return null;
  const formId = `user-edit-form-${user.id}`;

  const handleSubmit = async (data: FormData) => {
    const formValues = {
      name: data.get('name') as string,
      surname: data.get('surname') as string,
      email: data.get('email') as string,
      username: data.get('username') as string,
      role: data.get('role') as string,
      status: data.get('status') as string,
      departmentId: data.get('departmentId') as string,
      managerEmail: data.get('managerEmail') as string,
      notes: data.get('notes') as string,
    };

    const result = userEditSchema.safeParse(formValues);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;
      setErrors({
        name: fieldErrors.name?.[0] || '',
        surname: fieldErrors.surname?.[0] || '',
        email: fieldErrors.email?.[0] || '',
        username: fieldErrors.username?.[0] || '',
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
      await onSave({
        ...user,
        name: result.data.name,
        surname: result.data.surname,
        email: result.data.email,
        username: result.data.username,
        role: result.data.role,
        status: result.data.status,
        departmentId: result.data.departmentId,
        managerEmail: result.data.managerEmail,
        notes: result.data.notes?.trim() || null,
      });
      onClose();
    } catch {
      setSubmitError('Failed to save changes. Please try again.');
    } finally {
      setIsSaving(false);
    }
  };

  const roleOptions = userRoleSchema.options.map((role) => ({
    value: role,
    label: role,
  }));

  const statusLabels: Record<UserDto['status'], string> = {
    ACTIVE: 'Active',
    INACTIVE: 'Inactive',
    STUDENT: 'Student',
    LEFT_COMPANY: 'Left Company',
    DELETED: 'Deleted',
  };

  const statusOptions = userStatusSchema.options
    .filter((s) => s === 'ACTIVE' || s === 'INACTIVE')
    .map((status) => ({
      value: status,
      label: statusLabels[status],
    }));

  return (
    <Modal data-testid="user-edit"
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel="Edit user"
      headerRight={
        <IconButton onClick={onClose} aria-label="Close">
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={
        <div className="flex justify-end">
          <Button
            data-testid="button-save"
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
        key={user.id}
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
                  defaultValue={user.role}
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
                  defaultValue={user.status}
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
                defaultValue={user.username}
                error={!!errors.username}
                errorMessage={errors.username}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="name">
            <Form.Control asChild>
              <FormInput data-testid="user-name"
                id="user-first-name"
                name="name"
                type="text"
                label="First name"
                defaultValue={user.name}
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
                defaultValue={user.surname}
                error={!!errors.surname}
                errorMessage={errors.surname}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="email">
            <Form.Control asChild>
              <FormInput data-testid="user-email"
                id="user-email"
                name="email"
                type="email"
                label="Email"
                defaultValue={user.email}
                error={!!errors.email}
                errorMessage={errors.email}
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
                  defaultValue={String(user.departmentId)}
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
                  defaultValue={user.managerEmail}
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
                defaultValue={user.notes ?? ''}
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

