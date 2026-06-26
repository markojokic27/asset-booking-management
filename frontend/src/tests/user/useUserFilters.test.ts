/* unit tests for useUserFilters hook */

import { act, createElement } from 'react';
import { createRoot, type Root } from 'react-dom/client';
import { afterEach, describe, expect, it } from 'vitest';
import { useUserFilters } from '../../features/user/hooks/useUsersFilters';
import type { UserDto } from '../../features/user/types';

const baseUser: UserDto = {
  id: 0,
  username: 'user',
  surname: 'Prezime',
  name: 'Ime',
  email: 'user@example.com',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
  departmentId: 1,
  managerEmail: 'manager@example.com',
};

function user(overrides: Partial<UserDto> & Pick<UserDto, 'id'>): UserDto {
  return { ...baseUser, ...overrides };
}

const sampleUsers: UserDto[] = [
  user({ id: 1, name: 'Ana', surname: 'Babić', email: 'ana@example.com', departmentId: 1 }),
  user({ id: 2, name: 'Ivan', surname: 'Anić', email: 'ivan@example.com', role: 'ADMIN', departmentId: 2 }),
  user({ id: 3, name: 'Marko', surname: 'Babić', email: 'marko@example.com', role: 'MANAGER', departmentId: 1 }),
  user({
    id: 4,
    name: 'Obrisani',
    surname: 'Korisnik',
    email: 'deleted@example.com',
    status: 'DELETED',
    departmentId: 2,
  }),
];

type HookResult = ReturnType<typeof useUserFilters>;

function renderUserFilters(users: UserDto[]) {
  let latest!: HookResult;
  let currentUsers = users;
  const container = document.createElement('div');
  document.body.appendChild(container);
  const root: Root = createRoot(container);

  const render = () => {
    act(() => {
      root.render(
        createElement(function TestHarness() {
          latest = useUserFilters(currentUsers);
          return null;
        }),
      );
    });
  };

  render();

  return {
    get current() {
      return latest;
    },
    rerender(nextUsers: UserDto[]) {
      currentUsers = nextUsers;
      render();
    },
    unmount() {
      act(() => root.unmount());
      container.remove();
    },
  };
}

describe('useUserFilters', () => {
  let harness: ReturnType<typeof renderUserFilters>;

  afterEach(() => {
    harness?.unmount();
  });

  describe('showDeleted', () => {
    it('excludes DELETED users by default', () => {
      harness = renderUserFilters(sampleUsers);

      expect(harness.current.data.map((u) => u.id)).toEqual([2, 1, 3]);
      expect(harness.current.showDeleted).toBe(false);
    });

    it('includes DELETED users after toggleShowDeleted', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.toggleShowDeleted();
      });

      expect(harness.current.showDeleted).toBe(true);
      expect(harness.current.data.map((u) => u.id)).toEqual([2, 1, 3, 4]);
    });

    it('hides DELETED users again when toggled off', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.toggleShowDeleted();
        harness.current.toggleShowDeleted();
      });

      expect(harness.current.showDeleted).toBe(false);
      expect(harness.current.data.map((u) => u.id)).toEqual([2, 1, 3]);
    });
  });

  describe('search', () => {
    it('filters by name (case-insensitive)', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSearch('IVAN');
      });

      expect(harness.current.search).toBe('IVAN');
      expect(harness.current.data.map((u) => u.id)).toEqual([2]);
    });

    it('filters by surname', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSearch('babić');
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([1, 3]);
    });

    it('filters by email', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSearch('marko@');
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([3]);
    });

    it('trims whitespace and returns all when query is empty', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSearch('   ');
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([2, 1, 3]);
    });

    it('combines search with showDeleted', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.toggleShowDeleted();
        harness.current.setSearch('obrisani');
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([4]);
    });
  });

  describe('role', () => {
    it('returns all roles when no role is selected', () => {
      harness = renderUserFilters(sampleUsers);

      expect(harness.current.selectedRole).toBe('');
      expect(harness.current.data.map((u) => u.id)).toEqual([2, 1, 3]);
    });

    it('filters by EMPLOYEE role', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSelectedRole('EMPLOYEE');
      });

      expect(harness.current.selectedRole).toBe('EMPLOYEE');
      expect(harness.current.data.map((u) => u.id)).toEqual([1]);
    });

    it('filters by ADMIN role', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSelectedRole('ADMIN');
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([2]);
    });

    it('combines role filter with search', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSelectedRole('EMPLOYEE');
        harness.current.setSearch('ana');
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([1]);
    });
  });

  describe('department', () => {
    it('returns all departments when none is selected', () => {
      harness = renderUserFilters(sampleUsers);

      expect(harness.current.selectedDepartment).toBe('');
      expect(harness.current.data.map((u) => u.id)).toEqual([2, 1, 3]);
    });

    it('filters by selected department', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSelectedDepartment(1);
      });

      expect(harness.current.selectedDepartment).toBe(1);
      expect(harness.current.data.map((u) => u.id)).toEqual([1, 3]);
    });

    it('combines department filter with role', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSelectedDepartment(1);
        harness.current.setSelectedRole('MANAGER');
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([3]);
    });
  });

  describe('sort', () => {
    it('sorts ascending by surname, then name, then email', () => {
      harness = renderUserFilters(sampleUsers);

      expect(harness.current.sortDir).toBe('asc');
      expect(harness.current.data.map((u) => u.id)).toEqual([2, 1, 3]);
    });

    it('sorts descending after toggleSort', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.toggleSort();
      });

      expect(harness.current.sortDir).toBe('desc');
      expect(harness.current.data.map((u) => u.id)).toEqual([3, 1, 2]);
    });

    it('uses email as tie-breaker when surname and name match', () => {
      const users = [
        user({ id: 10, name: 'Ivo', surname: 'Horvat', email: 'z@example.com' }),
        user({ id: 11, name: 'Ivo', surname: 'Horvat', email: 'a@example.com' }),
      ];
      harness = renderUserFilters(users);

      expect(harness.current.data.map((u) => u.id)).toEqual([11, 10]);

      act(() => {
        harness.current.toggleSort();
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([10, 11]);
    });

    it('applies sort after search filtering', () => {
      harness = renderUserFilters(sampleUsers);

      act(() => {
        harness.current.setSearch('babić');
        harness.current.toggleSort();
      });

      expect(harness.current.data.map((u) => u.id)).toEqual([3, 1]);
    });
  });
});
