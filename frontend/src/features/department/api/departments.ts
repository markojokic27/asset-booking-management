// api
import api from '../../../shared/api';

// types
import type { DepartmentDto } from '../types';

type PageResponse<T> = {
  content: T[];
};

let departmentsCache: Promise<DepartmentDto[]> | null = null;

export const getDepartments = async (): Promise<DepartmentDto[]> => {
  // get departments from api
  departmentsCache ??= api
    .get<PageResponse<DepartmentDto>>('/departments', {
      params: { page: 0, size: 200 },
    })
    .then((res) => res.data.content)
    .catch((error) => {
      departmentsCache = null;
      throw error;
    });

  // return promise of departments
  return departmentsCache;
};
