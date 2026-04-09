export type AssetStatus = 'ACTIVE' | 'INACTIVE' | 'DAMAGED';

export const assetStatuses = ['ACTIVE', 'INACTIVE', 'DAMAGED'] as const;

export type Asset = {
  id: string;
  name: string;
  categoryId: number; // FK
  description?: string;
  imageUrl?: string;
  code: string;
  status: AssetStatus;
  location: string;
  createdAt: Date;
  lastModifiedAt: Date;
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

export type AssetCategory = typeof categories[number];
