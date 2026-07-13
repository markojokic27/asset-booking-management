// external packages
import { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';

// api
import { getDepartments } from '../api/departments';

// types
import type { DepartmentDto } from '../types';

// utils
import {
  buildDepartmentNameById,
  formatDepartmentName,
  getDepartmentNameById,
} from '../utils/department';

export function useDepartments() {
  const { t } = useTranslation();
  const [departments, setDepartments] = useState<DepartmentDto[]>([]);

  useEffect(() => {
    // check if the component is mounted
    let mounted = true;

    // get departments from api
    (async () => {
      try {
        const data = await getDepartments();
        // set the departments if the component is mounted
        if (mounted) setDepartments(data);
      } catch {
        if (mounted) setDepartments([]);
      }
    })();

    return () => {
      mounted = false;
    };
  }, []);

  // build a map of department id to department name
  const namesById = useMemo(
    () => buildDepartmentNameById(departments, t),
    [departments, t],
  );

  // get department name by id
  const getDepartmentName = (departmentId: number | null | undefined) =>
    getDepartmentNameById(departmentId, namesById);

  // get department options
  const departmentOptions = useMemo(
    () =>
      departments.map((department) => ({
        value: department.id,
        label: formatDepartmentName(department.name, t),
      })),
    [departments, t],
  );

  return { getDepartmentName, departmentOptions };
}
