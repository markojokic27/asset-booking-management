import type { Meta, StoryObj } from '@storybook/react-vite';
import { fn } from 'storybook/test';

import type { UserBookingsModalUser } from '../types';
import { UserBookingsModal } from './UserBookingsModal';

// Mock data used across stories
const user: UserBookingsModalUser = {
  id: 401,
  fullName: 'Anić Ana',
};

// Main Storybook configuration for this component
const meta = {
  title: 'Features/Users/UserBookingsModal',
  component: UserBookingsModal,
  tags: ['autodocs'],
  args: {
    isOpen: true,
    onClose: fn(),
    user,
  },
} satisfies Meta<typeof UserBookingsModal>;

export default meta;

// Reusable type for story objects
type Story = StoryObj<typeof meta>;

// Story for the open modal
export const Open: Story = {};

// Story for the closed modal
export const Closed: Story = {
  args: {
    isOpen: false,
  },
};
