import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '/v1';

const api = axios.create({
  baseURL: BASE_URL,
});

let accessToken: string | null = null;

export const getAccessToken = () => accessToken;

export const setAccessToken = (token: string | null) => {
  accessToken = token;
};

let isRefreshing = false;
let subscribers: ((token: string) => void)[] = [];

const subscribe = (cb: (token: string) => void) => {
  subscribers.push(cb);
};

const notify = (token: string) => {
  subscribers.forEach((cb) => cb(token));
  subscribers = [];
};

api.interceptors.request.use((config) => {
  if (accessToken) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const originalRequest = error.config;

    if (
      error.response?.status !== 401 ||
      originalRequest._retry ||
      originalRequest.url?.includes('/auth/login') ||
      originalRequest.url?.includes('/auth/register') ||
      originalRequest.url?.includes('/auth/refresh')
    ) {
      throw error;
    }

    originalRequest._retry = true;

    if (isRefreshing) {
      return new Promise((resolve) => {
        subscribe((token) => {
          originalRequest.headers = originalRequest.headers || {};
          originalRequest.headers.Authorization = `Bearer ${token}`;

          resolve(api(originalRequest));
        });
      });
    }

    isRefreshing = true;

    try {
      const refreshToken = localStorage.getItem('refreshToken');

      if (!refreshToken) {
        throw error;
      }

      const response = await axios.post(`${BASE_URL}/auth/refresh`, {
        refreshToken,
      });

      const newAccessToken = response.data.accessToken;

      setAccessToken(newAccessToken);
      notify(newAccessToken);

      originalRequest.headers = originalRequest.headers || {};
      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

      return api(originalRequest);
    } finally {
      isRefreshing = false;
    }
  }
);

export default api;