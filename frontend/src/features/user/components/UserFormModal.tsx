// external imports
import { useEffect, useMemo, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import { useTranslation } from 'react-i18next';

// icons
import CloseIcon from '@mui/icons-material/Close';

// components
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import { Toast } from '../../../components/ui/Toast';

// hooks
import { useEditFormChanges } from '../../../hooks/useEditFormChanges';
import { useDepartments } from '../../department/hooks/useDepartments';
import { getFullName } from '../utils/users';

// validation
import {
  createUserValidationSchema,
  userRoleSchema,
  userStatusSchema,
} from '../validation';

// types
import type { UserDto, UserUpsertRequest } from '../types';

export type UserFormModalCreatePayload = Pick<
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

type UserFormModalMode = 'create' | 'edit';

type UserFormModalProps = {
  isOpen: boolean;
  mode: UserFormModalMode;
  user: UserDto | null;
  onClose: () => void;
  onCreate: (user: UserFormModalCreatePayload) => Promise<void>;
  onSave: (user: UserDto) => Promise<void>;
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

const createInitialValues: UserFormModalCreatePayload = {
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

export const UserFormModal = ({
  isOpen,
  mode,
  user,
  onClose,
  onCreate,
  onSave,
}: UserFormModalProps) => {
  const { t } = useTranslation();
  const { getDepartmentName, departmentOptions } = useDepartments();
  const isCreate = mode === 'create';
  const fieldsKey = isCreate
    ? 'users.modals.create.fields'
    : 'users.modals.edit.fields';

  const validationSchema = useMemo(() => {
    const base = createUserValidationSchema(t).extend({
      status: userStatusSchema.extract(['ACTIVE', 'INACTIVE']),
    });

    if (isCreate) {
      return base.pick({
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
      });
    }

    return base.pick({
      name: true,
      surname: true,
      email: true,
      role: true,
      status: true,
      departmentId: true,
      managerEmail: true,
      notes: true,
    });
  }, [isCreate, t]);

  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const { onFormChange, isSaveDisabled } = useEditFormChanges(
    !isCreate,
    `${isOpen}-${user?.id ?? ''}`
  );

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      setSubmitError(null);
      setIsSaving(false);
    }
  }, [isOpen, mode, user]);

  if (!isOpen || (!isCreate && !user)) return null;

  const formValues = isCreate ? createInitialValues : user!;
  const formId = isCreate ? 'user-create-form' : `user-edit-form-${user!.id}`;
  const formKey = isCreate ? 'create' : String(user!.id);

  const roleOptions = userRoleSchema.options.map((role) => ({
    value: role,
    label: role,
  }));

  const statusLabels: Record<UserDto['status'], string> = {
    ACTIVE: t('users.status.active'),
    INACTIVE: t('users.status.inactive'),
    STUDENT: t('users.status.student'),
    LEFT_COMPANY: t('users.status.left_company'),
    DELETED: t('users.status.deleted'),
  };

  const statusOptions = userStatusSchema.options
    .filter((s) => s === 'ACTIVE' || s === 'INACTIVE')
    .map((status) => ({
      value: status,
      label: statusLabels[status],
    }));

  // resolve department options
  const resolvedDepartmentOptions =
    departmentOptions.length > 0
      ? departmentOptions
      : [
          {
            value: formValues.departmentId,
            label:
              getDepartmentName(formValues.departmentId) ??
              String(formValues.departmentId),
          },
        ];

  const handleSubmit = async (data: FormData) => {
    const formPayload = isCreate
      ? {
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
        }
      : {
          name: data.get('name') as string,
          surname: data.get('surname') as string,
          email: data.get('email') as string,
          role: data.get('role') as string,
          status: data.get('status') as string,
          departmentId: data.get('departmentId') as string,
          managerEmail: data.get('managerEmail') as string,
          notes: data.get('notes') as string,
        };

    const result = validationSchema.safeParse(formPayload);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors as Partial<
        Record<keyof FormErrors, string[]>
      >;
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
      if (isCreate) {
        const createData = result.data as UserFormModalCreatePayload;
        await onCreate({
          ...createData,
          notes: createData.notes?.trim() || null,
        });
        Toast.success(
          t('layout.toast.userCreated', {
            name: getFullName({
              name: createData.name,
              surname: createData.surname,
            }),
          })
        );
      } else {
        const editData = result.data as Omit<
          UserFormModalCreatePayload,
          'username' | 'password'
        >;
        await onSave({
          ...user!,
          ...editData,
          notes: editData.notes?.trim() || null,
        });
        Toast.success(
          t('layout.toast.userUpdated', {
            name: getFullName({
              name: editData.name,
              surname: editData.surname,
            }),
          })
        );
      }
      onClose();
    } catch {
      setSubmitError(
        isCreate
          ? t('users.modals.create.submitError')
          : t('users.modals.edit.submitError')
      );
      Toast.error(
        isCreate
          ? t('layout.toast.userCreateFailed')
          : t('layout.toast.userUpdateFailed')
      );
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      testId={isCreate ? undefined : 'user-edit'}
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={
        isCreate
          ? t('users.modals.create.ariaLabel')
          : t('users.modals.edit.ariaLabel')
      }
      title={
        <h2 className="text-2xl font-bold">
          {isCreate
            ? t('users.modals.create.title')
            : t('users.modals.edit.title')}
        </h2>
      }
      headerRight={
        <IconButton
          data-testid={isCreate ? 'close-button' : 'edit-close-button'}
          onClick={onClose}
          aria-label={t('users.modals.common.closeAria')}
        >
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={
        <div className="flex justify-end">
          <Button
            data-testid={isCreate ? 'create-user-button' : 'button-save'}
            type="submit"
            form={formId}
            className="shadow-none"
            disabled={isSaving || isSaveDisabled}
          >
            {isSaving
              ? t('users.modals.common.saving')
              : t('users.modals.common.save')}
          </Button>
        </div>
      }
    >
      <Form.Root
        noValidate
        id={formId}
        key={formKey}
        onChange={onFormChange}
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
                <FormDropdown
                  data-testid="user-role"
                  id="user-role"
                  name="role"
                  label={t(`${fieldsKey}.role`)}
                  defaultValue={formValues.role}
                  error={!!errors.role}
                  errorMessage={errors.role}
                  options={roleOptions}
                />
              </Form.Control>
            </Form.Field>

            <Form.Field name="status">
              <Form.Control asChild>
                <FormDropdown
                  data-testid="user-status"
                  id="user-status"
                  name="status"
                  label={t(`${fieldsKey}.status`)}
                  defaultValue={formValues.status}
                  error={!!errors.status}
                  errorMessage={errors.status}
                  options={statusOptions}
                />
              </Form.Control>
            </Form.Field>
          </div>

          {isCreate && (
            <Form.Field name="username">
              <Form.Control asChild>
                <FormInput
                  data-testid="user-username"
                  id="user-username"
                  name="username"
                  type="text"
                  label={t(`${fieldsKey}.username`)}
                  defaultValue={createInitialValues.username}
                  error={!!errors.username}
                  errorMessage={errors.username}
                />
              </Form.Control>
            </Form.Field>
          )}

          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <Form.Field name="name">
              <Form.Control asChild>
                <FormInput
                  data-testid="user-name"
                  id="user-first-name"
                  name="name"
                  type="text"
                  label={t(`${fieldsKey}.firstName`)}
                  defaultValue={formValues.name}
                  error={!!errors.name}
                  errorMessage={errors.name}
                />
              </Form.Control>
            </Form.Field>

            <Form.Field name="surname">
              <Form.Control asChild>
                <FormInput
                  data-testid="user-surname"
                  id="user-last-name"
                  name="surname"
                  type="text"
                  label={t(`${fieldsKey}.lastName`)}
                  defaultValue={formValues.surname}
                  error={!!errors.surname}
                  errorMessage={errors.surname}
                />
              </Form.Control>
            </Form.Field>
          </div>

          <Form.Field name="email">
            <Form.Control asChild>
              <FormInput
                data-testid="user-email"
                id="user-email"
                name="email"
                type="email"
                label={t(`${fieldsKey}.email`)}
                defaultValue={formValues.email}
                error={!!errors.email}
                errorMessage={errors.email}
              />
            </Form.Control>
          </Form.Field>

          {isCreate && (
            <Form.Field name="password">
              <Form.Control asChild>
                <FormInput
                  data-testid="user-password"
                  id="user-password"
                  name="password"
                  type="password"
                  label={t('users.modals.create.fields.password')}
                  defaultValue={createInitialValues.password}
                  error={!!errors.password}
                  errorMessage={errors.password}
                />
              </Form.Control>
            </Form.Field>
          )}

          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <Form.Field name="departmentId">
              <Form.Control asChild>
                <FormDropdown
                  data-testid="user-department-id"
                  id="user-department"
                  name="departmentId"
                  label={t(`${fieldsKey}.department`)}
                  defaultValue={formValues.departmentId}
                  error={!!errors.departmentId}
                  errorMessage={errors.departmentId}
                  options={resolvedDepartmentOptions}
                />
              </Form.Control>
            </Form.Field>

            <Form.Field name="managerEmail">
              <Form.Control asChild>
                <FormInput
                  data-testid="user-manager-email"
                  id="user-manager-email"
                  name="managerEmail"
                  type="email"
                  label={t(`${fieldsKey}.managerEmail`)}
                  defaultValue={formValues.managerEmail}
                  error={!!errors.managerEmail}
                  errorMessage={errors.managerEmail}
                />
              </Form.Control>
            </Form.Field>
          </div>

          <Form.Field name="notes">
            <Form.Control asChild>
              <FormInput
                data-testid="user-note"
                id="user-notes"
                name="notes"
                type="text"
                label={t(`${fieldsKey}.notes`)}
                defaultValue={formValues.notes ?? ''}
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
