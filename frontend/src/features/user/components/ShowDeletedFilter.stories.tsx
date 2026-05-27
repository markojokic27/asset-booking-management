import type { Meta, StoryObj } from '@storybook/react-vite';
import { fn } from 'storybook/test';

import { ShowDeletedFilter } from './ShowDeletedFilter';

// Main Storybook configuration for this component
const meta = {
  // Sidebar path in Storybook UI
  title: 'Features/Users/ShowDeletedFilter',

  // React component being rendered
  component: ShowDeletedFilter,

  // Enable automatic docs generation
  tags: ['autodocs'],

  // Storybook preview layout settings
  parameters: {
    layout: 'centered',
  },

  // Default props shared across all stories
  args: {
    onToggle: fn(),
  },
} satisfies Meta<typeof ShowDeletedFilter>;

export default meta;

// Reusable type for story objects
type Story = StoryObj<typeof meta>;

// Story for the unchecked state
export const Unchecked: Story = {
  args: {
    checked: false,
  },
};

// Story for the checked state
export const Checked: Story = {
  args: {
    checked: true,
  },
};
