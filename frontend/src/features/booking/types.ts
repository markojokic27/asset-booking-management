import type { AssetDto } from '../asset/types';

export type BookingStatus = 'SUCCESSFUL' | 'PENDING' | 'REJECTED';

export type Booking = {
  id: string;
  userId: number; // FK na User
  assetId: number; // FK na Asset
  bookingStart: Date;
  bookingEnd: Date;
  status: BookingStatus;
  note?: string;
  createdAt: Date;
  lastModifiedAt: Date;
};

export type BookingDto = Booking & {
  userName?: string;
  assetName?: string;
  assetCategory?: string;
};

export type Filters = {
  search: string;
  fromDate: string;
  toDate: string;
  fromHour: string;
  toHour: string;
};

export type BookingsState = {
  selectedCategory: string;
  assets: AssetDto[];
  filters: Filters;
};
