import { z } from 'zod';

export const assetStatusSchema = z.enum(['AVAILABLE', 'UNAVAILABLE']);

export const assetValidationSchema = z.object({
  name: z
    .string()
    .trim()
    .min(1, 'Naziv asseta je obavezan')
    .max(100, 'Naziv može imati najviše 100 znakova'),

  categoryId: z.coerce
    .number({ message: 'Kategorija je obavezna' })
    .int()
    .positive('Kategorija je obavezna'),

  description: z
    .string()
    .trim()
    .max(255, 'Opis može imati najviše 255 znakova')
    .optional(),

  code: z
    .string()
    .trim()
    .min(1, 'QR code je obavezan')
    .max(2000, 'QR code može imati najviše 2000 znakova'),

  status: assetStatusSchema,

  location: z
    .string()
    .trim()
    .min(1, 'Lokacija je obavezna')
    .max(255, 'Lokacija može imati najviše 255 znakova'),
});

export type AssetFormValues = z.infer<typeof assetValidationSchema>;
