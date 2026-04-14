import axios from 'axios';

const api = axios.create({
  baseURL: 'http://127.0.0.1:8080/v1',
});

let accessToken: string | null = null;

export const setAccessToken = (token: string | null) => {
  accessToken = token;
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

    if (
      error.response?.status === 401 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');

        const response = await axios.post(
          'http://127.0.0.1:8080/v1/auth/refresh',
          { refreshToken }
        );

        const newAccessToken = response.data.accessToken;

        setAccessToken(newAccessToken);

        return api(originalRequest);
      } catch (err) {
        localStorage.clear();
        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  }
);

export default api;