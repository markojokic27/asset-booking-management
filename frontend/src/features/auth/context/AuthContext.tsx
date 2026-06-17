import * as React from 'react';
import api, { setAccessToken, getAccessToken } from '../../../shared/api';

import type { UserDto } from '../../user/types';
import { decodeJwtPayload } from '../../../shared/jwt';

type AuthContextType = {
  user: UserDto | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  error: string | null;
  login: (accessToken: string, refreshToken: string) => Promise<void>;
  logout: () => Promise<void>;
};

function userFromToken(token: string): UserDto | null {
  try {
    const payload = decodeJwtPayload(token) as any;

    let parsedRole = '';
    if (Array.isArray(payload.roles) && payload.roles.length > 0) {
      parsedRole = payload.roles[0];
    } else if (typeof payload.roles === 'string') {
      parsedRole = payload.roles;
    }

    const userRole =
      parsedRole === 'ROLE_ADMIN'
        ? 'ADMIN'
        : parsedRole === 'ROLE_MANAGER'
          ? 'MANAGER'
          : 'EMPLOYEE';

    return {
      id: payload.userId ?? payload.sub ?? '',
      username: payload.sub ?? '',
      email: payload.email ?? '',
      role: userRole,
      name: payload.name ?? '',
      surname: payload.surname ?? '',
      status: 'ACTIVE',
      departmentId: 0,
      managerEmail: '',
      benefit: payload.benefit ?? '',
    };
  } catch {
    return null;
  }
}

const AuthContext = React.createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = React.useState<UserDto | null>(null);
  const [isLoading, setIsLoading] = React.useState(true);
  const [error, setError] = React.useState<string | null>(null);

  const syncUserFromToken = React.useCallback(() => {
    const token = getAccessToken();
    if (!token) {
      setUser(null);
      return;
    }

    setUser(userFromToken(token));
  }, []);

  // TODO: when BE add endpoint....
  // const refreshUser = React.useCallback(async () => {
  //   try {
  //     setError(null);
  //     const response = await api.get('/auth/me');
  //     setUser(response.data);
  //   } catch {
  //     setError('Greška pri dohvaćanju korisnika');
  //     throw new Error();
  //   }
  // }, []);

  const login = React.useCallback(
    async (accessToken: string, refreshToken: string) => {
      try {
        setError(null);
        setAccessToken(accessToken);
        localStorage.setItem('refreshToken', refreshToken);

        syncUserFromToken();
      } catch {
        setError('Greška pri prijavi');
        throw new Error();
      }
    },
    [syncUserFromToken]
  );

  const logout = React.useCallback(async () => {
    setAccessToken(null);
    localStorage.removeItem('refreshToken');
    setUser(null);
    setError(null);
  }, []);

  React.useEffect(() => {
    const initialize = async () => {
      try {
        const refreshToken = localStorage.getItem('refreshToken');

        if (!refreshToken) {
          setIsLoading(false);
          return;
        }

        const response = await api.post('/auth/refresh', {
          refreshToken,
        });

        setAccessToken(response.data.accessToken);

        syncUserFromToken();
      } catch {
        localStorage.removeItem('refreshToken');

        setAccessToken(null);
        setUser(null);
        setError('Sesija je istekla');
      } finally {
        setIsLoading(false);
      }
    };

    initialize();
  }, [syncUserFromToken]);

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        isAuthenticated: !!user,
        error,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = React.useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used inside AuthProvider');
  }

  return context;
}
