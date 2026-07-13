import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { authState, mockUseAuth } from '../mocks/auth';

// ── Mocks ─────────────────────────────────────────────────────────────────────

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string, opts?: Record<string, unknown>) => opts?.name ? `${key}:${opts.name}` : key }),
}));
vi.mock('@mui/icons-material/Add', () => ({ default: () => null }));

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

vi.mock('../../features/user/utils/users', async (importOriginal) => {
  const actual = await importOriginal<typeof import('../../features/user/utils/users')>();
  return { ...actual, isAdmin: vi.fn() };
});
import { isAdmin } from '../../features/user/utils/users';
const mockIsAdmin = vi.mocked(isAdmin);

vi.mock('../../features/user/hooks/usePagination', () => ({
  usePagination: (items: unknown[]) => ({
    paged: items, page: 1, totalPages: 1, items: items.length, setPage: vi.fn(),
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
vi.mock('../../components/ui/Pagination', () => ({ Pagination: () => <div>Pagination</div> }));

let capturedDeleteModalProps: Record<string, unknown> = {};
vi.mock('../../components/ui/DeleteModal', () => ({
  DeleteModal: (props: Record<string, unknown>) => {
    capturedDeleteModalProps = props;
    return (props.isOpen as boolean) ? (
      <div>
        <span>delete-modal</span>
        <button onClick={props.onConfirm as () => void}>confirm-delete</button>
        <button onClick={props.onClose as () => void}>cancel-delete</button>
      </div>
    ) : null;
  },
}));
vi.mock('../../features/asset/components/AssetCategoryGrid', () => ({
  AssetCategoryGrid: ({
    categories,
    onSelectCategory,
    allCategory,
  }: {
    categories: string[];
    onSelectCategory: (c: string) => void;
    allCategory?: { label: string; value: string };
  }) => (
    <div>
      {allCategory && (
        <button onClick={() => onSelectCategory(allCategory.value)}>
          {allCategory.label}
        </button>
      )}
      {categories.map((c) => <button key={c} onClick={() => onSelectCategory(c)}>{c}</button>)}
    </div>
  ),
}));
vi.mock('../../features/asset/components/AssetTable', () => ({
  AssetsTable: ({ assets, onView, onEdit, onDelete, onBookings, onReport, onToggleNameSort, nameSortDir }: {
    assets: { id: number; name: string }[];
    nameSortDir: string;
    onToggleNameSort: () => void;
    onView: (a: { id: number; name: string }) => void;
    onEdit?: (a: { id: number; name: string }) => void;
    onDelete?: (a: { id: number; name: string }) => void;
    onBookings: (a: { id: number; name: string }) => void;
    onReport: (a: { id: number; name: string }) => void;
  }) => (
    <div>
      <button onClick={onToggleNameSort}>toggle-sort</button>
      <span>sort-dir:{nameSortDir}</span>
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
  AssetModal: ({ isOpen }: { isOpen: boolean }) => isOpen ? <div>asset-modal</div> : null,
}));
vi.mock('../../features/asset/components/AssetBookingsModal', () => ({
  AssetBookingsModal: ({ isOpen }: { isOpen: boolean }) => isOpen ? <div>bookings-modal</div> : null,
}));
vi.mock('../../features/asset/components/AssetReportModal', () => ({
  AssetReportModal: ({ isOpen }: { isOpen: boolean }) => isOpen ? <div>report-modal</div> : null,
}));
vi.mock('../../features/asset/components/AssetFormModal', () => ({
  AssetFormModal: ({ isOpen, onCreate, onSave }: {
    isOpen: boolean;
    onCreate: (payload: unknown) => Promise<void>;
    onSave: (asset: { id: number; name: string; categoryId: number; status: string; location: string; description: string }) => Promise<void>;
  }) =>
    isOpen ? (
      <div>
        <span>form-modal</span>
        <button onClick={() => onCreate({ name: 'New Asset', categoryId: 1 })}>submit-create</button>
        <button onClick={() => onSave({ id: 10, name: 'Updated', categoryId: 1, status: 'ACTIVE', location: 'A', description: 'D' })}>submit-save</button>
      </div>
    ) : null,
}));
vi.mock('../../features/user/components/ShowDeletedFilter', () => ({
  ShowDeletedFilter: ({ onToggle }: { onToggle: () => void }) => (
    <button onClick={onToggle}>toggle-deleted</button>
  ),
}));

import Assets from '../../pages/Assets';

// ── Fixtures ──────────────────────────────────────────────────────────────────

const mockCategory = { id: 1, name: 'Laptops' };
const mockAsset = { id: 10, name: 'MacBook', categoryId: 1, categoryName: 'Laptops', status: 'ACTIVE' };
const deletedAsset = { id: 11, name: 'OldLaptop', categoryId: 1, categoryName: 'Laptops', status: 'DELETED' };

const setupMocks = ({ admin = false } = {}) => {
  mockUseAuth.mockReturnValue(authState({
    user: { id: 1, role: admin ? 'ADMIN' : 'MANAGER' } as any,
  }));
  mockIsAdmin.mockReturnValue(admin);
  mockGetAllCategories.mockResolvedValue({ content: [mockCategory] });
  mockGetAllAssets.mockResolvedValue({ content: [mockAsset] });
};

const waitForLoad = () => waitFor(() => expect(screen.getByText('MacBook')).toBeInTheDocument());
const openDeleteModal = () => waitFor(() => fireEvent.click(screen.getByText('delete-10')));

// ── Tests ─────────────────────────────────────────────────────────────────────

describe('Assets', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    capturedDeleteModalProps = {};
  });

  it('shows loading state initially', () => {
    setupMocks();
    mockGetAllAssets.mockReturnValue(new Promise(() => {}));
    mockGetAllCategories.mockReturnValue(new Promise(() => {}));
    render(<Assets />);
    expect(screen.getByText('assets.empty.loading')).toBeInTheDocument();
  });

  it('shows error when API fails', async () => {
    setupMocks();
    mockGetAllAssets.mockRejectedValue(new Error('fail'));
    render(<Assets />);
    await waitFor(() => expect(screen.getByText('assets.errors.loadAssets')).toBeInTheDocument());
  });

  it('renders assets and categories after load', async () => {
    setupMocks();
    render(<Assets />);
    await waitFor(() => {
      expect(screen.getByText('MacBook')).toBeInTheDocument();
      expect(screen.getByText('Laptops')).toBeInTheDocument();
    });
  });

  it('filters assets by search input', async () => {
    setupMocks();
    mockGetAllAssets.mockResolvedValue({
      content: [mockAsset, { id: 11, name: 'Monitor', categoryId: 1, status: 'ACTIVE' }],
    });
    render(<Assets />);
    await waitForLoad();
    fireEvent.change(screen.getByLabelText('search'), { target: { value: 'Monitor' } });
    expect(screen.getByText('Monitor')).toBeInTheDocument();
    expect(screen.queryByText('MacBook')).not.toBeInTheDocument();
  });

  describe('category filtering', () => {
    it('shows all assets and correct title by default', async () => {
      setupMocks();
      mockGetAllAssets.mockResolvedValue({
        content: [mockAsset, { id: 11, name: 'Monitor', categoryId: 2, categoryName: 'Monitors', status: 'ACTIVE' }],
      });
      render(<Assets />);
      await waitFor(() => {
        expect(screen.getByText('MacBook')).toBeInTheDocument();
        expect(screen.getByText('Monitor')).toBeInTheDocument();
        expect(screen.getAllByText('assets.categories.all').length).toBeGreaterThan(0);
      });
    });

    it('filters assets and updates title when category selected', async () => {
      setupMocks();
      mockGetAllCategories.mockResolvedValue({ content: [mockCategory, { id: 2, name: 'Monitors' }] });
      mockGetAllAssets.mockResolvedValue({
        content: [mockAsset, { id: 11, name: 'Monitor', categoryId: 2, categoryName: 'Monitors', status: 'ACTIVE' }],
      });
      render(<Assets />);
      await waitFor(() => screen.getByText('Monitors'));
      fireEvent.click(screen.getByText('Monitors'));
      expect(screen.getByText('Monitor')).toBeInTheDocument();
      expect(screen.queryByText('MacBook')).not.toBeInTheDocument();
      expect(screen.getByRole('heading', { name: 'Monitors' })).toBeInTheDocument();
    });

    it.each([
      ['categoryMap fallback when categoryName is null',                { ...mockAsset, categoryName: null }],
      ['"-" fallback when both categoryName and categoryMap missing',   { ...mockAsset, categoryId: 999, categoryName: null }],
    ])('uses %s', async (_, asset) => {
      setupMocks();
      if (asset.categoryId === 999) mockGetAllCategories.mockResolvedValue({ content: [] });
      mockGetAllAssets.mockResolvedValue({ content: [asset] });
      render(<Assets />);
      await waitForLoad();
      expect(screen.getByText('MacBook')).toBeInTheDocument();
    });
  });

  describe('sorting', () => {
    const twoAssets = [
      { id: 11, name: 'Zebra', categoryId: 1, categoryName: 'Laptops', status: 'ACTIVE' },
      { id: 10, name: 'Apple', categoryId: 1, categoryName: 'Laptops', status: 'ACTIVE' },
    ];

    it('sorts asc by default, toggles to desc on click', async () => {
      setupMocks();
      mockGetAllAssets.mockResolvedValue({ content: twoAssets });
      render(<Assets />);
      await waitFor(() => screen.getByText('Apple'));

      expect(screen.getByText('sort-dir:asc')).toBeInTheDocument();
      let items = screen.getAllByText(/^(Apple|Zebra)$/);
      expect(items[0].textContent).toBe('Apple');
      expect(items[1].textContent).toBe('Zebra');

      fireEvent.click(screen.getByText('toggle-sort'));
      expect(screen.getByText('sort-dir:desc')).toBeInTheDocument();
      items = screen.getAllByText(/^(Apple|Zebra)$/);
      expect(items[0].textContent).toBe('Zebra');
      expect(items[1].textContent).toBe('Apple');
    });
  });

  describe('showDeleted filter', () => {
    it('hides deleted assets by default, shows after toggle, hides again on second toggle', async () => {
      setupMocks({ admin: true });
      mockGetAllAssets.mockResolvedValue({ content: [mockAsset, deletedAsset] });
      render(<Assets />);
      await waitForLoad();
      expect(screen.queryByText('OldLaptop')).not.toBeInTheDocument();
      fireEvent.click(screen.getByText('toggle-deleted'));
      expect(screen.getByText('OldLaptop')).toBeInTheDocument();
      fireEvent.click(screen.getByText('toggle-deleted'));
      expect(screen.queryByText('OldLaptop')).not.toBeInTheDocument();
    });
  });

  describe('admin actions', () => {
    it('shows add button and ShowDeletedFilter for admin', async () => {
      setupMocks({ admin: true });
      render(<Assets />);
      await waitFor(() => {
        expect(screen.getByText('assets.actions.new')).toBeInTheDocument();
        expect(screen.getByText('toggle-deleted')).toBeInTheDocument();
      });
    });

    it('hides add button for non-admin', async () => {
      setupMocks();
      render(<Assets />);
      await waitForLoad();
      expect(screen.queryByText('assets.actions.new')).not.toBeInTheDocument();
    });

    it('opens add modal on add button click', async () => {
      setupMocks({ admin: true });
      render(<Assets />);
      await waitFor(() => fireEvent.click(screen.getByText('assets.actions.new')));
      expect(screen.getByText('form-modal')).toBeInTheDocument();
    });

    it('opens edit modal on edit click', async () => {
      setupMocks({ admin: true });
      render(<Assets />);
      await waitFor(() => fireEvent.click(screen.getByText('edit-10')));
      expect(screen.getByText('form-modal')).toBeInTheDocument();
    });

    it('passes correct title and item name to DeleteModal', async () => {
      setupMocks({ admin: true });
      render(<Assets />);
      await openDeleteModal();
      expect(capturedDeleteModalProps.title).toBe('assets.delete.title');
      expect((capturedDeleteModalProps.getItemName as (item: unknown) => string)?.(mockAsset)).toBe('MacBook');
    });

    it('confirms delete, marks asset as DELETED and shows after showDeleted toggle', async () => {
      setupMocks({ admin: true });
      mockDeleteAsset.mockResolvedValue({});
      render(<Assets />);
      await openDeleteModal();
      fireEvent.click(screen.getByText('confirm-delete'));
      await waitFor(() => {
        expect(mockDeleteAsset).toHaveBeenCalledWith(10);
        expect(screen.queryByText('delete-modal')).not.toBeInTheDocument();
      });
      fireEvent.click(screen.getByText('toggle-deleted'));
      expect(screen.getByText('MacBook')).toBeInTheDocument();
    });

    it('logs error and keeps asset in list when deleteAsset fails', async () => {
      setupMocks({ admin: true });
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
      mockDeleteAsset.mockRejectedValue(new Error('delete failed'));
      render(<Assets />);
      await openDeleteModal();
      fireEvent.click(screen.getByText('confirm-delete'));
      await waitFor(() =>
        expect(consoleSpy).toHaveBeenCalledWith('Failed to delete asset:', expect.any(Error))
      );
      expect(screen.getByText('MacBook')).toBeInTheDocument();
      consoleSpy.mockRestore();
    });
  });

  describe('modals', () => {
    it.each([
      ['view',     'view-10',     'asset-modal'],
      ['bookings', 'bookings-10', 'bookings-modal'],
      ['report',   'report-10',   'report-modal'],
    ])('opens %s modal on click', async (_, btnText, modalText) => {
      setupMocks();
      render(<Assets />);
      await waitFor(() => fireEvent.click(screen.getByText(btnText)));
      expect(screen.getByText(modalText)).toBeInTheDocument();
    });
  });

  describe('AssetFormModal callbacks', () => {
    describe('onCreate', () => {
      it.each([
        ['categoryName present',         { id: 99, name: 'New Asset', categoryId: 1,   categoryName: 'Laptops', status: 'ACTIVE' }],
        ['categoryMap fallback',         { id: 99, name: 'New Asset', categoryId: 1,   categoryName: null,      status: 'ACTIVE' }],
        ['"-" fallback (no category)',   { id: 99, name: 'New Asset', categoryId: 999, categoryName: null,      status: 'ACTIVE' }],
      ])('%s', async (_, apiResponse) => {
        setupMocks({ admin: true });
        if (apiResponse.categoryId === 999) mockGetAllCategories.mockResolvedValue({ content: [] });
        mockCreateAsset.mockResolvedValue(apiResponse);
        render(<Assets />);
        await waitFor(() => fireEvent.click(screen.getByText('assets.actions.new')));
        fireEvent.click(screen.getByText('submit-create'));
        await waitFor(() => {
          expect(mockCreateAsset).toHaveBeenCalledWith({ name: 'New Asset', categoryId: 1 });
          expect(screen.getByText('New Asset')).toBeInTheDocument();
        });
      });
    });

    describe('onSave', () => {
      it.each([
        ['categoryName present',         { id: 10, name: 'Updated', categoryId: 1,   categoryName: 'Laptops', status: 'ACTIVE' }],
        ['categoryMap fallback',         { id: 10, name: 'Updated', categoryId: 1,   categoryName: null,      status: 'ACTIVE' }],
        ['"-" fallback (no category)',   { id: 10, name: 'Updated', categoryId: 999, categoryName: null,      status: 'ACTIVE' }],
      ])('%s', async (_, apiResponse) => {
        setupMocks({ admin: true });
        if (apiResponse.categoryId === 999) mockGetAllCategories.mockResolvedValue({ content: [] });
        mockUpdateAsset.mockResolvedValue(apiResponse);
        render(<Assets />);
        await waitFor(() => fireEvent.click(screen.getByText('edit-10')));
        fireEvent.click(screen.getByText('submit-save'));
        await waitFor(() => {
          expect(mockUpdateAsset).toHaveBeenCalledWith(10, {
            name: 'Updated', categoryId: 1, status: 'ACTIVE', location: 'A', description: 'D',
          });
          expect(screen.getByText('Updated')).toBeInTheDocument();
        });
      });
    });
  });
});