// External packages
import { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { z } from 'zod';

// Components
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';
import { Toast } from '../../../components/ui/Toast';

// Types
import { type AssetDto, type AssetStatus } from '../types';
import { assetStatusSchema, createAssetValidationSchema } from '../validation';
import type { AssetCategoryDto } from '../../asset-category/types';
import type { CreateAssetRequest } from '../api/assetApi';

// API
import { getAllCategories } from '../../asset-category/api/categoryApi';

// hooks
import { useEditFormChanges } from '../../../hooks/useEditFormChanges';

export type AssetFormModalCreatePayload = CreateAssetRequest;

type AssetFormModalMode = 'create' | 'edit';

type AssetFormModalProps = {
  isOpen: boolean;
  mode: AssetFormModalMode;
  asset: AssetDto | null;
  onClose: () => void;
  onCreate: (payload: AssetFormModalCreatePayload) => Promise<void>;
  onSave: (asset: AssetDto) => Promise<void>;
};

type FormErrors = {
  name: string;
  categoryId: string;
  description: string;
  status: string;
  location: string;
};

const initialErrors: FormErrors = {
  name: '',
  categoryId: '',
  description: '',
  status: '',
  location: '',
};

const createInitialValues: AssetFormModalCreatePayload = {
  name: '',
  categoryId: 0,
  status: 'ACTIVE',
  location: '',
  description: '',
};

export const AssetFormModal = ({
  isOpen,
  mode,
  asset,
  onClose,
  onCreate,
  onSave,
}: AssetFormModalProps) => {
  const { t } = useTranslation();
  const isCreate = mode === 'create';

  const validationSchema = useMemo(() => createAssetValidationSchema(t), [t]);

  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const { onFormChange, isSaveDisabled } = useEditFormChanges(
    !isCreate,
    `${isOpen}-${asset?.id ?? ''}`
  );

  const [categories, setCategories] = useState<AssetCategoryDto[]>([]);
  const [categoriesLoading, setCategoriesLoading] = useState(false);
  const [categoriesError, setCategoriesError] = useState('');

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      setSubmitError(null);
      setIsSaving(false);
      setCategoriesError('');
      setCategories([]);
    }
  }, [isOpen, mode, asset]);

  useEffect(() => {
    const fetchCategories = async () => {
      if (!isOpen || categoriesLoading) return;
      try {
        setCategoriesLoading(true);
        const data = await getAllCategories();
        setCategories(data.content);
      } catch (error) {
        console.error('Failed to fetch categories:', error);
        setCategoriesError(t('assets.errors.loadCategories'));
      } finally {
        setCategoriesLoading(false);
      }
    };

    void fetchCategories();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isOpen]);

  if (!isOpen || (!isCreate && !asset)) return null;

  const formValues = isCreate ? createInitialValues : asset!;
  const formId = isCreate
    ? 'asset-create-form'
    : `asset-edit-form-${asset!.id}`;
  const formKey = isCreate ? 'create' : String(asset!.id);

  const statusOptions = assetStatusSchema.options.map((status) => ({
    value: status,
    label: t(`assets.status.${status}`),
  }));

  const handleSubmit = async (data: FormData) => {
    const payload = {
      name: data.get('name') as string,
      categoryId: Number(data.get('categoryId')),
      description: data.get('description') as string,
      status: data.get('status') as AssetStatus,
      location: data.get('location') as string,
    };

    const result = validationSchema.safeParse(payload);

    if (!result.success) {
      const fieldErrors = z.flattenError(result.error).fieldErrors;
      setErrors({
        name: fieldErrors.name?.[0] || '',
        categoryId: fieldErrors.categoryId?.[0] || '',
        description: fieldErrors.description?.[0] || '',
        status: fieldErrors.status?.[0] || '',
        location: fieldErrors.location?.[0] || '',
      });
      return;
    }

    setSubmitError(null);
    setIsSaving(true);
    try {
      if (isCreate) {
        await onCreate({
          name: result.data.name.trim(),
          categoryId: result.data.categoryId,
          description: result.data.description?.trim() || '',
          status: result.data.status,
          location: result.data.location.trim(),
        });
        Toast.success(t('layout.toast.assetCreated'));
      } else {
        await onSave({
          ...asset!,
          name: result.data.name.trim(),
          categoryId: result.data.categoryId,
          description: result.data.description?.trim() || '',
          status: result.data.status,
          location: result.data.location.trim(),
        });
        Toast.success(t('layout.toast.assetUpdated'));
      }
      onClose();
    } catch {
      setSubmitError(
        isCreate
          ? t('assets.errors.createAsset')
          : t('assets.errors.updateAsset')
      );
      Toast.error(
        isCreate
          ? t('layout.toast.assetCreateFailed')
          : t('layout.toast.assetUpdateFailed')
      );
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      testId={isCreate ? 'add-asset-modal' : 'edit-asset-modal'}
      ariaLabel={
        isCreate ? t('assets.modals.add.title') : t('assets.modals.edit.title')
      }
      title={
        <h2 className="text-2xl font-bold">
          {isCreate
            ? t('assets.modals.add.title')
            : t('assets.modals.edit.title')}
        </h2>
      }
      headerRight={
        <IconButton
          data-testid={isCreate ? 'close-asset-modal' : 'close-edit-modal'}
          onClick={onClose}
          aria-label={t('assets.modals.close')}
        >
          <CloseIcon className="pointer-events-none" />
        </IconButton>
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

          <Form.Field name="status">
            <Form.Control asChild>
              <FormDropdown
                data-testid="asset-status"
                id="asset-status"
                name="status"
                label={t('assets.modals.fields.status')}
                defaultValue={formValues.status}
                error={!!errors.status}
                errorMessage={errors.status}
                options={statusOptions}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="categoryId">
            <Form.Control asChild>
              <FormDropdown
                key={`asset-category-${formKey}-${categories.length}`}
                data-testid="asset-category"
                id="asset-category"
                name="categoryId"
                label={t('assets.modals.fields.category')}
                defaultValue={String(formValues.categoryId)}
                error={!!errors.categoryId || !!categoriesError}
                errorMessage={errors.categoryId || categoriesError}
                options={
                  categoriesLoading
                    ? [
                        {
                          value: String(formValues.categoryId),
                          label: t('assets.modals.loadingCategories'),
                        },
                      ]
                    : [
                        ...(isCreate
                          ? [
                              {
                                value: '0',
                                label: t('assets.modals.add.selectCategory'),
                              },
                            ]
                          : []),
                        ...categories.map((category) => ({
                          value: String(category.id),
                          label: category.name,
                        })),
                      ]
                }
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="name">
            <Form.Control asChild>
              <FormInput
                data-testid="asset-name"
                id="asset-name"
                name="name"
                type="text"
                label={t('assets.modals.fields.name')}
                defaultValue={formValues.name}
                error={!!errors.name}
                errorMessage={errors.name}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="location">
            <Form.Control asChild>
              <FormInput
                data-testid="asset-location"
                id="asset-location"
                name="location"
                type="text"
                label={t('assets.modals.fields.location')}
                defaultValue={formValues.location ?? ''}
                error={!!errors.location}
                errorMessage={errors.location}
              />
            </Form.Control>
          </Form.Field>

          <Form.Field name="description">
            <Form.Control asChild>
              <FormInput
                data-testid="asset-description"
                id="asset-description"
                name="description"
                type="text"
                label={t('assets.modals.fields.description')}
                defaultValue={formValues.description ?? ''}
                error={!!errors.description}
                errorMessage={errors.description}
              />
            </Form.Control>
          </Form.Field>
        </div>

        <div className="mt-6 flex justify-end">
          <Form.Submit asChild>
            <Button
              data-testid={isCreate ? 'save-asset-button' : 'save-edit-button'}
              type="submit"
              className="shadow-none"
              disabled={isSaving || isSaveDisabled}
            >
              {isSaving ? t('assets.modals.saving') : t('assets.modals.save')}
            </Button>
          </Form.Submit>
        </div>
      </Form.Root>
    </Modal>
  );
};
