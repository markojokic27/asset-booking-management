type Props = {
  selectedDays: number[];
  onChange: (days: number[]) => void;
};

export function RecurringDaysSelector({ selectedDays, onChange }: Props) {
  const days = [
    { value: 1, label: 'Ponedjeljak' },
    { value: 2, label: 'Utorak' },
    { value: 3, label: 'Srijeda' },
    { value: 4, label: 'Četvrtak' },
    { value: 5, label: 'Petak' },
  ];

  const toggleDay = (day: number) => {
    if (selectedDays.includes(day)) {
      onChange(selectedDays.filter((d) => d !== day));
    } else {
      onChange([...selectedDays, day]);
    }
  };

  return (
    <div className="mb-6">
      <p className="mb-2 font-medium">Rezerviraj svaki:</p>

      <div className="flex flex-wrap gap-4">
        {days.map((day) => (
          <label
            key={day.value}
            className="flex items-center gap-2 hover:cursor-pointer"
          >
            <input
              type="checkbox"
              className="h-5 w-5 rounded border-gray-300 text-blue-600 hover:cursor-pointer focus:ring-blue-500"
              checked={selectedDays.includes(day.value)}
              onChange={() => toggleDay(day.value)}
            />

            {day.label}
          </label>
        ))}
      </div>
    </div>
  );
}
