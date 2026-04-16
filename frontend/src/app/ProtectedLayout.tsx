import { Navigate, Outlet } from 'react-router-dom';

const ProtectedLayout = () => {
  const refreshToken = localStorage.getItem('refreshToken');

  if (!refreshToken) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedLayout;
