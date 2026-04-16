import axios from 'axios';

const api = axios.create({
  baseURL: 'http://127.0.0.1:8080/v1',
});

let accessToken: string | null = null;

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
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
});

api.interceptors.response.use(
  (res) => res,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status !== 401) {
      return Promise.reject(error);
    }

    if (originalRequest._retry) {
      logout();
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    // ako već refresh ide
    if (isRefreshing) {
      return new Promise((resolve) => {
        subscribe((token) => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          resolve(api(originalRequest));
        });
      });
    }

    isRefreshing = true;

    try {
      const refreshToken = localStorage.getItem('refreshToken');

      const res = await axios.post('http://127.0.0.1:8080/v1/auth/refresh', {
        refreshToken,
      });

      const newToken = res.data.accessToken;

      setAccessToken(newToken);
      notify(newToken);

      return api(originalRequest);
    } catch (err) {
      logout();
      return Promise.reject(err);
    } finally {
      isRefreshing = false;
    }
  }
);

export const logout = () => {
  setAccessToken(null);
  localStorage.clear();
  window.location.href = '/login';
};

export default api;
