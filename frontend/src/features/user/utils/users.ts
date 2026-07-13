// utils/users.ts
import type { UserDto, UserUpdateRequest } from '../types';

export function getFullName(user: Pick<UserDto, 'name' | 'surname'>) {
  return `${user.name} ${user.surname}`.trim();
}

export function getDisplayName(user: Pick<UserDto, 'name' | 'surname'>) {
  return `${user.surname} ${user.name}`.trim();
}

export const mapUserDtoToUpdateRequest = (u: UserDto): UserUpdateRequest => ({
  surname: u.surname,
  name: u.name,
  email: u.email,
  role: u.role,
  status: u.status,
  departmentId: u.departmentId,
  managerEmail: u.managerEmail,
  notes: u.notes ?? '',
  benefit: u.benefit ?? 'ALL',
});

//TODO: delete all bellow
// function to check if the user is an admin
export function isAdmin(
  user: Pick<UserDto, 'role'> | null | undefined
): boolean {
  return user?.role === 'ADMIN';
}

// function to check if the user is a manager
export function isManager(
  user: Pick<UserDto, 'role'> | null | undefined
): boolean {
  return user?.role === 'MANAGER';
}

// function to check if the user is an employee
export function isEmployee(
  user: Pick<UserDto, 'role'> | null | undefined
): boolean {
  return user?.role === 'EMPLOYEE';
}
