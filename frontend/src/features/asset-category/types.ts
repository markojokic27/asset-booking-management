export type BookingPeriod = 'HOUR' | 'DAY' | 'WEEK' | 'MONTH';

export type AssetCategoryDto = {
  id: number;
  name: string;
  description?: string;
  bookingPeriod: BookingPeriod;
  approval: boolean;
  createdAt: Date;
  lastModifiedAt: Date;
};
