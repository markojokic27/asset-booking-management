import CloseIcon from '@mui/icons-material/Close'
import { FormInput } from '../../../components/ui/FormInput'
import { Button } from '../../../components/ui/Button'
import { useState, useRef, useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { FormDropdown } from '../../../components/ui/FormDropdown'
import { Checkbox } from 'radix-ui'
import { CheckIcon } from '@radix-ui/react-icons'
import type { AssetCategoryDto } from '../types'

type Props = {
    isOpen: boolean
    onClose: () => void
    category: AssetCategoryDto | null
    onSave: (category: AssetCategoryDto) => Promise<void>
}

const bookingPeriodOptions = [
    { value: 'HOUR', label: 'Hour' },
    { value: 'DAY', label: 'Day' },
    { value: 'WEEK', label: 'Week' },
    { value: 'MONTH', label: 'Month' }
] as const

type FormValues = {
    name: string
    description: string
    bookingPeriod: 'DAY' | 'HOUR' | 'WEEK' | 'MONTH'
    approval: boolean
    picture?: string
}

export const EditCategoryModal: React.FC<Props> = ({
    isOpen,
    onClose,
    category,
    onSave,
}) => {
    const [imagePreview, setImagePreview] = useState<string | undefined>()
    const fileInputRef = useRef<HTMLInputElement | null>(null)

    const {
        register,
        handleSubmit,
        setValue,
        reset,
        formState: { errors },
    } = useForm<FormValues>({
        defaultValues: {
            name: '',
            description: '',
            bookingPeriod: 'DAY',
            approval: false,
        },
    })

    useEffect(() => {
        if (isOpen && category) {
            reset({
                name: category.name ?? '',
                description: category.description ?? '',
                bookingPeriod: category.bookingPeriod as FormValues['bookingPeriod'],
                approval: category.approval ?? false,
            })
        }
    }, [isOpen, category, reset])

    if (!isOpen || !category) return null

    const onSubmit = async (data: FormValues) => {
        try {
            await onSave({
                ...category,
                name: data.name,
                description: data.description,
                bookingPeriod: data.bookingPeriod,
                approval: data.approval,
                lastModifiedAt: new Date(),
            })

            onClose()
        } catch (err) {
            console.error('Error updating category:', err)
        }
    }

    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            setImagePreview(URL.createObjectURL(file))
        }
    }

    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
            role="dialog"
            aria-modal="true"
            aria-label="Edit category"
            onMouseDown={(e) => {
                if (e.target === e.currentTarget) onClose()
            }}
        >
            <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">
                <div className="relative flex items-center justify-center px-8 pt-6 pb-4">
                    <div className="text-center text-xl font-bold">Edit Category</div>

                    <Button
                        data-testid="edit-category-close-button"
                        type="button"
                        onClick={onClose}
                        aria-label="Close"
                        className="absolute right-8 inline-flex bg-white border-none cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
                    >
                        <CloseIcon className="pointer-events-none" />
                    </Button>
                </div>

                <div className="m-4 mx-8 h-px bg-(--color-table-border)"></div>

                <form onSubmit={handleSubmit(onSubmit)}>
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
                            <FormInput
                                data-testid="edit-category-name"
                                id="category-name"
                                label="Name"
                                error={!!errors.name}
                                errorMessage={errors.name?.message}
                                {...register('name', { required: 'Name is required' })}
                            />

                            <FormInput
                                data-testid="edit-category-description"
                                id="category-description"
                                label="Description"
                                {...register('description')}
                            />

                            <FormDropdown
                                data-testid="edit-category-booking-period"
                                label="Booking period"
                                options={bookingPeriodOptions}
                                error={!!errors.bookingPeriod}
                                errorMessage={errors.bookingPeriod?.message}
                                {...register('bookingPeriod', { required: 'Booking period is required' })}
                            />

                            <div className="flex items-center gap-2">
                                <Checkbox.Root
                                    data-testid="edit-category-approval-checkbox"
                                    id="edit-category-approval"
                                    checked={undefined}
                                    onCheckedChange={(checked) =>
                                        setValue('approval', !!checked)
                                    }
                                    className="flex h-5 w-5 items-center justify-center rounded border border-(--color-table-border) shadow-sm data-[state=checked]:bg-(--color-primaryblue)"
                                    defaultChecked={category.approval}
                                >
                                    <Checkbox.Indicator className="text-white">
                                        <CheckIcon />
                                    </Checkbox.Indicator>
                                </Checkbox.Root>

                                <label
                                    htmlFor="edit-category-approval"
                                    className="cursor-pointer text-sm"
                                >
                                    All assets from this category need Manager approval
                                </label>
                            </div>
                        </div>
                    </div>

                    <div className="mx-8 mt-5 h-px bg-(--color-table-border)"></div>

                    <div className="mt-5 flex justify-center gap-2">
                        <Button data-testid="save-category-button"
                            type="submit"
                            className="mr-5 mb-5 h-10 w-70 px-6 py-4 font-bold"
                        >
                            Save
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    )
}