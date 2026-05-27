import { describe, it, expect } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { useUserModals } from '../../features/user/hooks/useUserModals';

const mockUser = { id: 1, name: 'John' } as any;

// Runs the hook and returns result
const setup = () => renderHook(() => useUserModals()).result;

// Runs the hook, opens a modal and returns result
const setupWithOpen = (type: Parameters<ReturnType<typeof useUserModals>['open']>[0], user = mockUser) => {
  const result = setup();
  act(() => result.current.open(type, user));
  return result;
};

describe('useUserModals', () => {

  it('starts with modal null and no active user', () => {
    const result = setup();
    expect(result.current.modal).toBeNull();
    expect(result.current.activeUser).toBeNull();
  });


  it('open sets modal type and active user', () => {
    const result = setupWithOpen('view');
    expect(result.current.modal).toBe('view');
    expect(result.current.activeUser).toBe(mockUser);
  });

  it('open without user sets activeUser to null', () => {
    const result = setup();
    act(() => result.current.open('create'));
    expect(result.current.modal).toBe('create');
    expect(result.current.activeUser).toBeNull();
  });

  it.each(['view', 'edit', 'create', 'bookings', 'report'] as const)(
    'open works for modal type "%s"',
    (type) => {
      const result = setupWithOpen(type);
      expect(result.current.modal).toBe(type);
    }
  );

  it('open replaces previously opened modal', () => {
    const result = setupWithOpen('view');
    act(() => result.current.open('edit', mockUser));
    expect(result.current.modal).toBe('edit');
  });


  it('close resets modal and activeUser to null', () => {
    const result = setupWithOpen('view');
    act(() => result.current.close());
    expect(result.current.modal).toBeNull();
    expect(result.current.activeUser).toBeNull();
  });

  it('close works even if no modal is open', () => {
    const result = setup();
    act(() => result.current.close());
    expect(result.current.modal).toBeNull();
    expect(result.current.activeUser).toBeNull();
  });

});