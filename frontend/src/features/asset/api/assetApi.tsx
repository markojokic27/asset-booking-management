// Types
import type { AssetDto } from '../types';
// API
import api from '../../../shared/api';

export type PageResponse<T> = {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
}

export const getAllAssets = async (page = 0, size = 200) => {
  const res = await api.get<PageResponse<AssetDto>>(
    '/assets',
    {
      params: { page, size },
    }
  );
  
  return res.data;
};

export const getAssetById = async (id: string) => {
  const res = await api.get<AssetDto>(
    `/assets/${id}`
  );
  return res.data;
}

export type CreateAssetRequest = {
  name: string
  categoryId: number
  status: 'ACTIVE' | 'INACTIVE' | 'DAMAGED' | 'DELETED'
  location?: string
  description?: string
}

export const createAsset = async (data: CreateAssetRequest) => {
  const res = await api.post<AssetDto>(
    '/assets', data
  );
  return res.data;
}

export type UpdateAssetRequest = {
  name: string
  categoryId: number
  status: 'ACTIVE' | 'INACTIVE' | 'DAMAGED' | 'DELETED'
  location?: string
  description?: string
}

export const updateAsset = async (id: number, data: UpdateAssetRequest) => {
  const res = await api.patch<AssetDto>(`/assets/${id}`, data);
  return res.data;
};

export const deleteAsset = async (id: number) => {
  await api.delete<void>(`/assets/${id}`);
}

export const getAssetReport = async (id: number) => {
  const res = await api.get(`/reports/assets/${id}`);
  return res.data;
}