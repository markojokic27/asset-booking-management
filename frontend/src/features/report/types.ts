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
  fullName: string;
  bookingCount: number;
}

export type MonthlyBookingCountDTO = {
  year: number;
  month: number;
  totalBookingCount: number;

  totalApprovedCount: number;
  totalCancelledCount: number;
  totalPendingCount: number;
  totalRejectedCount: number;
  totalCompletedCount: number;
}

export type GeneralReportResponseDTO = {
  totalBookingCount: number;

  totalApprovedCount: number;
  totalCancelledCount: number;
  totalPendingCount: number;
  totalRejectedCount: number;
  totalCompletedCount: number;

  topUsers: TopUserBookingCountDTO[];
  topAssets: TopAssetBookingCountDTO[];
  monthlyStats: MonthlyBookingCountDTO[];
}