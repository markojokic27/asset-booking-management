import type { DepartmentName } from './validation';

export type { DepartmentName };

export type DepartmentDto = {
  id: number;
  name: DepartmentName;
  managerId?: number | null;
};
