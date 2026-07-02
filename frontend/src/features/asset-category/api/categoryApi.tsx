import type { AssetCategoryDto, BookingPeriod } from '../types';
import api from '../../../shared/api';

export type PageResponse<T> = {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
}

export const getAllCategories = async (page = 0, size = 10) => {
  const res = await api.get<PageResponse<AssetCategoryDto>>(
    '/asset-categories',
    {
      params: { page, size },
    }
  );
  return res.data;
};

export const getCategoryById = async (id: number) => {
  const res = await api.get<AssetCategoryDto>(
    `/asset-categories/${id}`
  );
  return res.data;
}

export type CreateCategoryRequest = {
  name: string;
  description: string;
  bookingPeriod: BookingPeriod;
  approval: boolean;
  photo?: string;
};

export const createCategory = async (data: CreateCategoryRequest) => {
  const res = await api.post<AssetCategoryDto>('/asset-categories', data);
  return res.data;
}

export type UpdateCategoryRequest = {
  name: string;
  description?: string;
  bookingPeriod: BookingPeriod;
  approval: boolean;
  photo?: string;
};

export const updateCategory = async (id: number, data: UpdateCategoryRequest) => {
  const res = await api.patch<AssetCategoryDto>(`/asset-categories/${id}`, data);
  return res.data;
}
