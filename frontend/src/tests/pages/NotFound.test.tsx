import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

const mockNavigate = vi.fn();

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('react-router-dom', () => ({ useNavigate: () => mockNavigate }));
vi.mock('../../components/layout/Layout', () => ({
  Layout: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
  LayoutRow: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
  LayoutColumn: ({ children, className }: { children: React.ReactNode; className?: string }) => (
    <div className={className}>{children}</div>
  ),
}));
vi.mock('../../components/ui/Button', () => ({
  Button: ({ children, onClick }: { children: React.ReactNode; onClick: () => void }) => (
    <button onClick={onClick}>{children}</button>
  ),
}));

import NotFound from '../../pages/NotFound';

describe('NotFound', () => {
  const user = userEvent.setup();

  beforeEach(() => mockNavigate.mockClear());

  it('renders title and go home button', () => {
    render(<NotFound />);
    expect(screen.getByRole('heading', { level: 1 })).toHaveTextContent('ui.notFound.title');
    expect(screen.getByRole('button', { name: 'ui.notFound.goHome' })).toBeInTheDocument();
  });

  it('navigates to "/" on button click', async () => {
    render(<NotFound />);
    await user.click(screen.getByRole('button', { name: 'ui.notFound.goHome' }));
    expect(mockNavigate).toHaveBeenCalledOnce();
    expect(mockNavigate).toHaveBeenCalledWith('/');
  });

  it('applies correct CSS classes to the column', () => {
    render(<NotFound />);
    const column = screen.getByRole('heading', { level: 1 }).parentElement;
    expect(column).toHaveClass('flex', 'h-screen', 'flex-col', 'items-center', 'justify-center');
  });
});