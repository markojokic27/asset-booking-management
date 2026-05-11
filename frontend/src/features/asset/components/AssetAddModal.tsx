// External packages
import { useEffect, useState } from 'react';
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
import { assetValidationSchema } from '../validation';

// API
import { getAllCategories } from '../../asset-category/api/categoryApi';
import { createAsset } from '../api/assetApi';

const assetAddSchema = assetValidationSchema.pick({
  name: true,
  categoryId: true,
  description: true,
  status: true,
  location: true,
});

type AssetAddModalProps = {
  isOpen: boolean;
  onClose: () => void;
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

const statusLabels: Record<AssetStatus, string> = {
  ACTIVE: 'Active',
  INACTIVE: 'Inactive',
  DAMAGED: 'Damaged',
  DELETED: 'Deleted',
};

export const AssetAddModal = ({
  isOpen,
  onClose,
  onSave,
}: AssetAddModalProps) => {
  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [categories, setCategories] = useState<AssetCategoryDto[]>([]);
  const [categoriesLoading, setCategoriesLoading] = useState(false);
  const [categoriesError, setCategoriesError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState('');

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      //setCategories([]);
      //setCategoriesError('');
      //setCategoriesLoading(false);
      setSubmitError('');
      setIsSubmitting(false);
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const fetchCategories = async () => {
    if (categories.length > 0 || categoriesLoading) return;

    try {
      setCategoriesLoading(true);
      setCategoriesError('');

      const data = await getAllCategories();
      setCategories(data.content);
    } catch (error) {
      console.error('Failed to fetch categories:', error);
      setCategoriesError('Failed to load categories.');
    } finally {
      setCategoriesLoading(false);
    }
  };

  const handleSubmit = async (data: FormData) => {
    const formValues = {
      name: data.get('name') as string,
      categoryId: data.get('categoryId') as string,
      description: data.get('description') as string,
      status: data.get('status') as AssetStatus,
      location: data.get('location') as string,
    };

    const result = assetAddSchema.safeParse(formValues);

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

      const payload = {
        name: result.data.name.trim(),
        categoryId: result.data.categoryId,
        description: result.data.description?.trim() || '',
        status: result.data.status,
        location: result.data.location.trim(),
      };

      const createdAsset = await createAsset(payload);

      onSave(createdAsset);
      onClose();
    } catch (error) {
      console.error('Failed to create asset:', error);
      setSubmitError('Failed to create asset.');
    } finally {
      setIsSubmitting(false);
    }

    onClose();
  };

  return (
    <div
      data-testid="add-asset-modal"
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label="Add asset"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-end px-8 pt-6 pb-4">
          <button
            data-testid="close-asset-modal"
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
                    label="Status"
                    defaultValue="ACTIVE"
                    error={!!errors.status}
                    errorMessage={errors.status}
                    options={assetStatuses.map((status) => ({
                      value: status,
                      label: statusLabels[status],
                    }))}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="categoryId">
                <Form.Control asChild>
                  <FormDropdown
                    data-testid="asset-category"
                    id="asset-category"
                    name="categoryId"
                    label="Category"
                    defaultValue=""
                    error={!!errors.categoryId || !!categoriesError}
                    errorMessage={errors.categoryId || categoriesError}
                    onFocus={fetchCategories}
                    onClick={fetchCategories}
                    options={[
                      {
                        value: '',
                        label: categoriesLoading
                          ? 'Loading categories...'
                          : 'Select category',
                      },
                      ...categories.map((category) => ({
                        value: category.id,
                        label: category.name,
                      })),
                    ]}
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
                    label="Name"
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
                    label="Location"
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
                    label="Description"
                    error={!!errors.description}
                    errorMessage={errors.description}
                  />
                </Form.Control>
              </Form.Field>
            </div>
          </div>

          <div className="mx-8 h-px bg-(--color-table-border)" />

          <div className="flex justify-end px-8 py-5">
            {submitError && (
              <p className="px-8 pt-4 text-sm text-red-500">{submitError}</p>
            )}
            <Form.Submit asChild>
              <Button
                data-testid="save-asset-button"
                type="submit"
                disabled={isSubmitting}
              >
                Add Asset
              </Button>
            </Form.Submit>
          </div>
        </Form.Root>
      </div>
    </div>
  );
};
