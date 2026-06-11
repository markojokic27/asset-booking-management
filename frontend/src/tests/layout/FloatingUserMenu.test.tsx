import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import FloatingUserMenu from '../../components/layout/FloatingUserMenu';

vi.mock('../../components/ui/UserMenu', () => ({
  default: () => <div>UserMenu</div>,
}));

describe('FloatingUserMenu', () => {
  it('renders UserMenu', () => {
    render(<FloatingUserMenu />);
    expect(screen.getByText('UserMenu')).toBeInTheDocument();
  });

  it('renders with fixed positioning classes', () => {
    const { container } = render(<FloatingUserMenu />);
    expect(container.firstChild).toHaveClass('fixed', 'top-20', 'z-50');
  });
});