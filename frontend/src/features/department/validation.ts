import type { TFunction } from 'i18next';
import { z } from 'zod';

export const DepartmentNameSchema = z.enum([
  'ADVANCED_TECHNOLOGIES',
  'ARCHITECTURE',
  'CLOUD_DATA_MANAGEMENT',
  'DEVOPS',
  'FINANCE',
  'HR',
  'MOBILE_SECURITY',
  'OPERATIONS',
  'SECURITY_SYSTEMS',
]);

export function createDepartmentValidationSchema(t: TFunction) {
  return z.object({
    name: DepartmentNameSchema,
    managerId: z.coerce
      .number({ message: t('departments.validation.managerRequired') })
      .int()
      .positive(t('departments.validation.managerRequired')),
  });
}

export type DepartmentFormValues = z.infer<
  ReturnType<typeof createDepartmentValidationSchema>
>;
