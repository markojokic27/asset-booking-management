import type { Meta, StoryObj } from '@storybook/react-vite';
import { fn } from 'storybook/test';

import type { UserDto } from '../types';
import { UsersTable } from './UsersTable';

// Mock data used across stories
const users: UserDto[] = [
  {
    id: 101,
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
  },
  {
    id: 102,
    username: 'mhorvat',
    name: 'Marko',
    surname: 'Horvat',
    email: 'marko.horvat@example.com',
    role: 'MANAGER',
    status: 'INACTIVE',
    departmentId: 2,
    managerEmail: 'director@example.com',
    notes: null,
    benefit: 'REC_PARK',
  },
  {
    id: 103,
    username: 'ipavic',
    name: 'Iva',
    surname: 'Pavić',
    email: 'iva.pavic@example.com',
    role: 'ADMIN',
    status: 'DELETED',
    departmentId: 3,
    managerEmail: 'hr@example.com',
    notes: 'Former employee',
    benefit: null,
  },
];

// Main Storybook configuration for this component
const meta = {
  title: 'Features/Users/UsersTable',
  component: UsersTable,
  tags: ['autodocs'],
  args: {
    onToggleNameSort: fn(),
    onView: fn(),
    onEdit: fn(),
    onBookings: fn(),
    onDelete: fn(),
    onReport: fn(),
  },
} satisfies Meta<typeof UsersTable>;

export default meta;
type Story = StoryObj<typeof meta>;

// Story for the table with users
export const WithUsers: Story = {
  args: {
    data: users,
    nameSortDir: 'asc',
  },
};

// Story for the empty table
export const Empty: Story = {
  args: {
    data: [],
    nameSortDir: 'asc',
    emptyMessage: 'No users found.',
  },
};
