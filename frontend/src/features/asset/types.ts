export type AssetStatus = 'AVAILABLE' | 'UNAVAILABLE';

export type Asset = {
  id: string;
  name: string;
  categoryId: number; // FK
  description?: string;
  code: string;
  status: AssetStatus;
  location: string;
  createdAt: Date;
  lastModifiedAt: Date;
};

export type AssetDto = Asset & {
  categoryName?: string; // opcionalno za UI prikaz
};
