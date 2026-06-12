import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));

import { HeaderHero } from '../../components/layout/HeaderHero';

describe('HeaderHero', () => {
  it('renders title, description lines and divider bars', () => {
    const { container } = render(<HeaderHero />);
    expect(screen.getByRole('heading', { level: 1 })).toHaveTextContent('layout.headerHero.title');
    const p = container.querySelector('p');
    for (const key of ['layout.headerHero.descriptionLine1', 'layout.headerHero.descriptionLine2', 'layout.headerHero.descriptionLine3']) {
      expect(p?.textContent).toContain(key);
    }
    expect(container.querySelector('.bg-\\[\\#00D097\\]')).toBeInTheDocument();
    expect(container.querySelector('.bg-\\[\\#030043\\]')).toBeInTheDocument();
  });

  it('applies custom className and forwards props', () => {
    const { container } = render(<HeaderHero className="custom-class" id="hero-section" />);
    expect(container.firstChild).toHaveClass('custom-class');
    expect(container.firstChild).toHaveAttribute('id', 'hero-section');
  });
});