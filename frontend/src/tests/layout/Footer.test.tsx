import { render, screen } from '@testing-library/react';
import { vi } from 'vitest';
import { Footer } from '../../components/layout/Footer';

vi.mock('react-i18next', () => ({
  useTranslation: () => ({
    t: (key: string) => key,
  }),
}));

vi.mock('../icons/FooterLogo', () => ({
  FooterLogo: ({ className }: React.SVGProps<SVGSVGElement>) => (
    <svg className={className} role="img" aria-label="Bundesdruckerei logo" />
  ),
}));

vi.mock('@radix-ui/react-icons', () => ({
  GlobeIcon: () => <svg aria-hidden="true" />,
  MobileIcon: () => <svg aria-hidden="true" />,
  EnvelopeClosedIcon: () => <svg aria-hidden="true" />,
}));

describe('Footer', () => {
  it('renders the footer element', () => {
    render(<Footer />);
    expect(screen.getByRole('contentinfo')).toBeInTheDocument();
  });

  it('renders the website link with correct href, target and rel', () => {
    render(<Footer />);
    const link = screen.getByRole('link', { name: /layout\.footer\.websiteLinkLabel/i });
    expect(link).toHaveAttribute(
      'href',
      'https://www.bundesdruckerei.de/en/careers/maurer-electronics-split'
    );
    expect(link).toHaveAttribute('target', '_blank');
    expect(link).toHaveAttribute('rel', 'noreferrer');
  });

  it('renders the phone number', () => {
    render(<Footer />);
    expect(screen.getByText('+385 21279 130')).toBeInTheDocument();
  });

  it('renders the email address', () => {
    render(<Footer />);
    expect(screen.getByText('info@maurer-electronics.hr')).toBeInTheDocument();
  });

  it('renders all i18n translation keys', () => {
    render(<Footer />);
    expect(screen.getByText('layout.footer.websiteLinkLabel')).toBeInTheDocument();
    expect(screen.getByText('layout.footer.copyright')).toBeInTheDocument();
    expect(screen.getByText('layout.footer.partOfThe')).toBeInTheDocument();
    expect(screen.getByText('layout.footer.groupName')).toBeInTheDocument();
  });

  it('applies custom className to the footer element', () => {
    render(<Footer className="custom-class" />);
    expect(screen.getByRole('contentinfo')).toHaveClass('custom-class');
  });

  it('merges custom className with base classes', () => {
    render(<Footer className="custom-class" />);
    const footer = screen.getByRole('contentinfo');
    expect(footer).toHaveClass('z-10');
    expect(footer).toHaveClass('custom-class');
  });
});