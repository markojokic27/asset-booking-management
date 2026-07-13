import type { TFunction } from 'i18next';
import { z } from 'zod';

export const assetStatusSchema = z.enum(['ACTIVE', 'INACTIVE', 'DAMAGED']);

export function createAssetValidationSchema(t: TFunction) {
  return z.object({
    name: z
      .string()
      .trim()
      .min(1, t('assets.validation.nameRequired'))
      .max(100, t('assets.validation.nameMax')),

    categoryId: z.coerce
      .number({ message: t('assets.validation.categoryRequired') })
      .int()
      .positive(t('assets.validation.categoryRequired')),

    description: z
      .string()
      .trim()
      .max(255, t('assets.validation.descriptionMax'))
      .optional(),

    status: assetStatusSchema,

    location: z
      .string()
      .trim()
      .min(1, t('assets.validation.locationRequired'))
      .max(255, t('assets.validation.locationMax')),
  });
}

export type AssetFormValues = z.infer<ReturnType<typeof createAssetValidationSchema>>;
