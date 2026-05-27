import type { Meta, StoryObj } from '@storybook/react-vite';
import { fn } from 'storybook/test';

import type { UserModalUser } from '../types';
import { UserModal } from './UserModal';

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
type Story = StoryObj<typeof meta>;

export const Active: Story = {
  args: {
    user: { ...baseUser, status: 'ACTIVE' },
  },
};

export const Inactive: Story = {
  args: {
    user: { ...baseUser, status: 'INACTIVE' },
  },
};

export const Deleted: Story = {
  args: {
    user: { ...baseUser, status: 'DELETED', notes: null },
  },
};

export const Closed: Story = {
  args: {
    isOpen: false,
  },
};
