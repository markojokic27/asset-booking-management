import * as React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import Bookings from '../../pages/Bookings';

// ── Mocks ────────────────────────────────────────────────────────────────────

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

const { mockUseBookingData } = vi.hoisted(() => ({
  mockUseBookingData: vi.fn(),
}));

vi.mock('../../features/booking/hooks/useBookingData', () => ({
  useBookingData: () => mockUseBookingData(),
}));

vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick }: { children: React.ReactNode; onClick?: () => void }) => (
    <button onClick={onClick}>{children}</button>
  ),
}));
vi.mock('../../features/asset/components/AssetCategoryGrid', () => ({
  AssetCategoryGrid: ({ categories, onSelectCategory }: {
    categories: string[];
    onSelectCategory: (name: string) => void;
  }) => (
    <div>{categories.map((c) => <button key={c} onClick={() => onSelectCategory(c)}>{c}</button>)}</div>
  ),
}));
vi.mock('../../features/booking/components/FilterBar', () => ({
  FiltersBar: () => <div>filters-bar</div>,
}));
vi.mock('../../features/booking/components/BookingTable', () => ({
  BookingTable: () => <div>booking-table</div>,
}));
vi.mock('../../features/booking/components/ParkingMap', () => ({
  ParkingMap: () => <div>parking-map</div>,
}));

// ── Fixtures ─────────────────────────────────────────────────────────────────

const laptops = { id: 1, name: 'Laptops', bookingPeriod: 'DAY' };
const parking = { id: 2, name: 'Parking', bookingPeriod: 'HOUR' };

function setup(selectedCategory: typeof laptops | null = laptops, loading = false) {
  const selectCategoryByName = vi.fn();
  mockUseBookingData.mockReturnValue({
    assets: [],
    categories: [laptops, parking],
    selectedCategory,
    selectCategoryByName,
    loading,
  });
  return { selectCategoryByName };
}

// ── Tests ─────────────────────────────────────────────────────────────────────

describe('Bookings', () => {
  beforeEach(() => vi.clearAllMocks());

  it('renders category names and reset button', () => {
    setup();
    render(<Bookings />);
    expect(screen.getAllByText('Laptops').length).toBeGreaterThan(0);
    expect(screen.getByText('Parking')).toBeInTheDocument();
    expect(screen.getByText('bookings.resetFilters')).toBeInTheDocument();
  });

  it('renders selected category name as heading', () => {
    setup();
    render(<Bookings />);
    expect(screen.getByRole('heading', { name: 'Laptops' })).toBeInTheDocument();
  });

  it('shows loading state', () => {
    setup(laptops, true);
    render(<Bookings />);
    expect(screen.getByText('bookings.loading')).toBeInTheDocument();
    expect(screen.queryByText('booking-table')).not.toBeInTheDocument();
  });

  it('shows booking table when loaded', () => {
    setup();
    render(<Bookings />);
    expect(screen.getByText('booking-table')).toBeInTheDocument();
    expect(screen.queryByText('bookings.loading')).not.toBeInTheDocument();
  });

  it('calls selectCategoryByName on category click', () => {
    const { selectCategoryByName } = setup();
    render(<Bookings />);
    fireEvent.click(screen.getByText('Parking'));
    expect(selectCategoryByName).toHaveBeenCalledWith('Parking');
  });

  it('shows ParkingMap button only for Parking category', () => {
    setup(parking);
    render(<Bookings />);
    expect(screen.getByText('parking-map')).toBeInTheDocument();
  });

  it('hides ParkingMap button for non-Parking category', () => {
    setup(laptops);
    render(<Bookings />);
    expect(screen.queryByText('parking-map')).not.toBeInTheDocument();
  });

  it('does not crash when no category is selected', () => {
    setup(null);
    render(<Bookings />);
    expect(screen.getByText('bookings.resetFilters')).toBeInTheDocument();
  });
});