import { render, screen, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { BookingWithRelations } from '../../features/booking/types';
import type { AssetDto } from '../../features/asset/types';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string, opts?: any) => opts ? `${key}:${JSON.stringify(opts)}` : key }),
}));
vi.mock('../../config/i18n', () => ({
  default: { language: 'en' },
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick, disabled, ...props }: any) => (
    <button onClick={onClick} disabled={disabled} {...props}>{children}</button>
  ),
}));
vi.mock('../../assets/Floor-1', () => ({
  FloorMinus1: ({ takenSpots, onSpotClick }: any) => (
    <div>
      <span>floor-1</span>
      <button onClick={() => onSpotClick(5)}>spot-5</button>
      <button onClick={() => onSpotClick(99)}>spot-99</button>
      {takenSpots.includes(5) && <span>spot-5-taken</span>}
    </div>
  ),
}));
vi.mock('../../assets/Floor-2', () => ({
  FloorMinus2: ({ onSpotClick }: any) => (
    <div>
      <span>floor-2</span>
      <button onClick={() => onSpotClick(10)}>spot-10</button>
    </div>
  ),
}));
vi.mock('../../features/booking/hooks/useCreateBooking', () => ({
  useCreateBooking: vi.fn(() => ({ isCreating: false, handleCreateBooking: vi.fn() })),
}));

import { ParkingMap } from '../../features/booking/components/ParkingMap';
import { useCreateBooking } from '../../features/booking/hooks/useCreateBooking';


const defaultFilters = { search: '', fromDate: '', toDate: '', fromHour: '', toHour: '', selectedWeekdays: [] };
const dateFilters = { ...defaultFilters, fromDate: '2025-06-01', toDate: '2025-06-01' };

const defaultProps = {
  bookings: [] as BookingWithRelations[],
  assets: [] as AssetDto[],
  filters: defaultFilters,
  refetchBookings: vi.fn().mockResolvedValue(undefined),
  setFilters: vi.fn(),
};

const withDate = { ...defaultProps, filters: dateFilters };

const approvedBooking: BookingWithRelations = {
  id: '1',
  userId: 1,
  assetId: 5,
  status: 'APPROVED',
  notes: '',
  createdAt: new Date(),
  lastModifiedAt: new Date(),
  bookingStart: new Date('2025-06-01T00:00:00'),
  bookingEnd: new Date('2025-06-01T23:59:59'),
  userName: 'Alice',
  assetName: 'Parking Spot 5',
  assetCategory: 'Parking',
  user: {
    id: 1,
    name: 'Alice',
    surname: 'Smith',
    email: '',
    role: 'EMPLOYEE',
    managerEmail: '',
  },
  asset: {
    id: 5,
    name: 'Parking Spot 5',
    categoryId: 1,
    status: 'ACTIVE',
    description: '',
    location: '',
    category: { id: 1, name: 'Parking', bookingPeriod: 'DAY', approval: false },
  },
};

const parkingAsset: AssetDto = {
  id: 5,
  name: 'Parking Spot 5',
  categoryId: 1,
  status: 'ACTIVE',
  description: '',
  location: '',
  createdAt: new Date(),
  lastModifiedAt: new Date(),
};

const renderMap = (props = defaultProps) =>
  render(
    <MemoryRouter>
      <ParkingMap {...props} />
    </MemoryRouter>
  );
const openModal = () => fireEvent.click(screen.getByText('bookings.viewParkingMap'));
const cancelDialog = (dialog: HTMLElement) =>
  fireEvent(dialog, new Event('cancel', { bubbles: true, cancelable: true }));

// Tests 

