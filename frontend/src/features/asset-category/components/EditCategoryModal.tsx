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
import type { AssetCategoryDto } from '../types';

type Props = {
  isOpen: boolean;
  onClose: () => void;
  category: AssetCategoryDto | null;
  onSave: (category: AssetCategoryDto) => Promise<void>;
};

type FormValues = {
  name: string;
  description: string;
  bookingPeriod: 'DAY' | 'HOUR' | 'WEEK' | 'MONTH';
  approval: boolean;
};

export const EditCategoryModal: React.FC<Props> = ({
  isOpen,
  onClose,
  category,
  onSave,
}) => {
  const { t } = useTranslation();
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
    defaultValues: {
      name: '',
      description: '',
      bookingPeriod: 'DAY',
      approval: false,
    },
  });

  useEffect(() => {
    if (isOpen && category) {
      setSubmitError(null);
      setIsSaving(false);
      reset({
        name: category.name ?? '',
        description: category.description ?? '',
        bookingPeriod: category.bookingPeriod as FormValues['bookingPeriod'],
        approval: category.approval ?? false,
      });
    }
  }, [isOpen, category, reset]);

  if (!isOpen || !category) return null;

  const formId = `asset-category-edit-form-${category.id}`;
  const approvalChecked = watch('approval');

  const onSubmit = async (data: FormValues) => {
    setSubmitError(null);
    setIsSaving(true);
    try {
      await onSave({
        ...category,
        name: data.name,
        description: data.description,
        bookingPeriod: data.bookingPeriod,
        approval: data.approval,
        lastModifiedAt: new Date(),
      });
      onClose();
    } catch (err) {
      console.error('Error updating category:', err);
      setSubmitError(t('assetCategories.modals.edit.submitError'));
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      ariaLabel={t('assetCategories.modals.edit.ariaLabel')}
      headerRight={
        <IconButton
          data-testid="category-close-modal"
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
        key={category.id}
        onSubmit={handleSubmit(onSubmit)}
        noValidate
      >
        <div
          data-testid="assetCategory-modal"
          className="flex flex-col gap-5"
        >
          {submitError && (
            <div className="rounded border border-red-300 bg-red-50 px-3 py-2 text-sm text-red-800">
              {submitError}
            </div>
          )}

          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            <FormInput
              data-testid="edit-category-name"
              id="edit-category-name"
              label={t('assetCategories.modals.edit.fields.name')}
              error={!!errors.name}
              errorMessage={errors.name?.message}
              {...register('name', {
                required: t('assetCategories.validation.nameRequired'),
              })}
            />

            <FormDropdown
              data-testid="edit-category-booking-period"
              label={t('assetCategories.modals.edit.fields.bookingPeriod')}
              options={bookingPeriodOptions}
              error={!!errors.bookingPeriod}
              errorMessage={errors.bookingPeriod?.message}
              {...register('bookingPeriod', {
                required: t('assetCategories.validation.bookingPeriodRequired'),
              })}
            />
          </div>

          <FormInput
            data-testid="edit-category-description"
            id="edit-category-description"
            label={t('assetCategories.modals.edit.fields.description')}
            error={!!errors.description}
            errorMessage={errors.description?.message}
            {...register('description')}
          />

          <FormControlLabel
            className="m-0 items-start gap-2"
            control={
              <Checkbox
                data-testid="edit-category-approval-checkbox"
                id="edit-category-approval"
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
                {t('assetCategories.modals.edit.fields.approvalLabel')}
              </span>
            }
          />
        </div>
      </form>
    </Modal>
  );
};
