import * as React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { ParkingMap } from '../../pages/ParkingMap';

// ── Mocks ────────────────────────────────────────────────────────────────────

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick }: { children: React.ReactNode; onClick?: () => void }) => (
    <button onClick={onClick}>{children}</button>
  ),
}));
vi.mock('../../assets/Floor-1', () => ({ FloorMinus1: () => <div>floor-1</div> }));
vi.mock('../../assets/Floor-2', () => ({ FloorMinus2: () => <div>floor-2</div> }));

// ── Default props ──────────────────────────────────────────────────────────────
// ParkingMap now requires bookings, assets, refetchBookings and setFilters.
// Empty arrays satisfy the typed array props; vi.fn() covers the callbacks.
// If TS still complains about the callback signatures, cast with
//   refetchBookings: vi.fn() as unknown as <ExactType>,
// using the real types from ParkingMap's Props.
const defaultProps = {
  bookings: [],
  assets: [],
  refetchBookings: vi.fn(),
  setFilters: vi.fn(),
};

const renderMap = (overrides: Partial<typeof defaultProps> = {}) =>
  render(<ParkingMap {...defaultProps} {...overrides} />);

// ── Tests ─────────────────────────────────────────────────────────────────────

describe('ParkingMap', () => {
  beforeEach(() => vi.clearAllMocks());

  it('renders open button, modal closed by default', () => {
    renderMap();
    expect(screen.getByText('bookings.viewParkingMap')).toBeInTheDocument();
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('opens modal on button click', () => {
    renderMap();
    fireEvent.click(screen.getByText('bookings.viewParkingMap'));
    expect(screen.getByRole('dialog')).toBeInTheDocument();
  });

  it('shows floor -1 by default', () => {
    renderMap();
    fireEvent.click(screen.getByText('bookings.viewParkingMap'));
    expect(screen.getByText('floor-1')).toBeInTheDocument();
    expect(screen.queryByText('floor-2')).not.toBeInTheDocument();
  });

  it('switches to floor -2 on tab click', () => {
    renderMap();
    fireEvent.click(screen.getByText('bookings.viewParkingMap'));
    fireEvent.click(screen.getByTestId('level-button--2'));
    expect(screen.getByText('floor-2')).toBeInTheDocument();
    expect(screen.queryByText('floor-1')).not.toBeInTheDocument();
  });

  it('closes modal on close button click', () => {
    renderMap();
    fireEvent.click(screen.getByText('bookings.viewParkingMap'));
    fireEvent.click(screen.getByTestId('parking-close-button'));
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('closes modal on Escape key', () => {
    renderMap();
    fireEvent.click(screen.getByText('bookings.viewParkingMap'));
    fireEvent.keyDown(window, { key: 'Escape' });
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('closes modal on backdrop click', () => {
    renderMap();
    fireEvent.click(screen.getByText('bookings.viewParkingMap'));
    fireEvent.click(screen.getByRole('dialog'));
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('sets body overflow hidden when open and restores on close', () => {
    renderMap();
    fireEvent.click(screen.getByText('bookings.viewParkingMap'));
    expect(document.body.style.overflow).toBe('hidden');
    fireEvent.click(screen.getByTestId('parking-close-button'));
    expect(document.body.style.overflow).toBe('');
  });
});