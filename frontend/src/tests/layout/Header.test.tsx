import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import { Header } from '../../components/layout/Header';

vi.mock('../../components/icons/Logo', () => ({
  Logo: ({ className }: React.SVGProps<SVGSVGElement>) => (
    <svg className={className} aria-label="Logo" />
  ),
}));

vi.mock('../../components/ui/ThemeToggle', () => ({
  default: () => <button>ThemeToggle</button>,
}));

vi.mock('../../components/ui/LanguageSwitcher', () => ({
  default: () => <button>LanguageSwitcher</button>,
}));

vi.mock('../../components/layout/MobileMenu', () => ({
  default: () => <div>MobileMenu</div>,
}));

vi.mock('../../components/layout/Layout', () => ({
  Layout: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => (
    <div className={className}>{children}</div>
  ),
  LayoutRow: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => (
    <div className={className}>{children}</div>
  ),
  LayoutColumn: ({ children, className }: React.HTMLAttributes<HTMLDivElement>) => (
    <div className={className}>{children}</div>
  ),
}));

const renderHeader = (props = {}) =>
  render(
    <MemoryRouter>
      <Header {...props} />
    </MemoryRouter>
  );

describe('Header', () => {
  it('renders the header element', () => {
    renderHeader();
    expect(document.querySelector('div.fixed')).toBeInTheDocument();
  });

  it('renders the logo link to home', () => {
    renderHeader();
    const links = screen.getAllByRole('link');
    expect(links[0]).toHaveAttribute('href', '/');
  });

  it('renders ThemeToggle', () => {
    renderHeader();
    expect(screen.getByText('ThemeToggle')).toBeInTheDocument();
  });

  it('renders LanguageSwitcher', () => {
    renderHeader();
    expect(screen.getByText('LanguageSwitcher')).toBeInTheDocument();
  });

  it('renders MobileMenu', () => {
    renderHeader();
    expect(screen.getByText('MobileMenu')).toBeInTheDocument();
  });

  it('applies custom className', () => {
    renderHeader({ className: 'custom-class' });
    expect(document.querySelector('.custom-class')).toBeInTheDocument();
  });

  it('renders app variant without Layout wrapper', () => {
    const { container } = renderHeader({ variant: 'app' });
    // app variant uses a plain flex div, not Layout component
    expect(container.querySelector('.flex.h-full')).toBeInTheDocument();
  });

  it('renders public variant by default', () => {
    renderHeader();
    // public variant renders Layout, LayoutRow, LayoutColumn mocks
    const links = screen.getAllByRole('link');
    expect(links[0]).toHaveAttribute('href', '/');
  });
});