import axios from 'axios';
import { setAccessToken } from './api';

export async function initAuth() {
  const refreshToken = localStorage.getItem('refreshToken');

  if (!refreshToken) {
    return false;
  }

  try {
    const response = await axios.post('http://127.0.0.1:8080/v1/auth/refresh', {
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
