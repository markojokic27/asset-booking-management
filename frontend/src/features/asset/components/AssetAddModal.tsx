import { useEffect, useRef, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import {
  categories,
  assetStatuses,
  type AssetDto,
  type AssetStatus,
} from '../types';
import { assetValidationSchema } from '../validation';

const assetAddSchema = assetValidationSchema.pick({
  name: true,
  categoryId: true,
  description: true,
  status: true,
  code: true,
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
  code: string;
  location: string;
};

const initialErrors: FormErrors = {
  name: '',
  categoryId: '',
  description: '',
  status: '',
  code: '',
  location: '',
};

const statusLabels: Record<AssetStatus, string> = {
  ACTIVE: 'Active',
  INACTIVE: 'Inactive',
  DAMAGED: 'Damaged',
};

export const AssetAddModal = ({
  isOpen,
  onClose,
  onSave,
}: AssetAddModalProps) => {
  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [imagePreview, setImagePreview] = useState<string | undefined>();
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      setImagePreview(undefined);
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      if (typeof reader.result === 'string') {
        setImagePreview(reader.result);
      }
    };
    reader.readAsDataURL(file);
  };

  const handleSubmit = (data: FormData) => {
    const formValues = {
      name: data.get('name') as string,
      categoryId: Number(data.get('categoryId')),
      description: data.get('description') as string,
      status: data.get('status') as AssetStatus,
      code: data.get('code') as string,
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
        code: fieldErrors.code?.[0] || '',
        location: fieldErrors.location?.[0] || '',
      });

      return;
    }

    const selectedCategory = categories.find(
      (_category, index) => index + 1 === result.data.categoryId
    );

    onSave({
      id: crypto.randomUUID(),
      ...result.data,
      description: result.data.description?.trim() || undefined,
      imageUrl: imagePreview,
      categoryName: selectedCategory ?? 'Unknown',
      createdAt: new Date(),
      lastModifiedAt: new Date(),
    });

    onClose();
  };

  return (
    <div
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
            handleSubmit(formData);
          }}
        >
          <div className="flex gap-10 px-8 py-8">
            <div className="flex w-65 flex-col items-center justify-center">
              <div className="relative w-full">
                {imagePreview ? (
                  <img
                    src={imagePreview}
                    alt="preview"
                    className="h-42.5 w-full rounded-lg border border-(--color-table-border) object-cover shadow-(--shadow-card) blur-[1.5px]"
                  />
                ) : (
                  <div className="flex h-42.5 w-full items-center justify-center rounded-lg border border-dashed border-(--color-table-border) bg-(--color-modal-placeholder-bg)" />
                )}

                <input
                  ref={fileInputRef}
                  type="file"
                  accept="image/*"
                  className="hidden"
                  onChange={handleImageChange}
                />

                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 px-3 py-1.5 text-xs shadow-none"
                  onClick={() => fileInputRef.current?.click()}
                >
                  {imagePreview ? 'Change photo' : 'Upload photo'}
                </Button>
              </div>
            </div>

            <div className="flex flex-1 flex-col space-y-5">
              <Form.Field name="status">
                <Form.Control asChild>
                  <FormDropdown data-testid="asset-status"
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
                  <FormDropdown data-testid="asset-category"
                    id="asset-category"
                    name="categoryId"
                    label="Category"
                    defaultValue=""
                    error={!!errors.categoryId}
                    errorMessage={errors.categoryId}
                    options={[
                      { value: '', label: 'Select category' },
                      ...categories.map((category, index) => ({
                        value: index + 1,
                        label: category,
                      })),
                    ]}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="name">
                <Form.Control asChild>
                  <FormInput data-testid="asset-name"
                    id="asset-name"
                    name="name"
                    type="text"
                    label="Name"
                    error={!!errors.name}
                    errorMessage={errors.name}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="code">
                <Form.Control asChild>
                  <FormInput data-testid="asset-code"
                    id="asset-code"
                    name="code"
                    type="text"
                    label="QR Code"
                    error={!!errors.code}
                    errorMessage={errors.code}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="location">
                <Form.Control asChild>
                  <FormInput data-testid="asset-location"
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
                  <FormInput data-testid="asset-description"
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
            <Form.Submit asChild>
              <Button data-testid="add-asset-button" type="submit">Add Asset</Button>
            </Form.Submit>
          </div>
        </Form.Root>
      </div>
    </div>
  );
};
