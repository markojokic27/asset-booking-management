import type { Meta, StoryObj } from '@storybook/react-vite';
import { fn } from 'storybook/test';

import { ShowDeletedFilter } from './ShowDeletedFilter';

const meta = {
  title: 'Features/Users/ShowDeletedFilter',
  component: ShowDeletedFilter,
  tags: ['autodocs'],
  parameters: {
    layout: 'centered',
  },
  args: {
    onToggle: fn(),
  },
} satisfies Meta<typeof ShowDeletedFilter>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Unchecked: Story = {
  args: {
    checked: false,
  },
};

export const Checked: Story = {
  args: {
    checked: true,
  },
};

export const AssetsLabel: Story = {
  args: {
    checked: false,
    labelKey: 'assets.filters.showDeleted',
  },
};
