import { createBrowserRouter, redirect } from 'react-router-dom';
import App from './App';

import Home from '../pages/Home';
import Assets from '../pages/Assets';
import Bookings from '../pages/Bookings';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Manager from '../pages/Manager';
import NotFound from '../pages/NotFound';
import Users from '../pages/Users';
import AssetCategories from '../pages/AssetCategories';


/*  auth loader - in  app, we will check auth status by making an API call to the backend
async function requireAuth() {
  const res = await fetch('http://localhost:8080/api/me', {
    credentials: 'include',
  });
  if (res.status === 401) {
    throw redirect('/login');
  }
  return null;
} */

// For now, we will check auth status by looking for a cookie
async function requireAuth() {
  const isAuth = document.cookie.includes('auth=true');
  if (!isAuth) {
    throw redirect('/login');
  }
  return null;
}

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <Home />,
        loader: requireAuth,
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
