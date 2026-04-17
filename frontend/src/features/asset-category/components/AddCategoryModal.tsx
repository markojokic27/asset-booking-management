import CloseIcon from '@mui/icons-material/Close'
import { FormInput } from '../../../components/ui/FormInput'
import { Button } from '../../../components/ui/Button'
import { useState, useRef } from 'react'
import { useForm } from 'react-hook-form'
import { FormDropdown } from '../../../components/ui/FormDropdown'
import { MultiSelect } from '../../../components/ui/MultiSelect'
import { createCategory } from '../api/categoryApi'
import { Checkbox } from "radix-ui";
import { CheckIcon } from "@radix-ui/react-icons";


const assetOptions = [
    { value: 'MacBookPro', label: 'MacBook Pro' },
    { value: 'HPElitebook', label: 'HP Elitebook' }
]

type Props = {
    open: boolean
    onClose: () => void
}

const bookingPeriodOptions = [
    { value: 'HOUR', label: 'Hour' },
    { value: 'DAY', label: 'Day' },
    //{ value: 'WEEK', label: 'Week' },
    //{ value: 'MONTH', label: 'Month' }
] as const

type FormValues = {
    name: string
    description: string
    bookingPeriod: 'DAY' | 'HOUR'
    assets?: string[]
    approval: boolean
    picture?: string
}

export const AddCategoryModal: React.FC<Props> = ({ open, onClose }) => {
    if (!open) return null

    const { register, handleSubmit, control, setValue, formState: { errors } } = useForm<FormValues>({
        defaultValues: {
            bookingPeriod: 'DAY',
            approval: false
        }
    })

    const [imagePreview, setImagePreview] = useState<string | undefined>()
    const fileInputRef = useRef<HTMLInputElement | null>(null)

    const onSubmit = async (data: FormValues) => {
        try {
            const payload = {
                name: data.name,
                description: data.description,
                bookingPeriod: data.bookingPeriod,
                assets: data.assets,
                approval: data.approval
            }

            await createCategory(payload)

            onClose() // close modal after success
        } catch (err) {
            console.error('Error creating category:', err)
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
            aria-label="Asset details"
            onMouseDown={(e) => {
                if (e.target === e.currentTarget) onClose()
            }}
        >
            <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">

                {/* HEADER */}
                <div className="relative flex items-center justify-center px-8 pt-6 pb-4">
                    <div className="text-center text-xl font-bold">Add New Category</div>

                    <Button
                        type="button"
                        onClick={onClose}
                        aria-label="Close"
                        className="absolute right-8 inline-flex bg-white border-none cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
                    >
                        <CloseIcon className="pointer-events-none" />
                    </Button>
                </div>

                <div className="m-4 mx-8 h-px bg-(--color-table-border)"></div>

                {/* FORM */}
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className="flex gap-10 px-8 py-8">

                        {/* IMAGE */}
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

                        {/* INPUTS */}
                        <div className="flex flex-1 flex-col space-y-5">

                            <FormInput
                                id="asset-name"
                                label="Name"
                                error={!!errors.name}
                                errorMessage={errors.name?.message}
                                {...register('name', { required: 'Name is required' })}
                            />

                            <FormInput
                                id="asset-description"
                                label="Description"
                                {...register('description')}
                            />

                            <FormDropdown
                                label="Booking period"
                                options={bookingPeriodOptions}
                                error={!!errors.bookingPeriod}
                                errorMessage={errors.bookingPeriod?.message}
                                {...register('bookingPeriod', { required: 'Booking period is required' })}
                            />

                            <MultiSelect
                                name="assets"
                                control={control}
                                label="Assets"
                                options={assetOptions}
                            />

                            <div className="flex items-center gap-2">
                                <Checkbox.Root
                                    id="c1"
                                    onCheckedChange={(checked) =>
                                        setValue('approval', !!checked)
                                    }
                                    className="flex h-5 w-5 items-center justify-center rounded border border-(--color-table-border) shadow-sm data-[state=checked]:bg-(--color-primaryblue)"
                                    defaultChecked
                                >
                                    <Checkbox.Indicator className="text-white">
                                        <CheckIcon />
                                    </Checkbox.Indicator>
                                </Checkbox.Root>

                                <label
                                    htmlFor="c1"
                                    className="cursor-pointer text-sm"
                                >
                                    All assets from this category need Manager approval
                                </label>
                            </div>
                        </div>
                    </div>

                    <div className="mx-8 mt-5 h-px bg-(--color-table-border)"></div>

                    {/* SUBMIT */}
                    <div className="mt-5 flex justify-center gap-2">
                        <Button
                            type="submit"
                            className="mr-5 mb-5 h-10 w-70 px-6 py-4 font-bold"
                        >
                            Add
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    )
}