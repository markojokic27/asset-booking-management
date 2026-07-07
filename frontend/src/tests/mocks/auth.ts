import { vi } from 'vitest';
import type { UserDto } from '../../features/user/types';

export function authState(
  overrides: {
    user?: UserDto | null;
    isLoading?: boolean;
    isAuthenticated?: boolean;
    error?: string | null;
  } = {},
) {
  const user = 'user' in overrides ? overrides.user : ({ id: 1, role: 'ADMIN' } as UserDto);

  return {
    user: user ?? null,
    isLoading: overrides.isLoading ?? false,
    isAuthenticated: overrides.isAuthenticated ?? !!user,
    error: overrides.error ?? null,
    login: vi.fn(),
    logout: vi.fn(),
  };
}

export const mockUseAuth = vi.fn(() => authState());

vi.mock('../../features/auth/context/AuthContext', () => ({
  useAuth: () => mockUseAuth(),
  AuthProvider: ({ children }: { children: React.ReactNode }) => children,
}));
