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
