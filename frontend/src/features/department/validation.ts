import { z } from 'zod';

export const DepartmentNameSchema = z.enum(['ADVANCED_TECHNOLOGIES', 'ARCHITECTURE', 'CLOUD_DATA_MANAGEMENT', 'DEVOPS', 'FINANCE','HR','MOBILE_SECURITY','OPERATIONS',
  'SECURITY_SYSTEMS']);

export const departmentValidationSchema = z.object({
  name: DepartmentNameSchema,
  managerId: z.coerce
    .number({ message: 'Manager is required' })
    .int()
    .positive('Manager is required'),
});

export type DepartmentFormValues = z.infer<typeof departmentValidationSchema>;

