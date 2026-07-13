export const getBookingLimit = (
  role: string | undefined,
  categoryName: string
) => {
  const today = new Date();

  if (role === 'EMPLOYEE' && categoryName === 'Parking') {
    const end = new Date(today);
    end.setDate(end.getDate() + 14);

    const dayOfWeek = end.getDay();

    if (dayOfWeek !== 0) {
      end.setDate(end.getDate() + (7 - dayOfWeek));
    }

    return end;
  }

  return new Date(today.getFullYear(), today.getMonth() + 5, 0);
};
