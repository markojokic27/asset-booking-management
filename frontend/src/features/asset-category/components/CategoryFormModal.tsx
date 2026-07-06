// External packages
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';

// Components
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import { Toast } from '../../../components/ui/Toast';

// Types
import type { AssetCategoryDto, BookingPeriod } from '../types';

// API
import type { CreateCategoryRequest } from '../api/categoryApi';

type CategoryFormModalMode = 'create' | 'edit';

type FormValues = {
  name: string;
  description: string;
  bookingPeriod: BookingPeriod;
  approval: boolean;
};

export type CategoryFormModalProps = {
  isOpen: boolean;
  mode: CategoryFormModalMode;
  category: AssetCategoryDto | null;
  onClose: () => void;
  onCreate: (data: CreateCategoryRequest) => Promise<void>;
  onSave: (category: AssetCategoryDto) => Promise<void>;
};

const createInitialValues: FormValues = {
  name: '',
  description: '',
  bookingPeriod: 'DAY',
  approval: false,
};

function getFieldsKey(isCreate: boolean) {
  return isCreate
    ? 'assetCategories.modals.add.fields'
    : 'assetCategories.modals.edit.fields';
}

function getModalConfig(isCreate: boolean) {
  return isCreate
    ? ({
        containerTestId: 'category-modal',
        closeTestId: 'category-close-button',
        nameTestId: 'category-name',
        nameId: 'asset-category-name',
        bookingTestId: 'category-booking-period',
        descriptionTestId: 'category-description',
        descriptionId: 'asset-category-description',
        approvalTestId: 'category-approval-checkbox',
        approvalId: 'asset-category-approval',
        ariaLabelKey: 'assetCategories.modals.add.ariaLabel',
        titleKey: 'assetCategories.modals.add.title',
      } as const)
    : ({
        containerTestId: 'assetCategory-modal',
        closeTestId: 'category-close-modal',
        nameTestId: 'edit-category-name',
        nameId: 'edit-category-name',
        bookingTestId: 'edit-category-booking-period',
        descriptionTestId: 'edit-category-description',
        descriptionId: 'edit-category-description',
        approvalTestId: 'edit-category-approval-checkbox',
        approvalId: 'edit-category-approval',
        ariaLabelKey: 'assetCategories.modals.edit.ariaLabel',
        titleKey: 'assetCategories.modals.edit.title',
      } as const);
}

