import { z } from 'zod';

export const userRoleSchema = z.enum(['EMPLOYEE', 'ADMIN', 'MANAGER']);
export const userStatusSchema = z.enum(['ACTIVE', 'INACTIVE']);

export const userValidationSchema = z.object({
  username: z
    .string()
    .min(3, 'Username must be at least 3 characters long')
    .max(50, 'Username must be at most 50 characters long')
    .regex(/^[a-zA-Z0-9._-]+$/, 'Only letters, numbers, ., _, and - are allowed'),
  surname: z
    .string()
    .min(1, 'Surname is required')
    .max(100, 'Surname must be at most 100 characters long'),
  name: z
    .string()
    .min(1, 'Name is required')
    .max(100, 'Name must be at most 100 characters long'),
  email: z
    .email('Email is not in a valid format')
    .max(254, 'Email must be at most 254 characters long'),
  password: z
    .string()
    .min(8, 'Password must be at least 8 characters long')
    .max(50, 'Password must be at most 50 characters long'),
  role: userRoleSchema,
  status: userStatusSchema,
  departmentId: z.coerce
    .number({ message: 'Department is required' })
    .int()
    .positive('Department is required'),
  managerEmail: z
    .email("Manager's email is not in a valid format")
    .max(254, "Manager's email must be at most 254 characters long"),
  notes: z
    .string()
    .max(1000, 'Notes must be at most 1000 characters long')
    .optional(),
});

export type UserFormValues = z.infer<typeof userValidationSchema>;
