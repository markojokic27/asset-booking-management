import { z } from 'zod';

export const userRoleSchema = z.enum(['EMPLOYEE', 'ADMIN', 'MANAGER']);
export const userStatusSchema = z.enum(['ACTIVE', 'INACTIVE']);

export const userValidationSchema = z.object({
  username: z
    .string()
    .min(3, 'Username mora imati barem 3 znaka')
    .max(50, 'Username može imati najviše 50 znakova')
    .regex(/^[a-zA-Z0-9._-]+$/, 'Dozvoljena su slova, brojevi, ., _, -'),
  surname: z
    .string()
    .min(1, 'Prezime je obavezno')
    .max(100, 'Prezime može imati najviše 100 znakova'),
  name: z
    .string()
    .min(1, 'Ime je obavezno')
    .max(100, 'Ime može imati najviše 100 znakova'),
  email: z
    .email('Email nije ispravnog formata')
    .max(254, 'Email može imati najviše 254 znaka'),
  password: z
    .string()
    .min(8, 'Lozinka mora imati barem 8 znakova')
    .max(50, 'Lozinka može imati najviše 50 znakova'),
  role: userRoleSchema,
  status: userStatusSchema,
  departmentId: z.coerce
    .number({ message: 'Department je obavezan' })
    .int()
    .positive('Department je obavezan'),
  managerEmail: z
    .email('Manager email nije ispravnog formata')
    .max(254, 'Manager email može imati najviše 254 znaka'),
  notes: z.string().max(1000, 'Notes može imati najviše 1000 znakova').optional(),
});

export type UserFormValues = z.infer<typeof userValidationSchema>;
