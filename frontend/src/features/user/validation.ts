import type { TFunction } from 'i18next';
import { z } from 'zod';

export const userRoleSchema = z.enum(['EMPLOYEE', 'ADMIN', 'MANAGER']);
export const userStatusSchema = z.enum(['ACTIVE', 'INACTIVE', 'STUDENT', 'LEFT_COMPANY']);

export function createUserValidationSchema(t: TFunction) {
  return z.object({
    username: z
      .string()
      .min(3, t('users.validation.username.min'))
      .max(50, t('users.validation.username.max'))
      .regex(/^[a-zA-Z0-9._-]+$/, t('users.validation.username.pattern')),
    surname: z
      .string()
      .trim()
      .min(1, t('users.validation.surname.required'))
      .min(3, t('users.validation.surname.min'))
      .max(100, t('users.validation.surname.max'))
      .regex(/^\p{L}+(?:[ -]\p{L}+)*$/u, t('users.validation.surname.pattern')),
    name: z
      .string()
      .trim()
      .min(1, t('users.validation.name.required'))
      .min(3, t('users.validation.name.min'))
      .max(100, t('users.validation.name.max'))
      .regex(/^\p{L}+(?:[ -]\p{L}+)*$/u, t('users.validation.name.pattern')),
    email: z
      .email(t('users.validation.email.invalid'))
      .max(254, t('users.validation.email.max')),
    password: z
      .string()
      .min(8, t('users.validation.password.min'))
      .max(50, t('users.validation.password.max')),
    role: userRoleSchema,
    status: userStatusSchema,
    departmentId: z.coerce
      .number({ message: t('users.validation.department.required') })
      .int()
      .positive(t('users.validation.department.required')),
    managerEmail: z
      .email(t('users.validation.managerEmail.invalid'))
      .max(254, t('users.validation.managerEmail.max')),
    notes: z
      .string()
      .max(1000, t('users.validation.notes.max'))
      .optional(),
  });
}

export type UserFormValues = z.infer<ReturnType<typeof createUserValidationSchema>>;
