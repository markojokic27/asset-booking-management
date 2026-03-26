export type BookingPeriod = 'HOUR' | 'DAY' | 'WEEK' | 'MONTH';

export type AssetCategory = {
  id: string;
  name: string;
  description?: string;
  bookingPeriod: BookingPeriod;
  approval: boolean;
  createdAt: Date;
  lastModifiedAt: Date;
};

export type AssetCategoryDto = AssetCategory & {
  // dodati dodatna polja za prikaz u UI ako zatreba
};
