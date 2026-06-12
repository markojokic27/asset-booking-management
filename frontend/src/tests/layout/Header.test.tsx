import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

vi.mock('../../components/icons/Logo', () => ({
  Logo: ({ className }: React.SVGProps<SVGSVGElement>) => <svg className={className} aria-label="Logo" />,
}));
vi.mock('../../components/ui/ThemeToggle', () => ({ default: () => <button>ThemeToggle</button> }));
vi.mock('../../components/ui/LanguageSwitcher', () => ({ default: () => <button>LanguageSwitcher</button> }));
vi.mock('../../components/layout/MobileMenu', () => ({ default: () => <div>MobileMenu</div> }));
vi.mock('../../components/layout/Layout', () => ({
  Layout: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => <div className={className}>{children}</div>,
  LayoutRow: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => <div className={className}>{children}</div>,
  LayoutColumn: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => <div className={className}>{children}</div>,
}));

import { Header } from '../../components/layout/Header';

const renderHeader = (props = {}) => render(<MemoryRouter><Header {...props} /></MemoryRouter>);

describe('Header', () => {
  it('renders all key elements', () => {
    renderHeader();
    expect(screen.getAllByRole('link')[0]).toHaveAttribute('href', '/');
    expect(screen.getByText('ThemeToggle')).toBeInTheDocument();
    expect(screen.getByText('LanguageSwitcher')).toBeInTheDocument();
    expect(screen.getByText('MobileMenu')).toBeInTheDocument();
  });

  it('applies custom className', () => {
    renderHeader({ className: 'custom-class' });
    expect(document.querySelector('.custom-class')).toBeInTheDocument();
  });

  it('renders app variant with plain flex wrapper', () => {
    const { container } = renderHeader({ variant: 'app' });
    expect(container.querySelector('.flex.h-full')).toBeInTheDocument();
  });
});