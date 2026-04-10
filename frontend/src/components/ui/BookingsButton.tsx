import CalendarTodaySharpIcon from '@mui/icons-material/CalendarTodaySharp';
import { Button } from './Button';

export type BookingsButtonProps = {
  onClick?: () => void;
};

export function BookingsButton({ onClick }: BookingsButtonProps) {
  return (
    <Button
      size="sm"
      variant="solid"
      iconLeft={<CalendarTodaySharpIcon fontSize="small" />}
      className="shadow-none"
      onClick={onClick}
    >
      Bookings
    </Button>
  );
}
