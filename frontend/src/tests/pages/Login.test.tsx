import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, it, expect, vi } from 'vitest';
import Login from '../../pages/Login';

vi.mock('../../app/ThemeProvider', () => ({
  ThemeProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>,
  useTheme: () => ({ theme: 'light', toggleTheme: vi.fn() }),
}));

vi.mock('../../components/layout/Layout', () => ({
  Layout: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
  LayoutRow: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
  LayoutColumn: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}));

vi.mock('../../components/layout/Header', () => ({
  Header: () => <header>Header</header>,
}));

vi.mock('../../components/layout/HeaderHero', () => ({
  HeaderHero: () => <div>HeaderHero</div>,
}));

vi.mock('../../components/icons/Logo', () => ({
  Logo: () => <svg aria-label="Logo" />,
}));

vi.mock('../../features/auth/components/LoginForm', () => ({
  default: () => <form aria-label="LoginForm" />,
}));

describe('Login', () => {
  it('renders without crashing', () => {
    render(<MemoryRouter><Login /></MemoryRouter>);

    expect(screen.getByText('Header')).toBeInTheDocument();
    expect(screen.getByText('HeaderHero')).toBeInTheDocument();
    expect(screen.getByLabelText('Logo')).toBeInTheDocument();
    expect(screen.getByLabelText('LoginForm')).toBeInTheDocument();
  });
});