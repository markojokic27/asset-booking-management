// External packages
import { useState } from 'react';

// Types
import type { UserDto } from '../types';

type ModalType = 'view' | 'edit' | 'create' | 'bookings' | 'report' | null;

export function useUserModals() {
  const [activeUser, setActiveUser] = useState<UserDto | null>(null);
  const [modal, setModal] = useState<ModalType>(null);

  const open = (type: ModalType, user?: UserDto) => {
    setActiveUser(user ?? null);
    setModal(type);
  };

  const close = () => {
    setModal(null);
    setActiveUser(null);
  };

  return {
    activeUser,
    modal,
    open,
    close,
  };
}