export const CategoryFormModal: React.FC<CategoryFormModalProps> = ({
  isOpen,
  mode,
  category,
  onClose,
  onCreate,
  onSave,
}) => {
  const { t } = useTranslation();
  const isCreate = mode === 'create';
  const fieldsKey = getFieldsKey(isCreate);
  const config = getModalConfig(isCreate);

  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const bookingPeriodOptions = [
    { value: 'HOUR', label: t('assetCategories.bookingPeriod.hour') },
    { value: 'DAY', label: t('assetCategories.bookingPeriod.day') },
  ] as const;

  const {
    register,
    handleSubmit,
    setValue,
    reset,
    watch,
    formState: { errors, isDirty },
  } = useForm<FormValues>({
    defaultValues: createInitialValues,
  });

  useEffect(() => {
    if (!isOpen) return;

    setSubmitError(null);
    setIsSaving(false);

    if (isCreate) {
      reset(createInitialValues);
      return;
    }

    if (category) {
      reset({
        name: category.name ?? '',
        description: category.description ?? '',
        bookingPeriod: category.bookingPeriod as FormValues['bookingPeriod'],
        approval: category.approval ?? false,
      });
    }
  }, [isOpen, isCreate, category, reset]);

  if (!isOpen || (!isCreate && !category)) return null;

  const formId = isCreate
    ? 'asset-category-create-form'
    : `asset-category-edit-form-${category!.id}`;
  const formKey = isCreate ? 'create' : String(category!.id);
  const approvalChecked = watch('approval');

  const onSubmit = async (data: FormValues) => {
    setSubmitError(null);
    setIsSaving(true);
    try {
      if (isCreate) {
        await onCreate({
          name: data.name,
          description: data.description,
          bookingPeriod: data.bookingPeriod,
          approval: data.approval,
        });
        Toast.success(t('layout.toast.categoryCreated'));
      } else {
        await onSave({
          ...category!,
          name: data.name,
          description: data.description,
          bookingPeriod: data.bookingPeriod,
          approval: data.approval,
          lastModifiedAt: new Date(),
        });
        Toast.success(t('layout.toast.categoryUpdated'));
      }
      onClose();
    } catch (err) {
      console.error(
        `Error ${isCreate ? 'creating' : 'updating'} category:`,
        err
      );
      setSubmitError(
        isCreate
          ? t('assetCategories.errors.createFailed')
          : t('assetCategories.modals.edit.submitError')
      );
      Toast.error(
        isCreate
          ? t('layout.toast.categoryCreateFailed')
          : t('layout.toast.categoryUpdateFailed')
      );
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t(config.ariaLabelKey)}
      title={<h2 className="text-2xl font-bold">{t(config.titleKey)}</h2>}
      headerRight={
        <IconButton
          data-testid={config.closeTestId}
          onClick={onClose}
          aria-label={t('assetCategories.modals.common.closeAria')}
        >
          <CloseIcon className="pointer-events-none" />
        </IconButton>
      }
      footer={
        <div className="flex justify-end">
          <Button
            data-testid="save-category-button"
            type="submit"
            form={formId}
            className="shadow-none"
            disabled={isSaving || (!isCreate && !isDirty)}
          >
            {isSaving
              ? t('assetCategories.modals.common.saving')
              : t('assetCategories.modals.common.save')}
          </Button>
        </div>
      }
    >
      <form
        id={formId}
        key={formKey}
        onSubmit={handleSubmit(onSubmit)}
        noValidate
      >
        <div
          data-testid={config.containerTestId}
          className="flex flex-col gap-5"
        >
          {submitError && (
            <div className="rounded border border-red-300 bg-red-50 px-3 py-2 text-sm text-red-800">
              {submitError}
            </div>
          )}

          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <FormInput
              data-testid={config.nameTestId}
              id={config.nameId}
              label={t(`${fieldsKey}.name`)}
              error={!!errors.name}
              errorMessage={errors.name?.message}
              {...register('name', {
                required: t('assetCategories.validation.nameRequired'),
              })}
            />

            <FormDropdown
              data-testid={config.bookingTestId}
              label={t(`${fieldsKey}.bookingPeriod`)}
              options={bookingPeriodOptions}
              error={!!errors.bookingPeriod}
              errorMessage={errors.bookingPeriod?.message}
              {...register('bookingPeriod', {
                required: t('assetCategories.validation.bookingPeriodRequired'),
              })}
            />
          </div>

          <FormInput
            data-testid={config.descriptionTestId}
            id={config.descriptionId}
            label={t(`${fieldsKey}.description`)}
            error={!!errors.description}
            errorMessage={errors.description?.message}
            {...register('description')}
          />

          <FormControlLabel
            className="m-0 items-start gap-2"
            control={
              <Checkbox
                data-testid={config.approvalTestId}
                id={config.approvalId}
                checked={approvalChecked}
                onChange={(e) =>
                  setValue('approval', e.target.checked, { shouldDirty: true })
                }
                sx={{
                  padding: 0,
                  marginTop: '2px',
                  color: 'var(--color-table-border)',
                  '&.Mui-checked': {
                    color: 'var(--color-primaryblue)',
                  },
                }}
              />
            }
            label={
              <span className="cursor-pointer text-sm">
                {t(`${fieldsKey}.approvalLabel`)}
              </span>
            }
          />
        </div>
      </form>
    </Modal>
  );
};
