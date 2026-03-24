export type BookingStatus =
  | "PENDING"
  | "APPROVED"
  | "REJECTED"
  | "CANCELLED"
  | "ACTIVE";
  //| "ENDED";

export type Booking = {
  id: string;

  userId: string;
  assetId: string;

  timestampStart: Date;
  timestampEnd: Date;

  status: BookingStatus;

  notes?: string;

  createdDate: Date;
  lastModifiedDate: Date;
};

export type BookingDto = Booking & {
  userName?: string;
  assetName?: string;
  assetCategory?:string;
};