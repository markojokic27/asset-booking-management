export type UserRole = 'EMPLOYEE' | 'ADMIN' | 'MANAGER';

export type UserStatus = 'ACTIVE' | 'INACTIVE';

export type UserBenefit = 'ALL' | 'REC_PARK';

export type User = {
  id: string;
  username: string;
  surname: string;
  name: string;
  email: string;
  password: string;
  role: UserRole;
  status: UserStatus;
  notes?: string;
  department: string;  // FK na Department
  managerEail: string;
  benefit?: UserBenefit;
  createdAt: Date;
  lastModifiedAt: Date;
};

export type UserDto = Omit<User, 'password'> & {
  isActive: boolean;
  hasBenefit: boolean;
  managerName?: string;
};
