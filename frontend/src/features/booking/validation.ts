import { z } from 'zod';

export const bookingStatusSchema = z.enum([
  'SUCCESSFUL',
  'PENDING',
  'REJECTED',
]);

export const bookingValidationSchema = z
  .object({
    userId: z.coerce
      .number({ message: 'Korisnik je obavezan' })
      .int()
      .positive('Korisnik je obavezan'),

    assetId: z.coerce
      .number({ message: 'Asset je obavezan' })
      .int()
      .positive('Asset je obavezan'),

    status: bookingStatusSchema,

    bookingStart: z.coerce.date({
      message: 'Početak rezervacije je obavezan',
    }),

    bookingEnd: z.coerce.date({
      message: 'Kraj rezervacije je obavezan',
    }),

    note: z
      .string()
      .trim()
      .max(1000, 'Napomena može imati najviše 1000 znakova')
      .optional(),
  })
  .refine((data) => data.bookingEnd > data.bookingStart, {
    path: ['bookingEnd'],
    message: 'Kraj rezervacije mora biti nakon početka',
  });

export type BookingFormValues = z.infer<typeof bookingValidationSchema>;
