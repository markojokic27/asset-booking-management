import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import CloseIcon from '@mui/icons-material/Close';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';

import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import type { CreateCategoryRequest } from '../api/categoryApi';
import type { AssetCategoryDto } from '../types';

type CategoryFormModalMode = 'create' | 'edit';

type FormValues = {
  name: string;
  description: string;
  bookingPeriod: 'DAY' | 'HOUR' | 'WEEK' | 'MONTH';
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

function getFieldsKey(isCreate: boolean): string {
  return isCreate
    ? 'assetCategories.modals.add.fields'
    : 'assetCategories.modals.edit.fields';
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

  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const bookingPeriodOptions = [
    { value: 'HOUR', label: t('assetCategories.bookingPeriod.hour') },
    { value: 'DAY', label: t('assetCategories.bookingPeriod.day') },
    { value: 'WEEK', label: t('assetCategories.bookingPeriod.week') },
    { value: 'MONTH', label: t('assetCategories.bookingPeriod.month') },
  ] as const;

  const {
    register,
    handleSubmit,
    setValue,
    reset,
    watch,
    formState: { errors },
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

  const formId = isCreate ? 'asset-category-create-form' : `asset-category-edit-form-${category!.id}`;
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
      } else {
        await onSave({
          ...category!,
          name: data.name,
          description: data.description,
          bookingPeriod: data.bookingPeriod,
          approval: data.approval,
          lastModifiedAt: new Date(),
        });
      }
      onClose();
    } catch (err) {
      console.error(`Error ${isCreate ? 'creating' : 'updating'} category:`, err);
      setSubmitError(
        isCreate
          ? t('assetCategories.errors.createFailed')
          : t('assetCategories.modals.edit.submitError'),
      );
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={
        isCreate
          ? t('assetCategories.modals.add.ariaLabel')
          : t('assetCategories.modals.edit.ariaLabel')
      }
      headerRight={
        <IconButton
          data-testid={isCreate ? 'category-close-button' : 'category-close-modal'}
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
            disabled={isSaving}
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
          data-testid={isCreate ? 'category-modal' : 'assetCategory-modal'}
          className="flex flex-col gap-5"
        >
          {submitError && (
            <div className="rounded border border-red-300 bg-red-50 px-3 py-2 text-sm text-red-800">
              {submitError}
            </div>
          )}

          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <FormInput
              data-testid={isCreate ? 'category-name' : 'edit-category-name'}
              id={isCreate ? 'asset-category-name' : 'edit-category-name'}
              label={t(`${fieldsKey}.name`)}
              error={!!errors.name}
              errorMessage={errors.name?.message}
              {...register('name', {
                required: t('assetCategories.validation.nameRequired'),
              })}
            />

            <FormDropdown
              data-testid={
                isCreate ? 'category-booking-period' : 'edit-category-booking-period'
              }
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
            data-testid={isCreate ? 'category-description' : 'edit-category-description'}
            id={isCreate ? 'asset-category-description' : 'edit-category-description'}
            label={t(`${fieldsKey}.description`)}
            error={!!errors.description}
            errorMessage={errors.description?.message}
            {...register('description')}
          />

          <FormControlLabel
            className="m-0 items-start gap-2"
            control={
              <Checkbox
                data-testid={
                  isCreate ? 'category-approval-checkbox' : 'edit-category-approval-checkbox'
                }
                id={isCreate ? 'asset-category-approval' : 'edit-category-approval'}
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
