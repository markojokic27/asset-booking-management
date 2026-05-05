import api from '../../../shared/api';
import type { UserDto, UserUpsertRequest } from '../types';

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

export const getUserById = async (id: string | number) => {
  const res = await api.get<UserDto>(`/users/${id}`);
  return res.data;
};

export const updateUser = async (id: string | number, payload: UserUpsertRequest) => {
  const res = await api.put<UserDto>(`/users/${id}`, payload);
  return res.data;
};

export const createUser = async (payload: UserUpsertRequest) => {
  const res = await api.post<UserDto>('/users', payload);
  return res.data;
};

export const deleteUser = async (id: number) => {
  await api.delete<void>(`/users/${id}`);
};
