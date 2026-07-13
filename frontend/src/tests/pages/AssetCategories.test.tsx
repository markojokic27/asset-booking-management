import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import AssetCategories from '../../pages/AssetCategories';
import { mockUseAuth, authState } from '../mocks/auth';
import { isAdmin } from '../../features/user/utils/users';
import { getAllCategories, getCategoryById } from '../../features/asset-category/api/categoryApi';

// Mocks 

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../features/user/utils/users', async (importOriginal) => {
  const actual = await importOriginal<typeof import('../../features/user/utils/users')>();
  return { ...actual, isAdmin: vi.fn() };
});
vi.mock('../../features/asset-category/api/categoryApi', () => ({
  getAllCategories: vi.fn(),
  getCategoryById: vi.fn(),
  createCategory: vi.fn(),
  updateCategory: vi.fn(),
}));
vi.mock('../../components/layout/Layout', () => ({
  LayoutColumn: ({ children }: React.HTMLAttributes<HTMLDivElement>) => <div>{children}</div>,
}));
vi.mock('../../components/ui/SearchBar', () => ({
  SearchInput: ({ value, onChange, placeholder }: any) => (
    <input value={value} onChange={(e) => onChange(e.target.value)} placeholder={placeholder} />
  ),
}));
vi.mock('../../components/ui/Pagination', () => ({
  Pagination: ({ page, totalPages, onPageChange }: any) => (
    <div>
      <span>Page {page} of {totalPages}</span>
      <button onClick={() => onPageChange(page + 1)}>Next</button>
    </div>
  ),
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick, ...props }: React.ButtonHTMLAttributes<HTMLButtonElement>) => (
    <button onClick={onClick} {...props}>{children}</button>
  ),
}));
vi.mock('../../features/asset-category/components/AssetCategoriesTable', () => ({
  AssetCategoriesTable: ({ data, onView, onEdit }: any) => (
    <div>
      {data.map((cat: any) => (
        <div key={cat.id}>
          <span>{cat.name}</span>
          <button onClick={() => onView(cat)}>View</button>
          {onEdit && <button onClick={() => onEdit(cat)}>Edit</button>}
        </div>
      ))}
    </div>
  ),
}));
vi.mock('../../features/asset-category/components/CategoryModal', () => ({
  CategoryModal: ({ isOpen, onClose, category }: any) =>
    isOpen ? <div><span>CategoryModal: {category?.name ?? 'loading'}</span><button onClick={onClose}>Close</button></div> : null,
}));
vi.mock('../../features/asset-category/components/CategoryFormModal', () => ({
  CategoryFormModal: ({ isOpen, mode, onClose }: any) =>
    isOpen ? <div><span>CategoryFormModal: {mode}</span><button onClick={onClose}>Close</button></div> : null,
}));
vi.mock('@mui/icons-material/AddSharp', () => ({ default: () => <svg /> }));

// Helpers 

const mockCategories = [
  { id: 1, name: 'Laptops', description: 'Laptop devices', bookingPeriod: 'DAY', approval: false },
  { id: 2, name: 'Projectors', description: 'Projector devices', bookingPeriod: 'HOUR', approval: true },
];

const setCategories = (data = mockCategories) =>
  vi.mocked(getAllCategories).mockResolvedValue({ content: data } as any);

const renderPage = () => render(<MemoryRouter><AssetCategories /></MemoryRouter>);


//  Tests 

describe('AssetCategories', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(isAdmin).mockReturnValue(false);
    mockUseAuth.mockReturnValue(authState({ user: null }));
    setCategories([]);
  });

  it('shows loading state initially', () => {
    vi.mocked(getAllCategories).mockReturnValue(new Promise(() => {}));
    renderPage();
    expect(screen.getByText('assetCategories.empty.loading')).toBeInTheDocument();
  });

  it('renders page title and categories after load', async () => {
    setCategories();
    renderPage();
    await waitFor(() => {
      expect(screen.getByText('assetCategories.title')).toBeInTheDocument();
      expect(screen.getByText('Laptops')).toBeInTheDocument();
      expect(screen.getByText('Projectors')).toBeInTheDocument();
    });
  });

  it('shows error when load fails', async () => {
    vi.mocked(getAllCategories).mockRejectedValue(new Error('fail'));
    renderPage();
    await waitFor(() => expect(screen.getByText('assetCategories.errors.loadFailed')).toBeInTheDocument());
  });

  it('shows empty message when no categories', async () => {
    renderPage();
    await waitFor(() => expect(screen.getByText('assetCategories.empty.none')).toBeInTheDocument());
  });

  it('filters categories by search input', async () => {
    setCategories();
    renderPage();
    await waitFor(() => screen.getByText('Laptops'));
    await userEvent.type(screen.getByPlaceholderText('assetCategories.search.placeholder'), 'Proj');
    expect(screen.getByText('Projectors')).toBeInTheDocument();
    expect(screen.queryByText('Laptops')).not.toBeInTheDocument();
  });

  describe('admin-only features', () => {
    beforeEach(() => vi.mocked(isAdmin).mockReturnValue(true));

    it('shows add button for admin, hides for non-admin', async () => {
      setCategories();
      const { unmount } = renderPage();
      await waitFor(() => expect(screen.getByTestId('add-category-button')).toBeInTheDocument());
      unmount();

      vi.mocked(isAdmin).mockReturnValue(false);
      renderPage();
      await waitFor(() => expect(screen.queryByTestId('add-category-button')).not.toBeInTheDocument());
    });

    it('opens and closes create modal', async () => {
      renderPage();
      await waitFor(() => screen.getByTestId('add-category-button'));
      await userEvent.click(screen.getByTestId('add-category-button'));
      expect(screen.getByText('CategoryFormModal: create')).toBeInTheDocument();
      await userEvent.click(screen.getByText('Close'));
      expect(screen.queryByText('CategoryFormModal: create')).not.toBeInTheDocument();
    });

    it('shows edit buttons and opens edit modal', async () => {
      setCategories();
      vi.mocked(getCategoryById).mockResolvedValue(mockCategories[0] as any);
      renderPage();
      await waitFor(() => screen.getAllByText('Edit'));
      expect(screen.getAllByText('Edit')).toHaveLength(mockCategories.length);
      await userEvent.click(screen.getAllByText('Edit')[0]);
      expect(screen.getByText('CategoryFormModal: edit')).toBeInTheDocument();
    });
  });

  it('opens view modal on view click', async () => {
    setCategories();
    vi.mocked(getCategoryById).mockResolvedValue(mockCategories[0] as any);
    renderPage();
    await waitFor(() => screen.getAllByText('View'));
    await userEvent.click(screen.getAllByText('View')[0]);
    expect(screen.getByText(/CategoryModal/)).toBeInTheDocument();
  });
});