export type Department = {
  id: string;
  name: string;
  managerId: string;       // FK na User
  createdAt: Date;
  lastModifiedAt: Date;
};

export type DepartmentDto = Department & {
  managerName?: string;    // opcionalno, samo za prikaz
};
