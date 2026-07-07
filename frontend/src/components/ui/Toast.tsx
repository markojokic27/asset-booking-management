import { toast, type ToastOptions } from 'react-toastify';

const options: ToastOptions = {
  position: 'bottom-right',
  autoClose: 3000,
  theme: 'colored',
};

export const Toast = {
  success(message: string) {
    toast.success(message, options);
  },

  error(message: string) {
    toast.error(message, options);
  },

  info(message: string) {
    toast.info(message, options);
  },

  warning(message: string) {
    toast.warning(message, options);
  },
};
