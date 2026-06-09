import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import Assets from '../../pages/Assets';

// ── Mocks ────────────────────────────────────────────────────────────────────

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string, opts?: Record<string, unknown>) => opts?.name ? `${key}:${opts.name}` : key }),
}));

vi.mock('@mui/icons-material/Add', () => ({ default: () => null }));

const { mockUseCurrentUser } = vi.hoisted(() => ({
  mockUseCurrentUser: vi.fn(),
}));
vi.mock('../../features/user/hooks/useCurrentUser', () => ({
  useCurrentUser: () => mockUseCurrentUser(),
}));

const { mockGetAllAssets, mockGetAllCategories, mockDeleteAsset, mockCreateAsset, mockUpdateAsset } = vi.hoisted(() => ({
  mockGetAllAssets: vi.fn(),
  mockGetAllCategories: vi.fn(),
  mockDeleteAsset: vi.fn(),
  mockCreateAsset: vi.fn(),
  mockUpdateAsset: vi.fn(),
}));
vi.mock('../../features/asset/api/assetApi', () => ({
  getAllAssets: () => mockGetAllAssets(),
  deleteAsset: (id: number) => mockDeleteAsset(id),
  createAsset: (payload: unknown) => mockCreateAsset(payload),
  updateAsset: (id: number, payload: unknown) => mockUpdateAsset(id, payload),
}));
vi.mock('../../features/asset-category/api/categoryApi', () => ({
  getAllCategories: () => mockGetAllCategories(),
}));

vi.mock('../../features/user/utilis/users', () => ({
  isAdmin: vi.fn(),
}));
import { isAdmin } from '../../features/user/utilis/users';
const mockIsAdmin = vi.mocked(isAdmin);

vi.mock('../../features/user/hooks/usePagination', () => ({
  usePagination: (items: unknown[]) => ({
    paged: items,
    page: 1,
    totalPages: 1,
    items: items.length,
    setPage: vi.fn(),
  }),
}));

vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick }: { children: React.ReactNode; onClick?: () => void }) => (
    <button onClick={onClick}>{children}</button>
  ),
}));
vi.mock('../../components/ui/SearchBar', () => ({
  SearchInput: ({ value, onChange }: { value: string; onChange: (v: string) => void }) => (
    <input aria-label="search" value={value} onChange={(e) => onChange(e.target.value)} />
  ),
}));
vi.mock('../../components/ui/Pagination', () => ({
  Pagination: () => <div>Pagination</div>,
}));
vi.mock('../../components/ui/DeleteModal', () => ({
  DeleteModal: ({ isOpen, onClose, onConfirm }: { isOpen: boolean; onClose: () => void; onConfirm: () => void }) =>
    isOpen ? (
      <div>
        <span>delete-modal</span>
        <button onClick={onConfirm}>confirm-delete</button>
        <button onClick={onClose}>cancel-delete</button>
      </div>
    ) : null,
}));
vi.mock('../../features/asset/components/AssetCategoryGrid', () => ({
  AssetCategoryGrid: ({ categories, onSelectCategory }: { categories: string[]; onSelectCategory: (c: string) => void }) => (
    <div>
      {categories.map((c) => (
        <button key={c} onClick={() => onSelectCategory(c)}>{c}</button>
      ))}
    </div>
  ),
}));
vi.mock('../../features/asset/components/AssetTable', () => ({
  AssetsTable: ({ assets, onView, onEdit, onDelete, onBookings, onReport }: {
    assets: { id: number; name: string }[];
    onView: (a: { id: number; name: string }) => void;
    onEdit?: (a: { id: number; name: string }) => void;
    onDelete?: (a: { id: number; name: string }) => void;
    onBookings: (a: { id: number; name: string }) => void;
    onReport: (a: { id: number; name: string }) => void;
  }) => (
    <div>
      {assets.map((a) => (
        <div key={a.id}>
          <span>{a.name}</span>
          <button onClick={() => onView(a)}>view-{a.id}</button>
          {onEdit && <button onClick={() => onEdit(a)}>edit-{a.id}</button>}
          {onDelete && <button onClick={() => onDelete(a)}>delete-{a.id}</button>}
          <button onClick={() => onBookings(a)}>bookings-{a.id}</button>
          <button onClick={() => onReport(a)}>report-{a.id}</button>
        </div>
      ))}
    </div>
  ),
}));
vi.mock('../../features/asset/components/AssetModal', () => ({
  AssetModal: ({ isOpen }: { isOpen: boolean }) =>
    isOpen ? <div>asset-modal</div> : null,
}));
vi.mock('../../features/asset/components/AssetBookingsModal', () => ({
  AssetBookingsModal: ({ isOpen }: { isOpen: boolean }) =>
    isOpen ? <div>bookings-modal</div> : null,
}));
vi.mock('../../features/asset/components/AssetReportModal', () => ({
  AssetReportModal: ({ isOpen }: { isOpen: boolean }) =>
    isOpen ? <div>report-modal</div> : null,
}));
vi.mock('../../features/asset/components/AssetFormModal', () => ({
  AssetFormModal: ({ isOpen }: { isOpen: boolean }) =>
    isOpen ? <div>form-modal</div> : null,
}));
vi.mock('../../features/user/components/ShowDeletedFilter', () => ({
  ShowDeletedFilter: ({ onToggle }: { onToggle: () => void }) => (
    <button onClick={onToggle}>toggle-deleted</button>
  ),
}));


