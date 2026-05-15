import axios from 'axios';

const api = axios.create({
  baseURL: 'http://127.0.0.1:8080/v1',
});

let accessToken: string | null = localStorage.getItem('accessToken');

export const setAccessToken = (token: string | null) => {
  accessToken = token;

  if (token) {
    localStorage.setItem('accessToken', token);
  } else {
    localStorage.removeItem('accessToken');
  }
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

const AUTH_ENDPOINTS_WITHOUT_REFRESH = [
  '/auth/login',
  '/auth/register',
  '/auth/refresh',
];

const isAuthEndpointWithoutRefresh = (url?: string) =>
  AUTH_ENDPOINTS_WITHOUT_REFRESH.some((endpoint) => url?.includes(endpoint));

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
      isAuthEndpointWithoutRefresh(originalRequest?.url)
    ) {
      return Promise.reject(error);
    }

    if (originalRequest._retry) {
      logout();
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    // if there's already a refresh in progress, wait for it to finish and then retry the original request
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
        logout();
        return Promise.reject(error);
      }

      const response = await axios.post(
        'http://127.0.0.1:8080/v1/auth/refresh',
        {
          refreshToken,
        }
      );

      const newAccessToken = response.data.accessToken;

      setAccessToken(newAccessToken);

      notify(newAccessToken);

      originalRequest.headers = originalRequest.headers || {};
      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

      return api(originalRequest);
    } catch (refreshError) {
      logout();
      return Promise.reject(refreshError);
    } finally {
      isRefreshing = false;
    }
  }
);

export const logout = () => {
  setAccessToken(null);

  localStorage.removeItem('refreshToken');

  window.location.href = '/login';
};

export default api;
