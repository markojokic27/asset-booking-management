import axios from 'axios';
import { setAccessToken } from './api';

export async function initAuth() {
  const refreshToken = localStorage.getItem('refreshToken');

  if (!refreshToken) return null;

  try {
    const res = await axios.post('http://127.0.0.1:8080/v1/auth/refresh', {
      refreshToken,
    });

    setAccessToken(res.data.accessToken);
    return true;
  } catch {
    localStorage.clear();
    return null;
  }
}
