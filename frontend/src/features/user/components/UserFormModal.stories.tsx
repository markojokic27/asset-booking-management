import type { Meta, StoryObj } from '@storybook/react-vite';
import { fn } from 'storybook/test';

import type { UserDto } from '../types';
import { UserFormModal } from './UserFormModal';

// Mock actions used across stories
const onCreateAction = fn();
const onSaveAction = fn();

// Mock data used across stories
const editUser: UserDto = {
  id: 301,
  username: 'aanic',
  name: 'Ana',
  surname: 'Anić',
  email: 'ana.anic@example.com',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
  departmentId: 1,
  managerEmail: 'manager@example.com',
  notes: 'Prefers morning bookings',
  benefit: 'ALL',
};

// Main Storybook configuration for this component
const meta = {
  title: 'Features/Users/UserFormModal',
  component: UserFormModal,
  tags: ['autodocs'],
  args: {
    isOpen: true,
    onClose: fn(),
    onCreate: async (payload) => {
      onCreateAction(payload);
    },
    onSave: async (user) => {
      onSaveAction(user);
    },
  },
} satisfies Meta<typeof UserFormModal>;

export default meta;

// Reusable type for story objects
type Story = StoryObj<typeof meta>;

// Story for the create mode
export const Create: Story = {
  args: {
    mode: 'create',
    user: null,
  },
};


// Story for the edit mode
export const Edit: Story = {
  args: {
    mode: 'edit',
    user: editUser,
  },
};

// Story for the closed modal
export const Closed: Story = {
  args: {
    isOpen: false,
    mode: 'create',
    user: null,
  },
};
