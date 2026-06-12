import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi, describe, it, expect } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: React.HTMLAttributes<HTMLDivElement>) => <div>{children}</div>,
}));
vi.mock('../../features/report/components/FilterBar', () => ({
  default: ({ filters, onReset }: any) => (
    <div>
      <span>FiltersBar</span>
      <span>from:{filters.fromDate}</span>
      <button onClick={onReset}>Reset</button>
    </div>
  ),
}));
vi.mock('../../features/report/components/BookingStatusPie', () => ({ default: () => <div>BookingStatusPie</div> }));
vi.mock('../../features/report/components/BookingStatusBar', () => ({ default: () => <div>BookingStatusBar</div> }));
vi.mock('../../features/report/components/TopUserBookingsPie', () => ({ default: () => <div>TopUserBookings</div> }));
vi.mock('../../features/report/components/TopAssetBookingsPie', () => ({ default: () => <div>TopAssetBookings</div> }));

import Report from '../../pages/Report';

const renderPage = () => render(<MemoryRouter><Report /></MemoryRouter>);

describe('Report', () => {
  it('renders title, filters and all charts', () => {
    renderPage();
    expect(screen.getByText('report.title')).toBeInTheDocument();
    expect(screen.getByText('FiltersBar')).toBeInTheDocument();
    expect(screen.getByText('from:')).toBeInTheDocument();
    for (const chart of ['BookingStatusPie', 'BookingStatusBar', 'TopUserBookings', 'TopAssetBookings']) {
      expect(screen.getByText(chart)).toBeInTheDocument();
    }
  });

  it('resets filters on reset click', async () => {
    renderPage();
    await userEvent.setup().click(screen.getByText('Reset'));
    expect(screen.getByText('from:')).toBeInTheDocument();
  });
});