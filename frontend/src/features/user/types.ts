export type Department =
  | 'ADVANCED_TECHNOLOGIES'
  | 'ARCHITECTURE'
  | 'CLOUD_DATA_MANAGEMENT'
  | 'DEVOPS'
  | 'FINANCE'
  | 'HR'
  | 'MOBILE_SECURITY'
  | 'OPERATIONS'
  | 'SECURITY_SYSTEMS';

  export type UserRole = 'employee' | 'manager' | 'admin';

  export type UserBenefit = 'parking';

  export type User = {
  id: string;
  surname: string;
  name: string;
  email: string;
  password: string;
  role: UserRole;             
  status: string;           // npr. 'active' | 'inactive' | 'banned' (treba definirati)
  notes?: string;
  department: Department; 
  manager_email?: string;
  benefit?: User;
  createdAt: Date;
  lastModifiedAt: Date;
};

export type UserDto = Omit<User, 'password'> & {
  isActive: boolean;
  hasBenefit: boolean;
  managerName?: string;
};
