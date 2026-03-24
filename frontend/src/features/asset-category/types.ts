export type BookingPeriod = 'daily' | 'weekly' | 'monthly';

export type AssetCategory = {
  id: string;
  name: string;
  description?: string;
  booking_period: BookingPeriod;
  approval: boolean;
  createdAt: Date;
  lastModifiedAt: Date;
};

export type AssetCategoryDto = AssetCategory & {};
