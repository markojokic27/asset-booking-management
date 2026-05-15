import type { TFunction } from 'i18next';
import { z } from 'zod';

export const bookingPeriodSchema = z.enum(['HOUR', 'DAY', 'WEEK', 'MONTH']);

export function createAssetCategoryValidationSchema(t: TFunction) {
  return z.object({
    name: z
      .string()
      .trim()
      .min(1, t('assetCategories.validation.nameRequired'))
      .max(100, t('assetCategories.validation.nameMax')),

    description: z
      .string()
      .trim()
      .max(255, t('assetCategories.validation.descriptionMax'))
      .optional(),

    bookingPeriod: z.coerce
      .number({ message: t('assetCategories.validation.bookingPeriodRequired') })
      .int()
      .positive(t('assetCategories.validation.bookingPeriodRequired')),

    approval: z.boolean({
      message: t('assetCategories.validation.approvalRequired'),
    }),
  });
}

export type AssetCategoryFormValues = z.infer<
  ReturnType<typeof createAssetCategoryValidationSchema>
>;
