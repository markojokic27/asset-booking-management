// Components
import { Button } from '../../../components/ui/Button';
import { Modal } from '../../../components/ui/Modal';

// Types
import type { Filters } from '../types';
import type { AssetDto } from '../../asset/types';
import type { UserDto } from '../../user/types';

// Utilis
import { getBookingMessage } from '../utils/getBookingMessage';
import { useTranslation } from 'react-i18next';

type BookingModalProps = {
  open: boolean;
  onClose: () => void;
  asset?: AssetDto | null;
  filters: Filters;
  user: UserDto | null;
  needApproval: boolean;
  availableRecurringDates: string[];
  variant: string;
  handleCreateBooking: () => Promise<boolean>;
};

export function BookingModal({
  open,
  onClose,
  asset,
  filters,
  user,
  needApproval,
  availableRecurringDates,
  variant,
  handleCreateBooking,
}: BookingModalProps) {
  const { t, i18n } = useTranslation();

  if (!open || !asset) return null;

  return (
    <Modal
      isOpen={true}
      onClose={onClose}
      title={
        <h2 className="text-xl font-semibold">
          {t('bookings.buttons.book')} {asset.name}
        </h2>
      }
    >
      <div className="text-lg">
        {getBookingMessage({
          filters,
          availableRecurringDates,
          needApproval,
          user,
          variant,
          t,
          language: i18n.language,
        })}
      </div>
      <div className="mt-10 flex justify-end gap-4">
        <Button
          data-testid="cancel-button"
          variant="outline"
          size="md"
          onClick={onClose}
        >
          {t('bookings.buttons.cancel')}
        </Button>

        <Button
          data-testid="book-now-button"
          variant="solid"
          size="md"
          onClick={() => {
            handleCreateBooking().then(() => {
              onClose();
            });
          }}
        >
          {needApproval && user?.role === 'EMPLOYEE'
            ? t('bookings.buttons.sendRequest')
            : t('bookings.buttons.bookNow')}
        </Button>
      </div>
    </Modal>
  );
}
