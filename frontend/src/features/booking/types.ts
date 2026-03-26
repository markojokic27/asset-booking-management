export type BookingStatus = 'SUCCESSFUL' | 'PENDING' | 'REJECTED';

export type Booking = {
  id: string;
  userId: string;           // FK na User
  assetId: string;          // FK na Asset
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
