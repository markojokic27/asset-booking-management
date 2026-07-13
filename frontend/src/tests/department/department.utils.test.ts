import { describe, expect, test } from 'vitest';

import {
  buildDepartmentNameById,
  formatDepartmentName,
  getDepartmentNameById,
} from '../../features/department/utils/department';

const t = ((key: string, options?: { defaultValue?: string }) =>
  options?.defaultValue ?? key) as never;

describe('department utils', () => {
  test('formatDepartmentName uses translation key', () => {
    const translate = ((key: string) =>
      key === 'departments.names.DEVOPS' ? 'DevOps team' : key) as never;

    expect(formatDepartmentName('DEVOPS', translate)).toBe('DevOps team');
  });

  test('getDepartmentNameById resolves from map', () => {
    const namesById = buildDepartmentNameById(
      [{ id: 3, name: 'ARCHITECTURE' }],
      t,
    );

    expect(getDepartmentNameById(3, namesById)).toBe('Architecture');
    expect(getDepartmentNameById(99, namesById)).toBeUndefined();
  });
});
