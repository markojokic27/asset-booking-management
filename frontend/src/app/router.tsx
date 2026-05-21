import App from './App';
import ProtectedLayout from './ProtectedLayout';
import { createBrowserRouter, redirect } from 'react-router-dom';
import { initAuth } from '../shared/auth';

import Assets from '../pages/Assets';
import Bookings from '../pages/Bookings';
import BookingsByAsset from '../pages/BookingByAsset';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Manager from '../pages/Manager';
import NotFound from '../pages/NotFound';
import Users from '../pages/Users';
import AssetCategories from '../pages/AssetCategories';
import AccountInfo from '../pages/AccountInfo';
import Report from '../pages/Report';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    loader: initAuth,
    children: [
      {
        index: true,
        loader: () => redirect('/bookings'),
      },

      {
        element: <ProtectedLayout />,
        children: [
          { path: 'assets', element: <Assets /> },
          { path: 'bookings', element: <Bookings /> },
          { path: 'assets/:assetId/bookings', element: <BookingsByAsset /> },
          { path: 'users', element: <Users /> },
          { path: 'categories', element: <AssetCategories /> },
          { path: 'manager', element: <Manager /> },
          { path: 'account-info', element: <AccountInfo /> },
          { path: 'report', element: <Report /> },
        ],
      },
    ],
  },

  { path: '/login', element: <Login /> },
  { path: '/register', element: <Register /> },
  { path: '*', element: <NotFound /> },
]);
