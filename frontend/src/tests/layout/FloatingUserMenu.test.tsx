import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';

vi.mock('../../components/ui/UserMenu', () => ({ default: () => <div>UserMenu</div> }));

import FloatingUserMenu from '../../components/layout/FloatingUserMenu';

describe('FloatingUserMenu', () => {
  it('renders UserMenu with fixed positioning', () => {
    const { container } = render(<FloatingUserMenu />);
    expect(screen.getByText('UserMenu')).toBeInTheDocument();
    expect(container.firstChild).toHaveClass('fixed', 'top-20', 'z-50');
  });
});