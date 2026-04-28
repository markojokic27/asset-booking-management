// utils/users.ts
import type { UserDto, UserUpsertRequest } from '../types';

export function getFullName(user: Pick<UserDto, 'name' | 'surname'>) {
  return `${user.name} ${user.surname}`.trim();
}

export function getDisplayName(user: Pick<UserDto, 'name' | 'surname'>) {
  return `${user.surname} ${user.name}`.trim();
}

export const mapUserToRequest = (u: UserDto): UserUpsertRequest => ({
  username: u.username,
  surname: u.surname,
  name: u.name,
  email: u.email,
  password: '********',
  role: u.role,
  status: u.status,
  departmentId: u.departmentId,
  managerEmail: u.managerEmail,
  notes: u.notes ?? '',
  benefit: u.benefit ?? 'ALL',
});