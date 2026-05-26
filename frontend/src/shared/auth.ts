import axios from 'axios';
import { API_BASE_URL, setAccessToken } from './api';

export async function initAuth() {
  const refreshToken = localStorage.getItem('refreshToken');

  if (!refreshToken) {
    return false;
  }

  try {
    const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
      refreshToken,
    });

    const accessToken = response.data.accessToken;
    setAccessToken(accessToken);

    return true;
  } catch {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    return false;
  }
}
