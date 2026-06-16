import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

import { getUserIdFromAccessToken } from '../../../shared/jwt';
import { getUserById } from '../api/users';
import type { UserDto } from '../types';


// TODO: delete this, not in use
export function useCurrentUser() {
  const { t } = useTranslation();
  const [user, setUser] = useState<UserDto | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        setIsLoading(true);
        setError(null);

        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) throw new Error('Missing accessToken');

        const userId = getUserIdFromAccessToken(accessToken);
        if (userId === null) throw new Error('Missing userId in accessToken');

        const me = await getUserById(userId);
        setUser(me);
      } catch {
        setError(t('account.error'));
      } finally {
        setIsLoading(false);
      }
    };

    fetchUser();
  }, [t]);

  return { user, isLoading, error };
}
