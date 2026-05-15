// External packages
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

// Components
import { Button } from '../../../components/ui/Button';
import { FormDropdown } from '../../../components/ui/FormDropdown';
import { FormInput } from '../../../components/ui/FormInput';
import { IconButton } from '../../../components/ui/IconButton';
import { Modal } from '../../../components/ui/Modal';

// Types
import type { AssetCategoryDto } from '../types';

// API
import { createCategory } from '../api/categoryApi';

// Icons
import CloseIcon from '@mui/icons-material/Close';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';

type Props = {
    open: boolean;
    onClose: () => void;
    onCreate: (category: AssetCategoryDto) => void;
}

type FormValues = {
    name: string;
    description: string;
    bookingPeriod: 'DAY' | 'HOUR' | 'WEEK' | 'MONTH';
    approval: boolean;
}

export const AddCategoryModal: React.FC<Props> = ({ open, onClose, onCreate }) => {
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
        if (open) {
            setSubmitError(null);
            setIsSaving(false);
            reset({
                name: '',
                description: '',
                bookingPeriod: 'DAY',
                approval: false,
            });
        }
    }, [open, reset]);

    if (!open) return null;
    const formId = 'asset-category-create-form';
    const approvalChecked = watch('approval');

    const onSubmit = async (data: FormValues) => {
        setSubmitError(null);
        setIsSaving(true);
        try {
            const created = await createCategory({
                name: data.name,
                description: data.description,
                bookingPeriod: data.bookingPeriod,
                approval: data.approval,
            });
            onCreate(created);
            onClose();
        } catch (err) {
            console.error('Error creating category:', err);
            setSubmitError(t('assetCategories.errors.createFailed'));
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <Modal
            isOpen={open}
            onClose={onClose}
            ariaLabel={t('assetCategories.modals.add.ariaLabel')}
            title={<div className="text-xl font-bold">{t('assetCategories.modals.add.title')}</div>}
            headerRight={
                <IconButton
                    data-testid="category-close-button"
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
                        {isSaving ? t('assetCategories.modals.add.saving') : t('assetCategories.modals.add.submit')}
                    </Button>
                </div>
            }
        >
            <form id={formId} onSubmit={handleSubmit(onSubmit)} noValidate>
                <div data-testid="category-modal" className="flex flex-col gap-5">
                    {submitError && (
                        <div className="rounded border border-red-300 bg-red-50 px-3 py-2 text-sm text-red-800">
                            {submitError}
                        </div>
                    )}

                    <FormInput
                        data-testid="category-name"
                        id="asset-category-name"
                        label={t('assetCategories.modals.add.fields.name')}
                        error={!!errors.name}
                        errorMessage={errors.name?.message}
                        {...register('name', { required: t('assetCategories.validation.nameRequired') })}
                    />

                    <FormInput
                        data-testid="category-description"
                        id="asset-category-description"
                        label={t('assetCategories.modals.add.fields.description')}
                        {...register('description')}
                    />

                    <FormDropdown
                        data-testid="category-booking-period"
                        label={t('assetCategories.modals.add.fields.bookingPeriod')}
                        options={bookingPeriodOptions}
                        error={!!errors.bookingPeriod}
                        errorMessage={errors.bookingPeriod?.message}
                        {...register('bookingPeriod', { required: t('assetCategories.validation.bookingPeriodRequired') })}
                    />

                    <FormControlLabel
                        className="m-0 items-start gap-2"
                        control={
                            <Checkbox
                                data-testid="category-approval-checkbox"
                                id="asset-category-approval"
                                checked={approvalChecked}
                                onChange={(e) => setValue('approval', e.target.checked, { shouldDirty: true })}
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
                                {t('assetCategories.modals.add.fields.approvalLabel')}
                            </span>
                        }
                    />
                </div>
            </form>
        </Modal>
    );
}