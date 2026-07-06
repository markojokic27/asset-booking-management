// external dependencies
import { useEffect, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { isAxiosError } from 'axios';
import { useTranslation } from 'react-i18next';

// components
import { Button } from '../../../components/ui/Button';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';

// api
import { changeOwnPassword } from '../api/users';

// validation
import { createUserValidationSchema } from '../validation';

// types
import type { UserDto } from '../types';
import { Toast } from '../../../components/ui/Toast';

type ChangePasswordFieldErrors = {
  currentPassword: string;
  newPassword: string;
  confirmNewPassword: string;
};

const emptyPasswordFieldErrors: ChangePasswordFieldErrors = {
  currentPassword: '',
  newPassword: '',
  confirmNewPassword: '',
};

export type ChangePasswordModalProps = {
  user: UserDto;
  isOpen: boolean;
  onClose: () => void;
};

export function ChangePasswordModal({ user, isOpen, onClose }: Readonly<ChangePasswordModalProps>) {
  const { t } = useTranslation();
  const [fieldErrors, setFieldErrors] = useState<ChangePasswordFieldErrors>(
    emptyPasswordFieldErrors
  );
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const formId = `account-password-form-${user.id}`;

  useEffect(() => {
    if (!isOpen) return;
    // Reset modal state whenever modal is reopened
    setFieldErrors(emptyPasswordFieldErrors);
    setSubmitError(null);
    setIsSaving(false);
  }, [isOpen, user.id]);

  if (!isOpen) return null;

  const handleSubmit = async (data: FormData) => {
    // Extract raw form values before validation
    const raw = {
      currentPassword: data.get('currentPassword') as string,
      newPassword: data.get('newPassword') as string,
      confirmNewPassword: data.get('confirmNewPassword') as string,
    };

    const next: ChangePasswordFieldErrors = { ...emptyPasswordFieldErrors };

    if (!raw.currentPassword.trim()) {
      next.currentPassword = t('account.password.validation.currentRequired');
    }

    // Validate new password against shared password rules
    const newPwResult = createUserValidationSchema(t).shape.password.safeParse(
      raw.newPassword
    );
    if (!newPwResult.success) {
      next.newPassword = newPwResult.error.issues[0]?.message ?? '';
    }

    // Check if new passwords match
    if (raw.newPassword !== raw.confirmNewPassword) {
      next.confirmNewPassword = t(
        'account.password.validation.confirmMismatch'
      );
    }

    // If there are any validation errors, set field errors and return
    if (next.currentPassword || next.newPassword || next.confirmNewPassword) {
      setFieldErrors(next);
      return;
    }

    setFieldErrors(emptyPasswordFieldErrors);
    setSubmitError(null);
    setIsSaving(true);
    try {
      await changeOwnPassword(user.id, {
        currentPassword: raw.currentPassword.trim(),
        newPassword: raw.newPassword,
      });
      Toast.success(t('layout.toast.passwordChanged'));
      onClose();
    } catch (err) {
      if (isAxiosError(err) && err.response?.status === 401) {
        setSubmitError(t('account.password.errors.wrongCurrent'));
      } else {
        setSubmitError(t('account.password.errors.submit'));
      }
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('account.password.modalTitle')}
      title={
        <h2 className="text-xl font-bold tracking-wide text-black dark:text-white">
          {t('account.password.modalTitle')}
        </h2>
      }
      headerRight={
        <IconButton
          onClick={onClose}
          aria-label={t('account.password.closeAria')}
        >
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={
        <div className="flex flex-wrap justify-end gap-3">
          <Button
            data-testid="account-password-cancel"
            type="button"
            variant="outline"
            className="shadow-none"
            disabled={isSaving}
            onClick={onClose}
          >
            {t('account.password.cancel')}
          </Button>
          <Button
            type="submit"
            form={formId}
            className="shadow-none"
            disabled={isSaving}
            data-testid="account-password-submit"
          >
            {isSaving
              ? t('account.password.saving')
              : t('account.password.save')}
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
            <div className="rounded border border-red-300 bg-red-50 px-3 py-2 text-sm text-red-800 dark:border-red-800 dark:bg-red-950/40 dark:text-red-200">
              {submitError}
            </div>
          )}

          <Form.Field name="currentPassword">
            <Form.Control asChild>
              <FormInput
                data-testid="account-password-current"
                id="account-password-current"
                name="currentPassword"
                type="password"
                autoComplete="current-password"
                label={t('account.password.fields.current')}
                error={!!fieldErrors.currentPassword}
                errorMessage={fieldErrors.currentPassword}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="newPassword">
            <Form.Control asChild>
              <FormInput
                data-testid="account-password-new"
                id="account-password-new"
                name="newPassword"
                type="password"
                autoComplete="new-password"
                label={t('account.password.fields.new')}
                error={!!fieldErrors.newPassword}
                errorMessage={fieldErrors.newPassword}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="confirmNewPassword">
            <Form.Control asChild>
              <FormInput
                data-testid="account-password-confirm"
                id="account-password-confirm"
                name="confirmNewPassword"
                type="password"
                autoComplete="new-password"
                label={t('account.password.fields.confirm')}
                error={!!fieldErrors.confirmNewPassword}
                errorMessage={fieldErrors.confirmNewPassword}
              />
            </Form.Control>
          </Form.Field>
        </div>
      </Form.Root>
    </Modal>
  );
}
