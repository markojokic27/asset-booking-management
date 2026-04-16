import { createBrowserRouter, redirect } from 'react-router-dom';
import App from './App';
import { initAuth } from '../shared/auth';
import ProtectedLayout from './ProtectedLayout';

import Assets from '../pages/Assets';
import Bookings from '../pages/Bookings';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Manager from '../pages/Manager';
import NotFound from '../pages/NotFound';
import Users from '../pages/Users';
import AssetCategories from '../pages/AssetCategories';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    loader: initAuth,
    children: [
      {
        index: true,
        loader: () => redirect('/assets'),
      },

      {
        element: <ProtectedLayout />,
        children: [
          { path: 'assets', element: <Assets /> },
          { path: 'bookings', element: <Bookings /> },
          { path: 'users', element: <Users /> },
          { path: 'categories', element: <AssetCategories /> },
          { path: 'manager', element: <Manager /> },
        ],
      },
    ],
  },

  { path: '/login', element: <Login /> },
  { path: '/register', element: <Register /> },
  { path: '*', element: <NotFound /> },
]);
