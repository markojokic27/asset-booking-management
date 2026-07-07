import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { AssetsTable } from '../../features/asset/components/AssetTable';
import type { AssetDto } from '../../features/asset/types';

// ── Mocks ─────────────────────────────────────────────────────────────────────

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string, opts?: any) => opts?.direction ? `${key}:${opts.direction}` : key }),
}));

vi.mock('../../../components/ui/Table', () => ({
  Table: ({ data, columns, getRowKey, rowClassName }: any) => (
    <table>
      <thead><tr>{columns.map((col: any) => <th key={col.key}>{col.header}</th>)}</tr></thead>
      <tbody>
        {data.map((row: any) => (
          <tr key={getRowKey(row)} className={rowClassName?.(row) ?? ''}>
            {columns.map((col: any) => (
              <td key={col.key}>{col.render ? col.render(row) : row[col.accessor]}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  ),
}));

vi.mock('../../../components/ui/IconButton', () => ({
  IconButton: ({ children, onClick, 'aria-label': label, disabled, ...props }: any) => (
    <button aria-label={label} onClick={onClick} disabled={disabled} {...props}>{children}</button>
  ),
}));

vi.mock('../../../components/ui/Button', () => ({
  Button: ({ children, onClick }: any) => <button onClick={onClick}>{children}</button>,
}));

vi.mock('@mui/icons-material/VisibilityOutlined', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/EditOutlined', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/DeleteOutline', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/CalendarTodaySharp', () => ({ default: () => <svg /> }));
vi.mock('@mui/icons-material/BarChart', () => ({ default: () => <svg /> }));

// ── Fixtures ──────────────────────────────────────────────────────────────────

const activeAsset: AssetDto = {
  id: 1,
  name: 'Hp 15',
  description: 'Laptop located in room 301',
  code: 'QR-LAPTOP-001',
  status: 'ACTIVE',
  categoryId: 1,
  categoryName: 'Electronics',
  location: 'Room 301',
  createdAt: new Date('2024-01-01'),
  lastModifiedAt: new Date('2024-01-01'),
};

const baseProps = {
  assets: [activeAsset],
  categoryMap: { 1: 'Electronics' },
  nameSortDir: 'asc' as const,
  onToggleNameSort: vi.fn(),
  onView: vi.fn(),
  onEdit: vi.fn(),
  onDelete: vi.fn(),
  onBookings: vi.fn(),
  onReport: vi.fn(),
};

const renderTable = (overrides = {}) => render(<AssetsTable {...baseProps} {...overrides} />);

// ── Tests ─────────────────────────────────────────────────────────────────────

describe('AssetsTable', () => {
  beforeEach(() => vi.clearAllMocks());

  it('renders asset name, status and category', () => {
    renderTable();
    expect(screen.getByText(activeAsset.name)).toBeInTheDocument();
    expect(screen.getByText(`assets.status.${activeAsset.status}`)).toBeInTheDocument();
    expect(screen.getByText(activeAsset.categoryName!)).toBeInTheDocument();
  });

  it.each([
    [{ categoryName: null },              'Electronics'],
    [{ categoryName: null, categoryId: 99 }, '-'],
  ])('category fallback: %o → "%s"', (assetOverride, expected) => {
    renderTable({ assets: [{ ...activeAsset, ...assetOverride }] });
    expect(screen.getByText(expected)).toBeInTheDocument();
  });

  it('calls onToggleNameSort when sort button is clicked', async () => {
    renderTable();
    await userEvent.click(screen.getByRole('button', { name: /byNameAria/i }));
    expect(baseProps.onToggleNameSort).toHaveBeenCalledTimes(1);
  });

  it.each([
    ['onView', 'assets.table.ariaView'],
    ['onEdit', 'assets.table.ariaEdit'],
  ])('calls %s when its button is clicked', async (handler, ariaLabel) => {
    renderTable();
    await userEvent.click(screen.getByRole('button', { name: ariaLabel }));
    expect(baseProps[handler as keyof typeof baseProps]).toHaveBeenCalledWith(activeAsset);
  });

  it('calls onDelete when delete button is clicked', async () => {
    renderTable();
    await userEvent.click(screen.getByRole('button', { name: 'assets.table.ariaDelete' }));
    expect(baseProps.onDelete).toHaveBeenCalledWith(activeAsset);
  });

  it('calls onBookings when bookings button is clicked', async () => {
    renderTable();
    await userEvent.click(screen.getAllByText('assets.table.bookings').find(el => el.tagName === 'BUTTON')!);
    expect(baseProps.onBookings).toHaveBeenCalledWith(activeAsset);
  });

  it('disables edit and delete buttons for deleted asset', () => {
    renderTable({ assets: [{ ...activeAsset, status: 'DELETED' }] });
    expect(screen.getByRole('button', { name: 'assets.table.ariaEdit' })).toBeDisabled();
    expect(screen.getByRole('button', { name: 'assets.table.ariaDelete' })).toBeDisabled();
  });
});