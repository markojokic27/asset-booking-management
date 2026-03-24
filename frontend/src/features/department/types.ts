export type Department = {
  id: string;

  name: string;

  managerId: string; // FK na User

  createdDate: Date;
  lastModifiedDate: Date;
};

export type DepartmentDto = Department & {
  //managerName?: string;
};