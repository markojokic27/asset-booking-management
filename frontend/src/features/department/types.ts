export type Department = {
  id: string;
  name: string;
  managerId: number;       // FK na User
  createdAt: Date;
  lastModifiedAt: Date;
};

export type DepartmentDto = Department & {
  managerName?: string;    // opcionalno, samo za prikaz
};