describe('ParkingMap', () => {
  beforeEach(() => vi.clearAllMocks());

  it('renders open button with modal closed by default', () => {
    renderMap();
    expect(screen.getByText('bookings.viewParkingMap')).toBeInTheDocument();
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('opens modal and shows floor -1 by default', () => {
    renderMap();
    openModal();
    expect(screen.getByRole('dialog')).toBeInTheDocument();
    expect(screen.getByText('floor-1')).toBeInTheDocument();
    expect(screen.queryByText('floor-2')).not.toBeInTheDocument();
  });

  it('switches to floor -2 on tab click', () => {
    renderMap();
    openModal();
    fireEvent.click(screen.getByTestId('level-button--2'));
    expect(screen.getByText('floor-2')).toBeInTheDocument();
    expect(screen.queryByText('floor-1')).not.toBeInTheDocument();
  });

  it.each([
    ['close button',   () => fireEvent.click(screen.getByTestId('parking-close-button'))],
    ['cancel event',   () => cancelDialog(screen.getByRole('dialog'))],
    ['backdrop click', () => fireEvent.click(screen.getByLabelText('ui.modal.closeAria'))],
  ])('closes modal on %s', (_, trigger) => {
    renderMap();
    openModal();
    trigger();
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('does not change body overflow when modal opens and closes', () => {
    renderMap();
    openModal();
    expect(document.body.style.overflow).toBe('');
    fireEvent.click(screen.getByTestId('parking-close-button'));
    expect(document.body.style.overflow).toBe('');
  });

  describe('date controls', () => {
    it('shows today label when no date selected', () => {
      renderMap();
      openModal();
      expect(screen.getByText(/bookings\.parkingMap\.today/)).toBeInTheDocument();
    });

    it('shows formatted date when date is selected', () => {
      renderMap(withDate);
      openModal();
      expect(screen.getByText('1.6.2025.')).toBeInTheDocument();
    });

    it('calls setFilters with date on date input change', () => {
      renderMap();
      openModal();
      fireEvent.change(screen.getByDisplayValue(''), { target: { value: '2025-06-01' } });
      expect(defaultProps.setFilters).toHaveBeenCalled();
    });

    it('calls setFilters when date input is cleared', () => {
      const setFilters = vi.fn();
      renderMap({ ...withDate, setFilters });
      openModal();
      fireEvent.change(screen.getByDisplayValue('2025-06-01'), { target: { value: '' } });
      expect(setFilters).toHaveBeenCalled();
    });
  });

  describe('spot popover', () => {
    it('opens popover on spot click', () => {
      renderMap(withDate);
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      expect(screen.getByText(/bookings\.parkingMap\.spotNumber/)).toBeInTheDocument();
    });

    it('closes popover on close button when spot is selected, keeps modal open', () => {
      renderMap(withDate);
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      fireEvent.click(screen.getByTestId('spot-popover-close-button'));
      expect(screen.queryByText(/bookings\.parkingMap\.spotNumber/)).not.toBeInTheDocument();
      expect(screen.getByRole('dialog')).toBeInTheDocument();
    });

    it('shows available status when spot is not taken', () => {
      renderMap(withDate);
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      expect(screen.getByText('bookings.parkingMap.available')).toBeInTheDocument();
    });

    it('shows taken status when spot is booked', () => {
      renderMap({ ...withDate, bookings: [approvedBooking] });
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      expect(screen.getByText('bookings.parkingMap.taken')).toBeInTheDocument();
    });

    it('disables book button when spot has no linked asset', () => {
      renderMap();
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      expect(screen.getByText(/bookings\.confirmation\.singleDay/)).toBeInTheDocument();
      expect(screen.getByTestId('spot-book-button')).toBeDisabled();
    });

    it('shows notes input when spot available and date selected', () => {
      renderMap({ ...withDate, assets: [parkingAsset] });
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      expect(screen.getByPlaceholderText('bookings.parkingMap.notesPlaceholder')).toBeInTheDocument();
    });

    it('calls handleCreateBooking on book click', async () => {
      const mockHandleCreateBooking = vi.fn().mockResolvedValue(undefined);
      vi.mocked(useCreateBooking).mockReturnValue({ isCreating: false, handleCreateBooking: mockHandleCreateBooking });
      renderMap({ ...withDate, assets: [parkingAsset] });
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      fireEvent.click(screen.getByText('bookings.table.book'));
      expect(mockHandleCreateBooking).toHaveBeenCalled();
    });

    it('passes correct assetId to useCreateBooking for known spot', () => {
      renderMap({ ...withDate, assets: [parkingAsset] });
      openModal();
      fireEvent.click(screen.getByText('spot-5'));
      expect(vi.mocked(useCreateBooking)).toHaveBeenCalledWith(
        expect.objectContaining({ assetId: 5 })
      );
    });

    it('passes assetId 0 for unknown spot', () => {
      renderMap(withDate);
      openModal();
      fireEvent.click(screen.getByText('spot-99'));
      expect(vi.mocked(useCreateBooking)).toHaveBeenCalledWith(
        expect.objectContaining({ assetId: 0 })
      );
    });
  });

  describe('getTakenSpots logic', () => {
    it('marks spot as taken for APPROVED booking on matching date', () => {
      renderMap({ ...withDate, bookings: [approvedBooking] });
      openModal();
      expect(screen.getByText('spot-5-taken')).toBeInTheDocument();
    });

    it('does not mark spot as taken for PENDING booking', () => {
      renderMap({ ...withDate, bookings: [{ ...approvedBooking, status: 'PENDING' }] });
      openModal();
      expect(screen.queryByText('spot-5-taken')).not.toBeInTheDocument();
    });

    it('ignores bookings whose asset name does not match parking spot pattern', () => {
      const nonParkingBooking = {
        ...approvedBooking,
        assetName: 'Desk 5',
        asset: { ...approvedBooking.asset, name: 'Desk 5' },
      };
      renderMap({ ...withDate, bookings: [nonParkingBooking] });
      openModal();
      expect(screen.queryByText('spot-5-taken')).not.toBeInTheDocument();
    });
  });

  it('closes spot popover on backdrop click', () => {
    renderMap(withDate);
    openModal();
    fireEvent.click(screen.getByText('spot-5'));
    expect(screen.getByText(/bookings\.parkingMap\.spotNumber/)).toBeInTheDocument();
    fireEvent.click(screen.getAllByLabelText('ui.modal.closeAria')[1]);
    expect(screen.queryByText(/bookings\.parkingMap\.spotNumber/)).not.toBeInTheDocument();
    expect(screen.getByRole('dialog')).toBeInTheDocument();
  });
});