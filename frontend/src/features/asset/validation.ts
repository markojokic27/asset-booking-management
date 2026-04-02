import { z } from 'zod';

export const assetStatusSchema = z.enum(['ACTIVE', 'INACTIVE', 'DAMAGED']);

export const assetValidationSchema = z.object({
  name: z
    .string()
    .trim()
    .min(1, 'Asset name is required')
    .max(100, 'Name must be at most 100 characters long'),

  categoryId: z.coerce
    .number({ message: 'Category is required' })
    .int()
    .positive('Category is required'),

  description: z
    .string()
    .trim()
    .max(255, 'Description must be at most 255 characters long')
    .optional(),

  code: z
    .string()
    .trim()
    .min(1, 'QR code is required')
    .max(2000, 'QR code must be at most 2000 characters long'),

  status: assetStatusSchema,

  location: z
    .string()
    .trim()
    .min(1, 'Location is required')
    .max(255, 'Location must be at most 255 characters long'),
});

export type AssetFormValues = z.infer<typeof assetValidationSchema>;
