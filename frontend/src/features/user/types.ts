export type User = {
  id: number;
  username: string;
  surname: string;
  name: string;
  email: string;
  password: string;
  role: 'EMPLOYEE' | 'ADMIN' | 'MANAGER';
  status: 'ACTIVE' | 'INACTIVE' | 'STUDENT' | 'LEFT_COMPANY' | 'DELETED';
  departmentId: number;
  managerEmail: string;
  notes?: string | null;
  benefit?: 'ALL' | 'REC_PARK' | null;
};

// type for the user role
export type UserRole = User['role'];

// Shape returned by backend
export type UserDto = Omit<User, 'password'>;

// Shape expected by UserModal component (name is combined from surname and name)
export type UserModalUser = {
  id: UserDto['id'];
  name: string;
} & Pick<
  UserDto,
  | 'email'
  | 'username'
  | 'role'
  | 'status'
  | 'departmentId'
  | 'managerEmail'
  | 'notes'
>;

// Shape expected by UserBookingsModal
export type UserBookingsModalUser = {
  id: UserDto['id'];
  fullName: string;
};

// Shape expected by backend for POST /users
export type UserUpsertRequest = Omit<User, 'id'>;

// PATCH /users/{id} 
export type UserUpdateRequest = Omit<User, 'id' | 'username' | 'password'>;
