export type Filter = {
  fromDate: string;
  toDate: string;
  userId: number | null;
  assetId: number | null;
};

export type TopUserBookingCountDTO = {
  userId: number;
  fullName: string;
  bookingCount: number;
}

export type TopAssetBookingCountDTO = {
  assetId: number;
  name: string;
  bookingCount: number;
}

export type MonthlyBookingCountDTO = {
  year: number;
  month: number;
  totalBookingsCount: number;

  totalApprovedBookingCount: number;
  totalCancelledBookingCount: number;
  totalPendingBookingCount: number;
  totalRejectedBookingCount: number;
  totalCompletedBookingCount: number;
}

export type GeneralReportResponseDTO = {
  totalBookingsCount: number;

  totalApprovedBookingCount: number;
  totalCancelledBookingCount: number;
  totalPendingBookingCount: number;
  totalRejectedBookingCount: number;
  totalCompletedBookingCount: number;

  topUsers: TopUserBookingCountDTO[];
  topAssets: TopAssetBookingCountDTO[];
  monthlyStats: MonthlyBookingCountDTO[];
}