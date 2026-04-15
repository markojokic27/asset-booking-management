import { createBrowserRouter, redirect } from 'react-router-dom';
import App from './App';
import axios from 'axios';
import { setAccessToken } from '../features/auth/api/login';

import Assets from '../pages/Assets';
import Bookings from '../pages/Bookings';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Manager from '../pages/Manager';
import NotFound from '../pages/NotFound';
import Users from '../pages/Users';
import AssetCategories from '../pages/AssetCategories';

export async function requireAuth() {
  const refreshToken = localStorage.getItem('refreshToken');

  if (!refreshToken) {
    throw redirect('/login');
  }

  try {
    const response = await axios.post('http://127.0.0.1:8080/v1/auth/refresh', {
      refreshToken,
    });
    console.log('Token refreshed successfully:', response.data);

    const newAccessToken = response.data.accessToken;

    setAccessToken(newAccessToken);

    return null;
  } catch (error) {
    localStorage.clear();
    throw redirect('/login');
  }
}
export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        loader: () => redirect('/assets'),
      },
      {
        path: 'manager',
        element: <Manager />,
        loader: requireAuth,
      },
      {
        path: 'assets',
        element: <Assets />,
        loader: requireAuth,
      },
      {
        path: 'bookings',
        element: <Bookings />,
        loader: requireAuth,
      },
      {
        path: 'users',
        element: <Users />,
        loader: requireAuth,
      },
      {
        path: 'categories',
        element: <AssetCategories />,
        loader: requireAuth,
      },
    ],
  },
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/register',
    element: <Register />,
  },
  {
    path: '*',
    element: <NotFound />,
  },
]);
