// Types 
import type { BookingDto } from "../types"

// API
import api from "../../../shared/api"

export type PageResponse<T> = {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
}

export const getAllBookings = async (page = 0, size = 10) => {
  const res = await api.get<PageResponse<BookingDto>>(
    '/bookings',
    {
      params: { page, size },
    }
  );
  console.log("res", res);
  return res.data;
};

// get all bookings by asset ID
export const getAllAssetBookings = async (page= 0, size = 10, assetId: number) => {
    const res = await api.get<PageResponse<BookingDto>>(
        '/bookings',
        {
            params: {page, size, assetId}
        }
    );
    console.log(res);
    return res.data;
}