// Types
import type {
  BookingWithRelations,
  CreateBookingDto,
} from '../types';

// API
import api from '../../../shared/api';

export type PageResponse<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
};

export const getAllBookings = async (
  page = 0,
  size = 10,
  sort = 'bookingStart,desc'
) => {
  const res = await api.get<PageResponse<BookingWithRelations>>('/bookings', {
    params: { page, size, sort },
  });
  return res.data;
};

// get all bookings by asset ID
export const getAllAssetBookings = async (
  page = 0,
  size = 10,
  assetId: number
) => {
  const res = await api.get<PageResponse<BookingWithRelations>>('/bookings', {
    params: { page, size, assetId },
  });
  console.log(res);
  return res.data;
};

// get all bookings by user ID
export const getAllUserBookings = async (
  page = 0,
  size = 10,
  userId: number,
  sort = 'bookingStart,desc'
) => {
  const res = await api.get<PageResponse<BookingWithRelations>>('/bookings', {
    params: { page, size, userId, sort },
  });
  return res.data;
};

// get all pending bookings (for manager approvals)
export const getPendingBookings = async (
  page = 0,
  size = 100,
  sort = 'bookingStart,asc'
) => {
  const res = await api.get<PageResponse<BookingWithRelations>>('/bookings', {
    params: { page, size, status: 'PENDING', sort },
  });
  return res.data;
};

// get all bookings by category ID
export const getAllCategoryBookings = async (
  page = 0,
  size = 100,
  categoryId: number
) => {
  const res = await api.get<PageResponse<BookingWithRelations>>('/bookings', {
    params: { page, size, categoryId },
  });
  console.log(res);
  return res.data;
};

// create booking
export const createBooking = async (bookingData: CreateBookingDto) => {
  const res = await api.post<CreateBookingDto>('/bookings', bookingData);
  return res.data;
};

// approve booking
export const approveBooking = async (bookingId: number) => {
  const res = await api.post<BookingWithRelations>(
    `/bookings/${bookingId}/approve`
  );
  return res.data;
};

// reject booking
export const rejectBooking = async (bookingId: number) => {
  const res = await api.post<BookingWithRelations>(
    `/bookings/${bookingId}/reject`
  );
  return res.data;
};
