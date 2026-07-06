import type { TFunction } from 'i18next';
import { z } from 'zod';

export const DepartmentNameSchema = z.enum([
  'ADVANCE_TECHNOLOGY',
  'SECURE_SERVICES',
  'ARCHITECTURE',
  'FINANCE_AND_BUSINESS_ADMINISTRATION',
  'MOBILE_AND_SECURITY',
  'SYSTEM_TEST',
  'HUMAN_RESOURCES',
  'CLOUD_AND_DATA_MANAGEMENT',
  'DEVOPS',
]);

export type DepartmentName = z.infer<typeof DepartmentNameSchema>;

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
