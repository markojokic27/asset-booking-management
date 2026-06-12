import { render, screen } from '@testing-library/react';
import { vi, describe, it, expect } from 'vitest';

vi.mock('react-i18next', () => ({ useTranslation: () => ({ t: (k: string) => k }) }));
vi.mock('../../components/icons/FooterLogo', () => ({
  FooterLogo: ({ className }: React.SVGProps<SVGSVGElement>) => (
    <svg className={className} role="img" aria-label="Bundesdruckerei logo" />
  ),
}));
vi.mock('@radix-ui/react-icons', () => ({
  GlobeIcon: () => <svg aria-hidden="true" />,
  MobileIcon: () => <svg aria-hidden="true" />,
  EnvelopeClosedIcon: () => <svg aria-hidden="true" />,
}));

import { Footer } from '../../components/layout/Footer';

describe('Footer', () => {
  it('renders footer with all content', () => {
    render(<Footer />);
    expect(screen.getByRole('contentinfo')).toBeInTheDocument();
    expect(screen.getByText('+385 21279 130')).toBeInTheDocument();
    expect(screen.getByText('info@maurer-electronics.hr')).toBeInTheDocument();
    for (const key of ['layout.footer.websiteLinkLabel', 'layout.footer.copyright', 'layout.footer.partOfThe', 'layout.footer.groupName']) {
      expect(screen.getByText(key)).toBeInTheDocument();
    }
  });

  it('renders website link with correct attributes', () => {
    render(<Footer />);
    const link = screen.getByRole('link', { name: /layout\.footer\.websiteLinkLabel/i });
    expect(link).toHaveAttribute('href', 'https://www.bundesdruckerei.de/en/careers/maurer-electronics-split');
    expect(link).toHaveAttribute('target', '_blank');
    expect(link).toHaveAttribute('rel', 'noreferrer');
  });

  it('merges custom className with base classes', () => {
    render(<Footer className="custom-class" />);
    const footer = screen.getByRole('contentinfo');
    expect(footer).toHaveClass('z-10', 'custom-class');
  });
});