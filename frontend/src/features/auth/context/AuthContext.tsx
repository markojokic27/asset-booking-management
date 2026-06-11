// External packages
import * as React from 'react';

// API
import api, { setAccessToken } from '../../../shared/api';

// Types
import type { UserDto } from '../../user/types';

type AuthContextType = {
  user: UserDto | null;
  isLoading: boolean;
  isAuthenticated: boolean;

  login: (accessToken: string) => Promise<void>;
  logout: () => Promise<void>;

  refreshUser: () => Promise<void>;
};

const AuthContext = React.createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = React.useState<UserDto | null>(null);

  const [isLoading, setIsLoading] = React.useState(true);

  const refreshUser = React.useCallback(async () => {
    const response = await api.get('/auth/me');

    setUser(response.data);
  }, []);

  const login = React.useCallback(
    async (accessToken: string) => {
      setAccessToken(accessToken);

      await refreshUser();
    },
    [refreshUser]
  );

  const logout = React.useCallback(async () => {
    try {
      await api.post('/auth/logout');
    } finally {
      setAccessToken(null);
      setUser(null);
    }
  }, []);

  React.useEffect(() => {
    const initialize = async () => {
      try {
        const refreshResponse = await api.post('/auth/refresh');

        setAccessToken(refreshResponse.data.accessToken);

        await refreshUser();
      } catch {
        setUser(null);
      } finally {
        setIsLoading(false);
      }
    };

    initialize();
  }, [refreshUser]);

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        isAuthenticated: !!user,
        login,
        logout,
        refreshUser,
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
