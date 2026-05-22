/* unit tests for user utils functions */

import { describe, it, expect } from 'vitest';
import {
  getDisplayName,
  getFullName,
  mapUserDtoToUpdateRequest,
} from '../../features/user/utilis/users';
import type { UserDto } from '../../features/user/types';

// base user dto for testing
const baseUserDto: UserDto = {
  id: 1,
  username: 'ivanivic',
  surname: 'Ivić',
  name: 'Ivan',
  email: 'ivanivic@maurer-electronics.hr',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
  departmentId: 5,
  managerEmail: 'antem@maurer-electronics.hr',
};

// test getFullName function
describe('getFullName', () => {
  it('returns name followed by surname', () => {
    expect(getFullName({ name: 'Ivan', surname: 'Ivić' })).toBe('Ivan Ivić');
  });

  it('trims leading and trailing whitespace on the full string', () => {
    expect(getFullName({ name: ' Ivan', surname: 'Ivić' })).toBe('Ivan Ivić');
  });

  it('returns only surname when name is empty', () => {
    expect(getFullName({ name: '', surname: 'Ivić' })).toBe('Ivić');
  });

  it('returns only name when surname is empty', () => {
    expect(getFullName({ name: 'Ivan', surname: '' })).toBe('Ivan');
  });

  it('returns empty string when both are empty', () => {
    expect(getFullName({ name: '', surname: '' })).toBe('');
  });
});

// test getDisplayName function
describe('getDisplayName', () => {
  it('returns surname followed by name', () => {
    expect(getDisplayName({ name: 'Ivan', surname: 'Ivić' })).toBe('Ivić Ivan');
  });

  it('trims leading and trailing whitespace on the full string', () => {
    expect(getDisplayName({ name: 'Ivan ', surname: ' Ivić' })).toBe('Ivić Ivan');
  });

  it('returns only name when surname is empty', () => {
    expect(getDisplayName({ name: 'Ivan', surname: '' })).toBe('Ivan');
  });

  it('returns only surname when name is empty', () => {
    expect(getDisplayName({ name: '', surname: 'Ivić' })).toBe('Ivić');
  });

  it('returns empty string when both are empty', () => {
    expect(getDisplayName({ name: '', surname: '' })).toBe('');
  });
});

// test mapUserDtoToUpdateRequest function
describe('mapUserDtoToUpdateRequest', () => {
  it('maps all fields from UserDto to UserUpdateRequest', () => {
    const dto: UserDto = {
      ...baseUserDto,
      notes: 'Some notes',
      benefit: 'REC_PARK',
    };

    expect(mapUserDtoToUpdateRequest(dto)).toEqual({
      surname: 'Ivić',
      name: 'Ivan',
      email: 'ivanivic@maurer-electronics.hr',
      role: 'EMPLOYEE',
      status: 'ACTIVE',
      departmentId: 5,
      managerEmail: 'antem@maurer-electronics.hr',
      notes: 'Some notes',
      benefit: 'REC_PARK',
    });
  });

  it('does not include id or username', () => {
    const result = mapUserDtoToUpdateRequest(baseUserDto);

    expect(result).not.toHaveProperty('id');
    expect(result).not.toHaveProperty('username');
    expect(result).not.toHaveProperty('password');
  });

  it('defaults notes to empty string when null or undefined', () => {
    expect(mapUserDtoToUpdateRequest({ ...baseUserDto, notes: null })).toMatchObject({
      notes: '',
    });
    expect(mapUserDtoToUpdateRequest({ ...baseUserDto, notes: undefined })).toMatchObject({
      notes: '',
    });
  });

  it('defaults benefit to ALL when null or undefined', () => {
    expect(mapUserDtoToUpdateRequest({ ...baseUserDto, benefit: null })).toMatchObject({
      benefit: 'ALL',
    });
    expect(mapUserDtoToUpdateRequest({ ...baseUserDto, benefit: undefined })).toMatchObject({
      benefit: 'ALL',
    });
  });
});
