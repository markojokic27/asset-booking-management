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
