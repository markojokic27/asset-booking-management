import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router/dom';
import { router } from './router';
import { AuthProvider } from '../features/auth/context/AuthContext.tsx';
import { ThemeProvider } from './ThemeProvider';
import '../styles/index.css';
import '../config/i18n.ts';
import { ToastProvider } from '../components/ui/ToastProvider.tsx';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ThemeProvider>
      <AuthProvider>
        <ToastProvider />
        <RouterProvider router={router} />
      </AuthProvider>
    </ThemeProvider>
  </StrictMode>
);
