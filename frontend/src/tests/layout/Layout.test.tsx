import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { Layout, LayoutRow, LayoutColumn } from '../../components/layout/Layout';

const getRoot = (ui: React.ReactElement) => render(ui).container.firstChild;

describe('Layout', () => {
  it('renders children, base classes, custom className and forwarded props', () => {
    const { container } = render(<Layout className="custom-class" id="main-layout">content</Layout>);
    expect(screen.getByText('content')).toBeInTheDocument();
    expect(container.firstChild).toHaveClass('container', 'mx-auto', 'custom-class');
    expect(container.firstChild).toHaveAttribute('id', 'main-layout');
  });
});

describe('LayoutRow', () => {
  it('renders children, base classes, custom className and forwarded props', () => {
    const { container } = render(<LayoutRow className="custom-class" id="main-row">row content</LayoutRow>);
    expect(screen.getByText('row content')).toBeInTheDocument();
    expect(container.firstChild).toHaveClass('-mx-1', 'flex', 'flex-wrap', 'custom-class');
    expect(container.firstChild).toHaveAttribute('id', 'main-row');
  });
});

describe('LayoutColumn', () => {
  it('renders children, base classes, custom className and forwarded props', () => {
    const { container } = render(<LayoutColumn className="custom-class" id="main-col">col content</LayoutColumn>);
    expect(screen.getByText('col content')).toBeInTheDocument();
    expect(container.firstChild).toHaveClass('relative', 'px-1', 'custom-class');
    expect(container.firstChild).toHaveAttribute('id', 'main-col');
  });

  it('applies default span of 12', () => {
    expect(getRoot(<LayoutColumn />)).toHaveClass('w-column-12');
  });

  it('applies custom span and offset', () => {
    expect(getRoot(<LayoutColumn span={6} offset={2} />)).toHaveClass('w-column-6', 'offset-2');
  });

  it('applies responsive span classes', () => {
    const el = getRoot(<LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3} />);
    expect(el).toHaveClass('sm:w-column-12', 'md:w-column-6', 'lg:w-column-4', 'xl:w-column-3');
  });

  it('applies responsive offset classes', () => {
    const el = getRoot(<LayoutColumn smOffset={1} mdOffset={2} lgOffset={3} xlOffset={4} />);
    expect(el).toHaveClass('sm:offset-1', 'md:offset-2', 'lg:offset-3', 'xl:offset-4');
  });

  it('does not apply class for undefined span', () => {
    expect(getRoot(<LayoutColumn smSpan={undefined} />)).not.toHaveClass('sm:w-column-undefined');
  });
});