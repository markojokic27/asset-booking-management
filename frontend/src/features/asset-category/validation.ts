import { z } from 'zod';

export const bookingPeriodSchema = z.enum(['HOUR', 'DAY', 'WEEK', 'MONTH']);

export const assetCategoryValidationSchema = z.object({
  name: z
    .string()
    .trim()
    .min(1, 'Category name is required')
    .max(100, 'Name must be at most 100 characters long'),

  description: z
    .string()
    .trim()
    .max(255, 'Description must be at most 255 characters long')
    .optional(),

  bookingPeriod: bookingPeriodSchema,

  approval: z.coerce.boolean({
    message: 'Approval is required',
  }),
});

export type AssetCategoryFormValues = z.infer<
  typeof assetCategoryValidationSchema
>;
