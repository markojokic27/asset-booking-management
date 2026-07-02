import type { TFunction } from 'i18next';
import { z } from 'zod';

export const bookingStatusSchema = z.enum([
  'PENDING',
  'REJECTED',
  'COMPLETED',
  'APPROVED',
  'CANCELLED',
]);

export function createBookingValidationSchema(t: TFunction) {
  return z
    .object({
      userId: z.coerce
        .number({ message: t('bookings.validation.userRequired') })
        .int()
        .positive(t('bookings.validation.userRequired')),

      assetId: z.coerce
        .number({ message: t('bookings.validation.assetRequired') })
        .int()
        .positive(t('bookings.validation.assetRequired')),

      status: bookingStatusSchema,

      bookingStart: z.date({
        message: t('bookings.validation.bookingStartRequired'),
      }),

      bookingEnd: z.date({
        message: t('bookings.validation.bookingEndRequired'),
      }),

      note: z
        .string()
        .trim()
        .max(1000, t('bookings.validation.noteMax'))
        .optional(),
    })
    .refine((data) => data.bookingEnd > data.bookingStart, {
      path: ['bookingEnd'],
      message: t('bookings.validation.endAfterStart'),
    });
}

export type BookingFormValues = z.infer<ReturnType<typeof createBookingValidationSchema>>;
