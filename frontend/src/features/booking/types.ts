import type { AssetDto } from '../asset/types';

// TODO fix this, changes in Database are needed
export type BookingStatus =
  | 'APPROVED'
  | 'PENDING'
  | 'REJECTED'
  | 'ACTIVE'
  | 'COMPLETED'
  | 'CANCELLED';

export type Booking = {
  id: string;
  userId: number; // FK na User
  assetId: number; // FK na Asset
  bookingStart: Date;
  bookingEnd: Date;
  status: BookingStatus;
  notes?: string;
  createdAt: Date;
  lastModifiedAt: Date;
};

export type BookingDto = Booking & {
  userName?: string;
  assetName?: string;
  assetCategory?: string;
};

export type BookingWithRelations = BookingDto & {
  user: {
    id: number;
    name: string;
    surname: string;
    email: string;
    role: string;
  };
  asset: {
    id: number;
    name: string;
    category: {
      id: number;
      name: string;
      bookingPeriod: string;
      approval: boolean;
    };
    status: string;
    description: string;
    location: string;
  };
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
