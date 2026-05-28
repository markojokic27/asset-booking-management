import axios from 'axios';
import { setAccessToken } from './api';
const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '/v1';

export async function initAuth() {
  const refreshToken = localStorage.getItem('refreshToken');

  if (!refreshToken) {
    return false;
  }

  try {
    const response = await axios.post(`${BASE_URL}/auth/refresh`, {
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
