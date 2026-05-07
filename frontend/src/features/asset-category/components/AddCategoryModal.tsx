import CloseIcon from '@mui/icons-material/Close'
import { FormInput } from '../../../components/ui/FormInput'
import { Button } from '../../../components/ui/Button'
import { useForm } from 'react-hook-form'
import { FormDropdown } from '../../../components/ui/FormDropdown'
import { createCategory } from '../api/categoryApi'
import { Checkbox } from "radix-ui";
import { CheckIcon } from "@radix-ui/react-icons";
import { useTranslation } from 'react-i18next'

type Props = {
    open: boolean
    onClose: () => void
}

type FormValues = {
    name: string
    description: string
    bookingPeriod: 'DAY' | 'HOUR' | 'WEEK' | 'MONTH'
    approval: boolean
}

export const AddCategoryModal: React.FC<Props> = ({ open, onClose }) => {
    if (!open) return null
    const { t } = useTranslation()

    const bookingPeriodOptions = [
        { value: 'HOUR', label: t('assetCategories.bookingPeriod.hour') },
        { value: 'DAY', label: t('assetCategories.bookingPeriod.day') },
        { value: 'WEEK', label: t('assetCategories.bookingPeriod.week') },
        { value: 'MONTH', label: t('assetCategories.bookingPeriod.month') }
    ] as const

    const { register, handleSubmit, setValue, formState: { errors } } = useForm<FormValues>({
        defaultValues: {
            bookingPeriod: 'DAY',
            approval: false
        }
    })

    const onSubmit = async (data: FormValues) => {
        try {
            const payload = {
                name: data.name,
                description: data.description,
                bookingPeriod: data.bookingPeriod,
                approval: data.approval
            }

            await createCategory(payload)

            onClose() // close modal after success
        } catch (err) {
            console.error('Error creating category:', err)
        }
    }

    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center bg-(--color-modal-overlay) p-6"
            role="dialog"
            aria-modal="true"
            aria-label={t('assetCategories.modals.add.ariaLabel')}
            onMouseDown={(e) => {
                if (e.target === e.currentTarget) onClose()
            }}
        >
            <div className="w-full max-w-200 overflow-hidden rounded-2xl border border-(--color-table-border) bg-(--color-table-surface) text-(--color-table-text) shadow-(--shadow-card)">

                {/* HEADER */}
                <div className="relative flex items-center justify-center px-8 pt-6 pb-4">
                    <div className="text-center text-xl font-bold">{t('assetCategories.modals.add.title')}</div>

                    <Button data-testid="category-close-button"
                        type="button"
                        onClick={onClose}
                        aria-label={t('assetCategories.modals.common.closeAria')}
                        className="absolute right-8 inline-flex bg-white border-none cursor-pointer items-center justify-center rounded p-1.5 text-(--color-table-text) transition-colors hover:bg-(--color-table-row-hover) hover:text-(--color-primaryblue) active:scale-95"
                    >
                        <CloseIcon className="pointer-events-none" />
                    </Button>
                </div>

                <div className="m-4 mx-8 h-px bg-(--color-table-border)"></div>

                {/* FORM */}
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className="flex gap-10 px-8 py-8">

                        {/* INPUTS */}
                        <div className="flex flex-1 flex-col space-y-5">

                            <FormInput
                                data-testid="category-name"
                                id="asset-name"
                                label={t('assetCategories.modals.add.fields.name')}
                                error={!!errors.name}
                                errorMessage={errors.name?.message}
                                {...register('name', { required: t('assetCategories.modals.add.validation.nameRequired') })}
                            />

                            <FormInput
                                data-testid="category-description"
                                id="asset-description"
                                label={t('assetCategories.modals.add.fields.description')}
                                {...register('description')}
                            />

                            <FormDropdown
                                data-testid="category-booking-period"
                                label={t('assetCategories.modals.add.fields.bookingPeriod')}
                                options={bookingPeriodOptions}
                                error={!!errors.bookingPeriod}
                                errorMessage={errors.bookingPeriod?.message}
                                {...register('bookingPeriod', { required: t('assetCategories.modals.add.validation.bookingPeriodRequired') })}
                            />

                            <div className="flex items-center gap-2">
                                <Checkbox.Root
                                    data-testid="category-approval-checkbox"
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
                                    {t('assetCategories.modals.add.fields.approvalLabel')}
                                </label>
                            </div>
                        </div>
                    </div>

                    <div className="mx-8 mt-5 h-px bg-(--color-table-border)"></div>

                    {/* SUBMIT */}
                    <div className="mt-5 flex justify-center gap-2">
                        <Button
                            data-testid="add-category-button"
                            type="submit"
                            className="mr-5 mb-5 h-10 w-70 px-6 py-4 font-bold"
                        >
                            {t('assetCategories.modals.add.submit')}
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    )
}