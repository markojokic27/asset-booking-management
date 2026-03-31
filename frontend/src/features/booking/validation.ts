import { z } from 'zod';

export const bookingStatusSchema = z.enum([
  'SUCCESSFUL',
  'PENDING',
  'REJECTED',
]);

export const bookingValidationSchema = z
  .object({
    userId: z.coerce
      .number({ message: 'User is required' })
      .int()
      .positive('User is required'),

    assetId: z.coerce
      .number({ message: 'Asset is required' })
      .int()
      .positive('Asset is required'),

    status: bookingStatusSchema,

    bookingStart: z.coerce.date({
      message: 'Booking start is required',
    }),

    bookingEnd: z.coerce.date({
      message: 'Booking end is required',
    }),

    note: z
      .string()
      .trim()
      .max(1000, 'Note must be at most 1000 characters long')
      .optional(),
  })
  .refine((data) => data.bookingEnd > data.bookingStart, {
    path: ['bookingEnd'],
    message: 'Booking end must be after booking start',
  });

export type BookingFormValues = z.infer<typeof bookingValidationSchema>;
