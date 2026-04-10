import { useEffect, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { FormInput } from '../../../components/ui/FormInput';
import { userValidationSchema } from '../validation';

const userEditSchema = userValidationSchema.pick({
  name: true,
  surname: true,
  email: true,
});

export type UserEditModalUser = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
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
};

const initialErrors: FormErrors = {
  name: '',
  surname: '',
  email: '',
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
    };

    const result = userEditSchema.safeParse(formValues);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;
      setErrors({
        name: fieldErrors.name?.[0] || '',
        surname: fieldErrors.surname?.[0] || '',
        email: fieldErrors.email?.[0] || '',
      });
      return;
    }

    onSave({
      ...user,
      firstName: result.data.name,
      lastName: result.data.surname,
      email: result.data.email,
    });
    onClose();
  };

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

