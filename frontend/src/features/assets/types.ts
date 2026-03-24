export type Asset = {
  id: string;

  name: string;
  description?: string;

  code?: string;
  location?: string;

  status: string;  // ili neki enum?

  categoryId: string;

  createdDate: Date;
  lastModifiedDate: Date;
};

export type AssetDto = Asset & {
  categoryName?: string;
};
