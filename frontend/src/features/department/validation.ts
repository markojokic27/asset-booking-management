import { z } from 'zod';

export const departmentValidationSchema = z.object({
  name: z.string().trim().min(1, 'Naziv departmenta je obavezan'),
  managerId: z.coerce
    .number({ message: 'Manager je obavezan' })
    .int()
    .positive('Manager je obavezan'),
});

export type DepartmentFormValues = z.infer<typeof departmentValidationSchema>;
