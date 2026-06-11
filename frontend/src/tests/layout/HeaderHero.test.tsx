import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import { HeaderHero } from '../../components/layout/HeaderHero';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string) => key,
  }),
}));

describe('HeaderHero', () => {
  it('renders the title', () => {
    render(<HeaderHero />);
    expect(screen.getByRole('heading', { level: 1 })).toHaveTextContent(
      'layout.headerHero.title'
    );
  });

  it('renders all description lines', () => {
    const { container } = render(<HeaderHero />);
    const p = container.querySelector('p');
    expect(p?.textContent).toContain('layout.headerHero.descriptionLine1');
    expect(p?.textContent).toContain('layout.headerHero.descriptionLine2');
    expect(p?.textContent).toContain('layout.headerHero.descriptionLine3');
  });

  it('renders the two colored divider bars', () => {
    const { container } = render(<HeaderHero />);
    expect(container.querySelector('.bg-\\[\\#00D097\\]')).toBeInTheDocument();
    expect(container.querySelector('.bg-\\[\\#030043\\]')).toBeInTheDocument();
  });

  it('applies custom className', () => {
    const { container } = render(<HeaderHero className="custom-class" />);
    expect(container.firstChild).toHaveClass('custom-class');
  });

  it('forwards additional props to the root element', () => {
    const { container } = render(<HeaderHero id="hero-section" />);
    expect(container.firstChild).toHaveAttribute('id', 'hero-section');
  });
});