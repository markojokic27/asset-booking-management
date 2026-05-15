// External packages
import { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';

// Components
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';

// Types
import { assetStatuses, type AssetDto, type AssetStatus } from '../types';
import type { AssetCategoryDto } from '../../asset-category/types';

// Utilis
import { createAssetValidationSchema } from '../validation';

// API
import { updateAsset } from '../api/assetApi';
import { getAllCategories } from '../../asset-category/api/categoryApi';

type AssetEditModalProps = {
  isOpen: boolean;
  onClose: () => void;
  asset: AssetDto | null;
  onSave: (asset: AssetDto) => void;
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

//TODO: Refactoring
export const AssetEditModal = ({
  isOpen,
  onClose,
  asset,
  onSave,
}: AssetEditModalProps) => {
  const { t } = useTranslation();
  const assetEditSchema = useMemo(
    () =>
      createAssetValidationSchema(t).pick({
        name: true,
        categoryId: true,
        description: true,
        status: true,
        location: true,
      }),
    [t],
  );
  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState('');
  const [categories, setCategories] = useState<AssetCategoryDto[]>([]);
  const [categoriesLoading, setCategoriesLoading] = useState(false);
  const [categoriesError, setCategoriesError] = useState('');

  const fetchCategories = async () => {
    if (categoriesLoading) return;

    try {
      setCategoriesLoading(true);
      setCategoriesError('');

      const data = await getAllCategories();
      setCategories(data.content);
    } catch (error) {
      console.error('Failed to fetch categories:', error);
      setCategoriesError(t('assets.errors.loadCategories'));
    } finally {
      setCategoriesLoading(false);
    }
  };

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      setSubmitError('');
      setIsSubmitting(false);
      setCategories([]);
      void fetchCategories();
    }
  }, [isOpen, asset]);

  if (!isOpen || !asset) return null;

  const handleSubmit = async (data: FormData) => {
    const formValues = {
      name: data.get('name') as string,
      categoryId: Number(data.get('categoryId')),
      description: data.get('description') as string,
      status: data.get('status') as AssetStatus,
      location: data.get('location') as string,
    };

    const result = assetEditSchema.safeParse(formValues);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;

      setErrors({
        name: fieldErrors.name?.[0] || '',
        categoryId: fieldErrors.categoryId?.[0] || '',
        description: fieldErrors.description?.[0] || '',
        status: fieldErrors.status?.[0] || '',
        location: fieldErrors.location?.[0] || '',
      });

      return;
    }

    try {
      setIsSubmitting(true);
      setSubmitError('');

      const updatedAsset = await updateAsset(asset.id, {
        name: result.data.name.trim(),
        categoryId: result.data.categoryId,
        description: result.data.description?.trim() || '',
        status: result.data.status,
        location: result.data.location,
      });

      onSave(updatedAsset);
      onClose();
    } catch (error) {
      console.error('Failed to update asset:', error);
      setSubmitError(t('assets.errors.updateAsset'));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div data-testid="asset-modal"
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label={t('assets.modals.edit.aria')}
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-end px-8 pt-6 pb-4">
          <button
            data-testid="close-modal"
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
          key={asset.id}
          onSubmit={(event) => {
            event.preventDefault();
            const formData = new FormData(event.currentTarget);
            void handleSubmit(formData);
          }}
        >
          <div className="flex gap-10 px-8 py-8">
            <div className="flex flex-1 flex-col space-y-5">
              <Form.Field name="status">
                <Form.Control asChild>
                  <FormDropdown
                    data-testid="asset-status"
                    id="asset-status"
                    name="status"
                    label={t('assets.modals.fields.status')}
                    defaultValue={asset.status}
                    error={!!errors.status}
                    errorMessage={errors.status}
                    options={assetStatuses.map((status) => ({
                      value: status,
                      label: t(`assets.status.${status}`),
                    }))}
                  />
                </Form.Control>
              </Form.Field>

            <Form.Field name="categoryId">
              <Form.Control asChild>
                {categoriesLoading ? (
                  <FormDropdown
                    id="asset-category"
                    name="categoryId"
                    label={t('assets.modals.fields.category')}
                    options={[
                      {
                        value: '',
                        label: t('assets.modals.loadingCategories'),
                      },
                    ]}
                  />
                ) : (
                  <FormDropdown
                    key={`category-${asset.id}`}
                    data-testid="asset-category"
                    id="asset-category"
                    name="categoryId"
                    label={t('assets.modals.fields.category')}
                    defaultValue={String(asset.categoryId)}
                    error={!!errors.categoryId || !!categoriesError}
                    errorMessage={errors.categoryId || categoriesError}
                    options={categories.map((category) => ({
                      value: String(category.id),
                      label: category.name,
                    }))}
                  />
                )}
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
                    defaultValue={asset.name}
                    error={!!errors.name}
                    errorMessage={errors.name}
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
                    defaultValue={asset.description ?? ''}
                    error={!!errors.description}
                    errorMessage={errors.description}
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
                    defaultValue={asset.location}
                    label={t('assets.modals.fields.location')}
                    error={!!errors.location}
                    errorMessage={errors.location}
                  />
                </Form.Control>
              </Form.Field>
            </div>
          </div>

          <div className="mx-8 h-px bg-(--color-table-border)" />

          {submitError && (
            <p className="px-8 pt-4 text-sm text-red-500">{submitError}</p>
          )}

          <div className="flex justify-end px-8 py-5">
            <Form.Submit asChild>
              <Button
                data-testid="save-edit-button"
                type="submit"
                disabled={isSubmitting}
              >
                {isSubmitting
                  ? t('assets.modals.saving')
                  : t('assets.modals.save')}
              </Button>
            </Form.Submit>
          </div>
        </Form.Root>
      </div>
    </div>
  );
};
