export type User = {
  id: number;
  username: string;
  surname: string;
  name: string;
  email: string;
  password: string;
  role: 'EMPLOYEE' | 'ADMIN' | 'MANAGER';
  status: 'ACTIVE' | 'INACTIVE';
  departmentId: number;
  managerEmail: string;
  notes?: string | null;
  benefit?: 'ALL' | 'REC_PARK' | null;
};

// Shape returned by backend
export type UserDto = Omit<User, 'password'>;