const mockCategory = { id: 1, name: 'Laptops' };
const mockAsset = { id: 10, name: 'MacBook', categoryId: 1, status: 'ACTIVE' };

function setupMocks({ admin = false } = {}) {
  mockUseCurrentUser.mockReturnValue({ user: { id: 1 } });
  mockIsAdmin.mockReturnValue(admin);
  mockGetAllCategories.mockResolvedValue({ content: [mockCategory] });
  mockGetAllAssets.mockResolvedValue({ content: [mockAsset] });
}

// ── Tests ─────────────────────────────────────────────────────────────────────

describe('Assets', () => {
  beforeEach(() => vi.clearAllMocks());

  it('shows loading state initially', () => {
    setupMocks();
    // Never resolves — stays in loading
    mockGetAllAssets.mockReturnValue(new Promise(() => {}));
    mockGetAllCategories.mockReturnValue(new Promise(() => {}));

    render(<Assets />);

    expect(screen.getByText('assets.empty.loading')).toBeInTheDocument();
  });

  it('shows error when API fails', async () => {
    setupMocks();
    mockGetAllAssets.mockRejectedValue(new Error('fail'));

    render(<Assets />);

    await waitFor(() =>
      expect(screen.getByText('assets.errors.loadAssets')).toBeInTheDocument()
    );
  });

  it('renders assets after successful load', async () => {
    setupMocks();

    render(<Assets />);

    await waitFor(() =>
      expect(screen.getByText('MacBook')).toBeInTheDocument()
    );
  });

  it('renders category buttons from API', async () => {
    setupMocks();

    render(<Assets />);

    await waitFor(() =>
      expect(screen.getByText('Laptops')).toBeInTheDocument()
    );
  });

  it('filters assets by search input', async () => {
    mockUseCurrentUser.mockReturnValue({ user: { id: 1 } });
    mockIsAdmin.mockReturnValue(false);
    mockGetAllCategories.mockResolvedValue({ content: [mockCategory] });
    mockGetAllAssets.mockResolvedValue({
      content: [
        mockAsset,
        { id: 11, name: 'Monitor', categoryId: 1, status: 'ACTIVE' },
      ],
    });

    render(<Assets />);

    await waitFor(() => expect(screen.getByText('MacBook')).toBeInTheDocument());

    fireEvent.change(screen.getByLabelText('search'), { target: { value: 'Monitor' } });

    expect(screen.getByText('Monitor')).toBeInTheDocument();
    expect(screen.queryByText('MacBook')).not.toBeInTheDocument();
  });

  describe('admin actions', () => {
    it('shows add button for admin', async () => {
      setupMocks({ admin: true });

      render(<Assets />);

      await waitFor(() =>
        expect(screen.getByText('assets.actions.new')).toBeInTheDocument()
      );
    });

    it('does not show add button for non-admin', async () => {
      setupMocks({ admin: false });

      render(<Assets />);

      await waitFor(() => screen.getByText('MacBook'));

      expect(screen.queryByText('assets.actions.new')).not.toBeInTheDocument();
    });

    it('opens add modal when add button is clicked', async () => {
      setupMocks({ admin: true });

      render(<Assets />);

      await waitFor(() => fireEvent.click(screen.getByText('assets.actions.new')));

      expect(screen.getByText('form-modal')).toBeInTheDocument();
    });

    it('opens edit modal when edit is clicked', async () => {
      setupMocks({ admin: true });

      render(<Assets />);

      await waitFor(() => fireEvent.click(screen.getByText('edit-10')));

      expect(screen.getByText('form-modal')).toBeInTheDocument();
    });

    it('opens delete modal when delete is clicked', async () => {
      setupMocks({ admin: true });

      render(<Assets />);

      await waitFor(() => fireEvent.click(screen.getByText('delete-10')));

      expect(screen.getByText('delete-modal')).toBeInTheDocument();
    });

    it('calls deleteAsset and closes modal on confirm', async () => {
      setupMocks({ admin: true });
      mockDeleteAsset.mockResolvedValue({});

      render(<Assets />);

      await waitFor(() => fireEvent.click(screen.getByText('delete-10')));
      fireEvent.click(screen.getByText('confirm-delete'));

      await waitFor(() => expect(mockDeleteAsset).toHaveBeenCalledWith(10));
      expect(screen.queryByText('delete-modal')).not.toBeInTheDocument();
    });

    it('shows ShowDeletedFilter for admin', async () => {
      setupMocks({ admin: true });

      render(<Assets />);

      await waitFor(() =>
        expect(screen.getByText('toggle-deleted')).toBeInTheDocument()
      );
    });
  });

  describe('modals', () => {
    it('opens view modal', async () => {
      setupMocks();

      render(<Assets />);

      await waitFor(() => fireEvent.click(screen.getByText('view-10')));

      expect(screen.getByText('asset-modal')).toBeInTheDocument();
    });

    it('opens bookings modal', async () => {
      setupMocks();

      render(<Assets />);

      await waitFor(() => fireEvent.click(screen.getByText('bookings-10')));

      expect(screen.getByText('bookings-modal')).toBeInTheDocument();
    });

    it('opens report modal', async () => {
      setupMocks();

      render(<Assets />);

      await waitFor(() => fireEvent.click(screen.getByText('report-10')));

      expect(screen.getByText('report-modal')).toBeInTheDocument();
    });
  });
});