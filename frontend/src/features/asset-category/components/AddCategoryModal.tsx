import { useEffect, useRef, useState } from 'react';
import * as Form from '@radix-ui/react-form';
import CloseIcon from '@mui/icons-material/Close';
import { Button } from '../../../components/ui/Button';
import { FormInput } from '../../../components/ui/FormInput';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { assetCategoryValidationSchema } from '../validation';

type Props = {
  open: boolean;
  onClose: () => void;
};

type FormErrors = {
  name: string;
  description: string;
  bookingPeriod: string;
  approval: string;
};

const initialErrors: FormErrors = {
  name: '',
  description: '',
  bookingPeriod: '',
  approval: '',
};

const bookingPeriodOptions = [
  { value: 1, label: 'Hour' },
  { value: 2, label: 'Day' },
  { value: 3, label: 'Week' },
  { value: 4, label: 'Month' },
];

export const AddCategoryModal: React.FC<Props> = ({ open, onClose }) => {
  const [errors, setErrors] = useState<FormErrors>(initialErrors);
  const [approvalValue, setApprovalValue] = useState(false);
  const [imagePreview, setImagePreview] = useState<string | undefined>();
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  useEffect(() => {
    if (open) {
      setErrors(initialErrors);
      setApprovalValue(false);
      setImagePreview(undefined);
    }
  }, [open]);

  if (!open) return null;

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
      description: (data.get('description') as string) || undefined,
      bookingPeriod: data.get('bookingPeriod') as string,
      approval: approvalValue,
    };

    const result = assetCategoryValidationSchema.safeParse(formValues);

    if (!result.success) {
      const fieldErrors = result.error.flatten().fieldErrors;
      setErrors({
        name: fieldErrors.name?.[0] || '',
        description: fieldErrors.description?.[0] || '',
        bookingPeriod: fieldErrors.bookingPeriod?.[0] || '',
        approval: fieldErrors.approval?.[0] || '',
      });
      return;
    }

    // TODO: Connect with BE and DB so new category can be added in database
    onClose();
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
      role="dialog"
      aria-modal="true"
      aria-label="Asset details"
      onMouseDown={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
        <div className="flex items-center justify-between px-8 pt-6 pb-4">
          <div className="text-xl font-bold">Add New Category</div>
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
                  data-testid="category-picture"
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
              <Form.Field name="name">
                <Form.Control asChild>
                  <FormInput
                    data-testid="category-name"
                    id="category-name"
                    name="name"
                    type="text"
                    label="Name"
                    error={!!errors.name}
                    errorMessage={errors.name}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="description">
                <Form.Control asChild>
                  <FormInput
                    data-testid="category-description"
                    id="category-description"
                    name="description"
                    type="text"
                    label="Description"
                    error={!!errors.description}
                    errorMessage={errors.description}
                  />
                </Form.Control>
              </Form.Field>

              <Form.Field name="bookingPeriod">
                <Form.Control asChild>
                  <FormDropdown
                    data-testid="category-booking-period"
                    id="category-booking-period"
                    name="bookingPeriod"
                    label="Booking Period"
                    defaultValue=""
                    error={!!errors.bookingPeriod}
                    errorMessage={errors.bookingPeriod}
                    options={[
                      { value: '', label: 'Select booking period' },
                      ...bookingPeriodOptions,
                    ]}
                  />
                </Form.Control>
              </Form.Field>

              {/* TODO: fetch all assets so we can assign them to category */}

              <div className="flex flex-col gap-1">
                <div className="flex items-center gap-2 font-bold">
                  <input
                    data-testid="category-approval-checkbox"
                    type="checkbox"
                    id="approval"
                    checked={approvalValue}
                    onChange={(e) => setApprovalValue(e.target.checked)}
                  />
                  <label htmlFor="approval">
                    All Assets must be approved by Manager
                  </label>
                </div>
                {errors.approval && (
                  <span className="text-sm text-red-500">{errors.approval}</span>
                )}
              </div>
            </div>
          </div>

          <div className="mx-8 h-px bg-(--color-table-border)" />

          <div className="flex justify-end px-8 py-5">
            <Form.Submit asChild>
              <Button data-testid="add-category-button" type="submit">
                Add
              </Button>
            </Form.Submit>
          </div>
        </Form.Root>
      </div>
    </div>
  );
};