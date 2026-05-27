import type { Meta, StoryObj } from '@storybook/react-vite';
import { fn } from 'storybook/test';

import type { UserModalUser } from '../types';
import { UserModal } from './UserModal';

// Mock data used across stories
const baseUser: UserModalUser = {
  id: 201,
  name: 'Anić Ana',
  email: 'ana.anic@example.com',
  username: 'aanic',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
  departmentId: 1,
  managerEmail: 'manager@example.com',
  notes: 'Prefers morning bookings',
};

// Main Storybook configuration for this component
const meta = {
  title: 'Features/Users/UserModal',
  component: UserModal,
  tags: ['autodocs'],
  args: {
    isOpen: true,
    onClose: fn(),
    user: baseUser,
  },
} satisfies Meta<typeof UserModal>;

export default meta;

// Reusable type for story objects
type Story = StoryObj<typeof meta>;

// Story for the active user
export const Active: Story = {
  args: {
    user: { ...baseUser, status: 'ACTIVE' },
  },
};

// Story for the inactive user
export const Inactive: Story = {
  args: {
    user: { ...baseUser, status: 'INACTIVE' },
  },
};

// Story for the deleted user
export const Deleted: Story = {
  args: {
    user: { ...baseUser, status: 'DELETED', notes: null },
  },
};

// Story for the closed modal
export const Closed: Story = {
  args: {
    isOpen: false,
  },
};
