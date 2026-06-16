import App from './App';
import ProtectedLayout from './ProtectedLayout';
import { createBrowserRouter, redirect } from 'react-router-dom';

import Assets from '../pages/Assets';
import Bookings from '../pages/Bookings';
import BookingsByAsset from '../pages/BookingByAsset';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Approvals from '../pages/Approvals';
import NotFound from '../pages/NotFound';
import Users from '../pages/Users';
import AssetCategories from '../pages/AssetCategories';
import AccountInfo from '../pages/AccountInfo';
import Report from '../pages/Report';
import MyBookings from '../pages/MyBookings';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
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
          { path: 'my-bookings', element: <MyBookings /> },
          { path: 'assets/:assetId/bookings', element: <BookingsByAsset /> },
          { path: 'users', element: <Users /> },
          { path: 'categories', element: <AssetCategories /> },
          { path: 'approvals', element: <Approvals /> },
          { path: 'approvals/:bookingId', element: <Approvals /> },
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
