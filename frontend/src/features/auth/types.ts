import type { UserDto } from '../user/types';

export type LoginFormValues = {
  email: string;
  password: string;
};

export type AuthResponse = {
  token: string;
  user: UserDto;
};
