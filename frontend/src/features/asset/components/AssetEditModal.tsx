import { useEffect, useRef, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { categories, assetStatuses, type AssetDto, type AssetStatus } from '../types';
import { assetValidationSchema } from '../validation';

const assetEditSchema = assetValidationSchema.pick({
  name: true,
  categoryId: true,
  description: true,
  status: true,
});

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
};

const initialErrors: FormErrors = {
  name: '',
  categoryId: '',
  description: '',
  status: '',
};

const statusLabels: Record<AssetStatus, string> = {
  ACTIVE: 'Active',
  INACTIVE: 'Inactive',
  DAMAGED: 'Damaged',
};

export const AssetEditModal = ({
  isOpen,
  onClose,
  asset,
  onSave,
}: AssetEditModalProps) => {
  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [imagePreview, setImagePreview] = useState<string | undefined>();
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  useEffect(() => {
    if (isOpen) {
      setErrors(initialErrors);
      setImagePreview(asset?.imageUrl);
    }
  }, [isOpen, asset]);

  if (!isOpen || !asset) return null;

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
      categoryId: data.get('categoryId') as string,
      description: data.get('description') as string,
      status: data.get('status') as AssetStatus,
    };

    const result = assetEditSchema.safeParse(formValues);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;

      setErrors({
        name: fieldErrors.name?.[0] || '',
        categoryId: fieldErrors.categoryId?.[0] || '',
        description: fieldErrors.description?.[0] || '',
        status: fieldErrors.status?.[0] || '',
      });

      return;
    }

    // Map the select option id back to the display label shown in the table
    const selectedCategory = categories.find(
      (_category, index) => index + 1 === result.data.categoryId
    );

    onSave({
      ...asset,
      ...result.data,
      description: result.data.description?.trim() || undefined,
      imageUrl: imagePreview,
      categoryName: selectedCategory ?? asset.categoryName,
      lastModifiedAt: new Date(),
    });
    onClose();
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label="Edit asset"
      onMouseDown={(e) => {
        // Only close on backdrop clicks, not when interacting inside the modal
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
          key={asset.id}
          onSubmit={(event) => {
            event.preventDefault();
            const formData = new FormData(event.currentTarget);
            handleSubmit(formData);
          }}
        >
          <div className="flex gap-10 px-8 py-8">
            <div className="flex w-[260px] flex-col items-center justify-center">
              <div className="relative w-full">
                {imagePreview ? (
                  <img
                    src={imagePreview}
                    alt={asset.name}
                    className="h-[170px] w-full rounded-lg border border-(--color-table-border) object-cover shadow-(--shadow-card) blur-[1.5px]"
                  />
                ) : (
                  <div className="flex h-[170px] w-full items-center justify-center rounded-lg border border-dashed border-(--color-table-border) bg-(--color-modal-placeholder-bg)">
                  </div>
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
                  className="absolute top-1/2 left-1/2 px-3 py-1.5 text-xs shadow-none -translate-x-1/2 -translate-y-1/2"
                  // Trigger the hidden native file input from the styled button
                  onClick={() => fileInputRef.current?.click()}
                >
                  {imagePreview ? 'Change photo' : 'Upload photo'}
                </Button>
              </div>
            </div>
            <div className="flex flex-1 flex-col space-y-5">
              <Form.Field name="status">
                <Form.Control asChild>
                  <FormDropdown
                    id="asset-status"
                    name="status"
                    label="Status"
                    defaultValue={asset.status}
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
                    id="asset-category"
                    name="categoryId"
                    label="Asset category"
                    defaultValue={String(asset.categoryId)}
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
                  <FormInput
                    id="asset-name"
                    name="name"
                    type="text"
                    label="Name"
                    defaultValue={asset.name}
                    error={!!errors.name}
                    errorMessage={errors.name}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="description">
                <Form.Control asChild>
                  <FormInput
                    id="asset-description"
                    name="description"
                    type="text"
                    label="Description"
                    defaultValue={asset.description ?? ''}
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
              <Button type="submit">Save</Button>
            </Form.Submit>
          </div>
        </Form.Root>
      </div>
    </div>
  );
};
