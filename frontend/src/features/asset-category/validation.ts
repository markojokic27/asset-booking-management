import { z } from 'zod';

export const bookingPeriodSchema = z.enum(['HOUR', 'DAY', 'WEEK', 'MONTH']);

export const assetCategoryValidationSchema = z.object({
  name: z
    .string()
    .trim()
    .min(1, 'Naziv kategorije je obavezan')
    .max(100, 'Naziv može imati najviše 100 znakova'),

  description: z
    .string()
    .trim()
    .max(255, 'Opis može imati najviše 255 znakova')
    .optional(),

  bookingPeriod: bookingPeriodSchema,

  approval: z.coerce.boolean({
    message: 'Approval je obavezan',
  }),
});

export type AssetCategoryFormValues = z.infer<typeof assetCategoryValidationSchema>;
