export type AssetStatus = 'ACTIVE' | 'INACTIVE' | 'DAMAGED' | 'DELETED';

export const ALL_ASSETS_CATEGORY = 'Assets' as const;

export const assetStatuses = [
  'ACTIVE',
  'INACTIVE',
  'DAMAGED',
  'DELETED',
] as const;

export type Asset = {
  id: number;
  name: string;
  categoryId: number; // FK
  description?: string;
  code?: string;
  status: AssetStatus;
  location: string;
  createdAt?: Date;
  lastModifiedAt?: Date;
};

export type AssetDto = Asset & {
  categoryName?: string; // opcionalno za UI prikaz
};

export const categories = [
  'Laptops',
  'Parking',
  'Desks',
  'Books',
  'Meeting room',
  'IT equipment',
] as const;

export type AssetCategory = (typeof categories)[number];
