// Shared Axios instance used for all API requests
import api from '../../../shared/api';

// Type definitions for users and request payloads
import type { UserDto, UserUpdateRequest, UserUpsertRequest } from '../types';

// Generic type representing paginated backend response
type PageResponse<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
};

export type ChangePasswordRequest = {
  currentPassword: string;
  newPassword: string;
};

// Fetch paginated list of users
export const getUsers = async (params?: { page?: number; size?: number }) => {
  const res = await api.get<PageResponse<UserDto>>('/users', {
    params: {
      page: params?.page ?? 0,
      size: params?.size ?? 200,
    },
  });
  return res.data.content;
};

// Fetch single user by ID
export const getUserById = async (id: string | number) => {
  const res = await api.get<UserDto>(`/users/${id}`);
  return res.data;
};

// Update existing user 
export const updateUser = async (id: string | number, payload: UserUpdateRequest) => {
  const res = await api.patch<UserDto>(`/users/${id}`, payload);
  return res.data;
};

// Create new user
export const createUser = async (payload: UserUpsertRequest) => {
  const res = await api.post<UserDto>('/users', payload);
  return res.data;
};

// Delete user by ID
export const deleteUser = async (id: number) => {
  await api.delete<void>(`/users/${id}`);
};

// Change own password
export const changeOwnPassword = async (id: string | number, payload: ChangePasswordRequest) => {
  await api.patch<void>(`/users/${id}/password`, payload);
};

// Fetch report data for specific user
export const getUserReport = async (id: number) => {
  const res = await api.get(`/reports/users/${id}`);
  return res.data;
};
