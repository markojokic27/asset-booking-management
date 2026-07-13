export const getDatesForWeekdays = (month: Date, weekdays: number[]) => {
  const result: string[] = [];

  const year = month.getFullYear();
  const monthIndex = month.getMonth();

  const current = new Date(year, monthIndex, 1);

  while (current.getMonth() === monthIndex) {
    const day = current.getDay();

    const normalized = day === 0 ? 7 : day;

    if (weekdays.includes(normalized)) {
      result.push(current.toLocaleDateString('sv-SE'));
    }

    current.setDate(current.getDate() + 1);
  }

  return result;
};
