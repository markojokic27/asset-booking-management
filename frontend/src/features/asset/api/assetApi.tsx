import type { AssetDto } from '../types';
import api from '../../../shared/api';

export type PageResponse<T> = {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
}

export const getAllAssets = async (page = 0, size = 10) => {
  const res = await api.get<PageResponse<AssetDto>>(
    '/assets',
    {
      params: { page, size },
    }
  );
  console.log("res", res);
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
  const res = await api.put<AssetDto>(
    `/assets/${id}`, data
  );
  return res.data;
}