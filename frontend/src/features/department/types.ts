export type DepartmentName =
  | 'ADVANCE_TECHNOLOGY'
  | 'SECURE_SERVICES'
  | 'ARCHITECTURE'
  | 'FINANCE_AND_BUSINESS_ADMINISTRATION'
  | 'MOBILE_AND_SECURITY'
  | 'SYSTEM_TEST'
  | 'HUMAN_RESOURCES'
  | 'CLOUD_AND_DATA_MANAGEMENT'
  | 'DEVOPS';

export type DepartmentDto = {
  id: number;
  name: DepartmentName;
  managerId?: number | null;
};
