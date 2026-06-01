// external packages
import type { TFunction } from 'i18next';

// types
import type { DepartmentName } from '../types';

// format department name
export function formatDepartmentName(name: DepartmentName, t: TFunction): string {
  // return the department name from i18next or the default value
  return t(`departments.names.${name}`, {
    defaultValue: name
      .split('_')
      .map((part) => part.charAt(0) + part.slice(1).toLowerCase())
      .join(' '),
  });
}

// build department name by id
export function buildDepartmentNameById(
  departments: { id: number; name: DepartmentName }[],
  t: TFunction,
): Map<number, string> {
  // build a map of department id to department name
  return new Map(
    departments.map((department) => [
      department.id,
      formatDepartmentName(department.name, t),
    ]),
  );
}

// get department name by id
export function getDepartmentNameById(
  departmentId: number | null | undefined,
  namesById: Map<number, string>,
): string | undefined {
  if (departmentId == null) return undefined;
  return namesById.get(departmentId);
}
