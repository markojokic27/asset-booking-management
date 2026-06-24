import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { AssetModal } from '../../features/asset/components/AssetModal';
import type { AssetDto } from '../../features/asset/types';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({ t: (key: string) => key }),
}));

vi.mock('@mui/icons-material/Close', () => ({ default: () => <svg /> }));

const asset: AssetDto = {
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
  onClose: vi.fn(),
};

const renderModal = (overrides = {}) =>
  render(<AssetModal isOpen asset={asset} {...baseProps} {...overrides} />);

describe('AssetModal', () => {
  beforeEach(() => vi.clearAllMocks());

  it('does not render when closed', () => {
    renderModal({ isOpen: false });
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('does not render without an asset', () => {
    renderModal({ asset: null });
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('renders asset details', () => {
    renderModal();
    expect(screen.getByRole('heading', { level: 2, name: asset.name })).toBeInTheDocument();
    expect(screen.getByText(asset.description!)).toBeInTheDocument();
    expect(screen.getByText(asset.categoryName!)).toBeInTheDocument();
    expect(screen.getByText(`assets.status.${asset.status}`)).toBeInTheDocument();
  });

  it('renders "-" when description is null', () => {
    renderModal({ asset: { ...asset, description: null } });
    expect(screen.getByText('-')).toBeInTheDocument();
  });

  it('renders "?" when categoryName is null', () => {
    renderModal({ asset: { ...asset, categoryName: null } });
    expect(screen.getByText('?')).toBeInTheDocument();
  });

  it('calls onClose when close button is clicked', () => {
    renderModal();
    fireEvent.click(screen.getByRole('button', { name: 'assets.modals.close' }));
    expect(baseProps.onClose).toHaveBeenCalledTimes(1);
  });

  it('calls onClose when clicking the overlay', () => {
    renderModal();
    fireEvent.click(screen.getByTestId('asset-view-modal-backdrop'));
    expect(baseProps.onClose).toHaveBeenCalledTimes(1);
  });

  it('does not call onClose when clicking inside the modal', () => {
    renderModal();
    fireEvent.click(screen.getByText(asset.description!));
    expect(baseProps.onClose).not.toHaveBeenCalled();
  });
});