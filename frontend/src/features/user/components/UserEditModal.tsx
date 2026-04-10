import { useEffect, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { userRoleSchema, userStatusSchema, userValidationSchema } from '../validation';

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

export type UserEditModalUser = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  role: 'EMPLOYEE' | 'ADMIN' | 'MANAGER';
  status: 'ACTIVE' | 'INACTIVE';
  departmentId: number;
  managerEmail: string;
  notes?: string;
};

type UserEditModalProps = {
  isOpen: boolean;
  onClose: () => void;
  user: UserEditModalUser | null;
  onSave: (user: UserEditModalUser) => void;
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

  useEffect(() => {
    if (isOpen) setErrors(initialErrors);
  }, [isOpen, user]);

  if (!isOpen || !user) return null;

  const handleSubmit = (data: FormData) => {
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

    onSave({
      ...user,
      firstName: result.data.name,
      lastName: result.data.surname,
      email: result.data.email,
      username: result.data.username,
      role: result.data.role,
      status: result.data.status,
      departmentId: result.data.departmentId,
      managerEmail: result.data.managerEmail,
      notes: result.data.notes?.trim() || undefined,
    });
    onClose();
  };

  const roleOptions = userRoleSchema.options.map((role) => ({
    value: role,
    label: role,
  }));

  const statusLabels: Record<UserEditModalUser['status'], string> = {
    ACTIVE: 'Active',
    INACTIVE: 'Inactive',
  };

  const statusOptions = userStatusSchema.options
    .filter((s) => s === 'ACTIVE' || s === 'INACTIVE')
    .map((status) => ({
      value: status,
      label: statusLabels[status],
    }));

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label="Edit user"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-[800px] overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-end px-8 pt-6 pb-4">
          <button
            type="button"
            onClick={onClose}
            aria-label="Close"
            className="inline-flex cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
          >
            <CloseIcon className="pointer-events-none" />
          </button>
        </div>
        <div className="mx-8 h-px bg-(--color-table-border)" />

        <Form.Root
          key={user.id}
          onSubmit={(event) => {
            event.preventDefault();
            const formData = new FormData(event.currentTarget);
            handleSubmit(formData);
          }}
        >
          <div className="px-8 py-8">
            <div className="flex flex-col gap-5">
              <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
                <Form.Field name="role">
                  <Form.Control asChild>
                    <FormDropdown
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
                    <FormDropdown
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
                  <FormInput
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
                  <FormInput
                    id="user-first-name"
                    name="name"
                    type="text"
                    label="First name"
                    defaultValue={user.firstName}
                    error={!!errors.name}
                    errorMessage={errors.name}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="surname">
                <Form.Control asChild>
                  <FormInput
                    id="user-last-name"
                    name="surname"
                    type="text"
                    label="Last name"
                    defaultValue={user.lastName}
                    error={!!errors.surname}
                    errorMessage={errors.surname}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="email">
                <Form.Control asChild>
                  <FormInput
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
                    <FormInput
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
                    <FormInput
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
                  <FormInput
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
          </div>

          <div className="mx-8 h-px bg-(--color-table-border)" />
          <div className="flex justify-end px-8 py-5">
            <Form.Submit asChild>
              <Button type="submit">Save</Button>
            </Form.Submit>
          </div>
        </Form.Root>
      </div>
    </div>
  );
};

