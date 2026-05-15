import { createDepartmentValidationSchema } from '../../features/department/validation';
import { describe, test, expect } from 'vitest';
import { testT } from '../testI18n';

const departmentValidationSchema = createDepartmentValidationSchema(testT);


describe("Department schema validation", () => {
    test("should pass with valid data", () => {
        const result = departmentValidationSchema.safeParse({
            name: "DEVOPS",
            managerId: 1

        });

        expect(result.success).toBe(true);
    });
})

describe("Name", () => {
    test("should fail when name is null", () => {
        const result = departmentValidationSchema.safeParse({
            name: null,
            managerId: 1

        });

        expect(result.success).toBe(false);
    });
})

describe("ManagerId", () => {
    test("should fail when managerId is null", () => {
        const result = departmentValidationSchema.safeParse({
            name: "DEVOPS",
            managerId: null

        });

        expect(result.success).toBe(false);
    });
})