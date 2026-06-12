import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../components/layout/Layout', () => ({
  Layout: ({ children }: React.HTMLAttributes<HTMLDivElement>) => <div>{children}</div>,
  LayoutRow: ({ children }: React.HTMLAttributes<HTMLDivElement>) => <div>{children}</div>,
  LayoutColumn: ({ children }: React.HTMLAttributes<HTMLDivElement>) => <div>{children}</div>,
}));
vi.mock('../../components/layout/HeaderHero', () => ({ HeaderHero: () => <div>HeaderHero</div> }));
vi.mock('../../components/layout/Header', () => ({
  Header: ({ className }: { className?: string }) => <header className={className}>Header</header>,
}));
vi.mock('../../components/icons/Logo', () => ({
  Logo: ({ className }: React.SVGProps<SVGSVGElement>) => <svg className={className} aria-label="Logo" />,
}));
vi.mock('../../features/auth/components/RegisterForm', () => ({ default: () => <div>RegisterForm</div> }));

import Register from '../../pages/Register';

const renderPage = () => render(<MemoryRouter><Register /></MemoryRouter>);

describe('Register', () => {
  it('renders all key elements', () => {
    const { container } = renderPage();
    expect(screen.getByText('HeaderHero')).toBeInTheDocument();
    expect(screen.getByText('RegisterForm')).toBeInTheDocument();
    expect(container.querySelector('svg[aria-label="Logo"]')).toBeInTheDocument();
  });

  it('renders Header with hidden md:flex classes', () => {
    renderPage();
    expect(screen.getByText('Header').closest('header')).toHaveClass('hidden', 'md:flex');
  });
});