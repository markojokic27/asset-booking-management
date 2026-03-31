import { z } from 'zod';

export const departmentValidationSchema = z.object({
  name: z.string().trim().min(1, 'Department name is required'),
  managerId: z.coerce
    .number({ message: 'Manager is required' })
    .int()
    .positive('Manager is required'),
});

export type DepartmentFormValues = z.infer<typeof departmentValidationSchema>;
