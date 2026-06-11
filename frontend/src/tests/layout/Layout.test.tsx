import { render, screen } from '@testing-library/react';
import { Layout, LayoutRow, LayoutColumn } from '../../components/layout/Layout';

describe('Layout', () => {
  it('renders children', () => {
    render(<Layout>content</Layout>);
    expect(screen.getByText('content')).toBeInTheDocument();
  });

  it('applies base classes', () => {
    const { container } = render(<Layout />);
    expect(container.firstChild).toHaveClass('container', 'mx-auto');
  });

  it('applies custom className', () => {
    const { container } = render(<Layout className="custom-class" />);
    expect(container.firstChild).toHaveClass('custom-class');
  });

  it('forwards additional props', () => {
    const { container } = render(<Layout id="main-layout" />);
    expect(container.firstChild).toHaveAttribute('id', 'main-layout');
  });
});

describe('LayoutRow', () => {
  it('renders children', () => {
    render(<LayoutRow>row content</LayoutRow>);
    expect(screen.getByText('row content')).toBeInTheDocument();
  });

  it('applies base classes', () => {
    const { container } = render(<LayoutRow />);
    expect(container.firstChild).toHaveClass('-mx-1', 'flex', 'flex-wrap');
  });

  it('applies custom className', () => {
    const { container } = render(<LayoutRow className="custom-class" />);
    expect(container.firstChild).toHaveClass('custom-class');
  });

  it('forwards additional props', () => {
    const { container } = render(<LayoutRow id="main-row" />);
    expect(container.firstChild).toHaveAttribute('id', 'main-row');
  });
});

describe('LayoutColumn', () => {
  it('renders children', () => {
    render(<LayoutColumn>col content</LayoutColumn>);
    expect(screen.getByText('col content')).toBeInTheDocument();
  });

  it('applies base classes', () => {
    const { container } = render(<LayoutColumn />);
    expect(container.firstChild).toHaveClass('relative', 'px-1');
  });

  it('applies default span of 12', () => {
    const { container } = render(<LayoutColumn />);
    expect(container.firstChild).toHaveClass('w-column-12');
  });

  it('applies custom span', () => {
    const { container } = render(<LayoutColumn span={6} />);
    expect(container.firstChild).toHaveClass('w-column-6');
  });

  it('applies offset class', () => {
    const { container } = render(<LayoutColumn offset={2} />);
    expect(container.firstChild).toHaveClass('offset-2');
  });

  it('applies responsive span classes', () => {
    const { container } = render(<LayoutColumn smSpan={12} mdSpan={6} lgSpan={4} xlSpan={3} />);
    expect(container.firstChild).toHaveClass('sm:w-column-12');
    expect(container.firstChild).toHaveClass('md:w-column-6');
    expect(container.firstChild).toHaveClass('lg:w-column-4');
    expect(container.firstChild).toHaveClass('xl:w-column-3');
  });

  it('applies responsive offset classes', () => {
    const { container } = render(<LayoutColumn smOffset={1} mdOffset={2} lgOffset={3} xlOffset={4} />);
    expect(container.firstChild).toHaveClass('sm:offset-1');
    expect(container.firstChild).toHaveClass('md:offset-2');
    expect(container.firstChild).toHaveClass('lg:offset-3');
    expect(container.firstChild).toHaveClass('xl:offset-4');
  });

  it('applies custom className', () => {
    const { container } = render(<LayoutColumn className="custom-class" />);
    expect(container.firstChild).toHaveClass('custom-class');
  });

  it('forwards additional props', () => {
    const { container } = render(<LayoutColumn id="main-col" />);
    expect(container.firstChild).toHaveAttribute('id', 'main-col');
  });

  it('does not apply span class when span is not provided', () => {
    const { container } = render(<LayoutColumn smSpan={undefined} />);
    expect(container.firstChild).not.toHaveClass('sm:w-column-undefined');
  });
});