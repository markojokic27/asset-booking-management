import api from '../../../shared/api';
import type { UserDto } from '../types';

type PageResponse<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
};

export const getUsers = async (params?: { page?: number; size?: number }) => {
  const res = await api.get<PageResponse<UserDto>>('/users', {
    params: {
      page: params?.page ?? 0,
      size: params?.size ?? 200,
    },
  });
  return res.data.content;
};

