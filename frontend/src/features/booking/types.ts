import type { AssetDto } from '../asset/types';

export const bookingStatuses = [
  'APPROVED',
  'PENDING',
  'REJECTED',
  'COMPLETED',
  'CANCELLED',
] as const;

export type BookingStatus = (typeof bookingStatuses)[number];

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
    managerEmail: string;
  };
  asset: {
    categoryId: number;
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

export type CreateBookingDto = {
  userId: number;
  assetId: number;
  status: string;
  bookingStart: string;
  bookingEnd: string;
  notes?: string;
};

export type TimeSlotDto = {
  bookingStart: string;
  bookingEnd: string;
}

export type CreateRecurringBookingDto = {
  userId: number;
  assetId: number;
  timeSlots: TimeSlotDto[];
  notes?: string;
}
export type BookingUpdateDto = {
  status?: BookingStatus;
  bookingStart?: string;
  bookingEnd?: string;
  notes?: string;
};

export type Filters = {
  search: string;
  fromDate: string;
  toDate: string;
  fromHour: string;
  toHour: string;
  selectedWeekdays: number[];
};

export type BookingsState = {
  selectedCategory: string;
  assets: AssetDto[];
  filters: Filters;
};
