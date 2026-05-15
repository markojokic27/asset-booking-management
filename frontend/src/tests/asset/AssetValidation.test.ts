import { createAssetValidationSchema } from '../../features/asset/validation';
import { describe, test, expect } from 'vitest';
import { testT } from '../testI18n';

const assetValidationSchema = createAssetValidationSchema(testT);

describe("Asset schema validation", () => {
    test("should pass with valid data", () => {
        const result = assetValidationSchema.safeParse({
            name: "Hp 15",
            categoryId: 1,
            description: "Laptop located in room 301",
            code: "QR-LAPTOP-001",
            status: "ACTIVE",
            location: "Room 301"

        });

        expect(result.success).toBe(true);

    });

    describe("Name", () => {
        test("should fail when name is empty", () => {
            const result = assetValidationSchema.safeParse({
                name: "",
                categoryId: 1,
                description: "Laptop located in room 301",
                code: "QR-LAPTOP-001",
                status: "ACTIVE",
                location: "Room 301"

            });

            expect(result.success).toBe(false);

        });

        test("should fail when name too long", () => {
            const result = assetValidationSchema.safeParse({
                name: "a".repeat(101),
                categoryId: 1,
                description: "Laptop located in room 301",
                code: "QR-LAPTOP-001",
                status: "ACTIVE",
                location: "Room 301"

            });

            expect(result.success).toBe(false);

        });

    })

    describe("CategoryId", () => {
        test("should fail when categoryId is null", () => {
            const result = assetValidationSchema.safeParse({
                name: "Hp 15",
                categoryId: null,
                description: "Laptop located in room 301",
                code: "QR-LAPTOP-001",
                status: "ACTIVE",
                location: "Room 301"

            });

            expect(result.success).toBe(false);

        });

    })


    describe("Descirption", () => {
        test("should pass when description is null", () => {
            const result = assetValidationSchema.safeParse({
                name: "Hp 15",
                categoryId: 1,
                description: "",
                code: "QR-LAPTOP-001",
                status: "ACTIVE",
                location: "Room 301"

            });

            expect(result.success).toBe(true);

        });

        test("should fail when name too long", () => {
            const result = assetValidationSchema.safeParse({
                name: "a".repeat(101),
                categoryId: 1,
                description: "L".repeat(256),
                code: "QR-LAPTOP-001",
                status: "ACTIVE",
                location: "Room 301"

            });

            expect(result.success).toBe(false);

        });

    })


    describe("Code", () => {
        test("should fail when code is empty", () => {
            const result = assetValidationSchema.safeParse({
                name: "Hp 15",
                categoryId: 1,
                description: "Laptop located in room 301",
                code: "",
                status: "ACTIVE",
                location: "Room 301"

            });

            expect(result.success).toBe(false);

        });

        test("should fail when code too long", () => {
            const result = assetValidationSchema.safeParse({
                name: "a".repeat(101),
                categoryId: 1,
                description: "Laptop located in room 301",
                code: "Q".repeat(20001),
                status: "ACTIVE",
                location: "Room 301"

            });

            expect(result.success).toBe(false);

        });

    })

    describe("Status", () => {
        test("should fail when status is null", () => {
            const result = assetValidationSchema.safeParse({
                name: "Hp 15",
                categoryId: 1,
                description: "Laptop located in room 301",
                code: "QR-LAPTOP-001",
                status: null,
                location: "Room 301"

            });

            expect(result.success).toBe(false);

        });

    })

    describe("Location", () => {
        test("should fail when location is empty", () => {
            const result = assetValidationSchema.safeParse({
                name: "Hp 15",
                categoryId: 1,
                description: "Laptop located in room 301",
                code: "QR-LAPTOP-001",
                status: "ACTIVE",
                location: ""

            });

            expect(result.success).toBe(false);

        });

        test("should fail when location too long", () => {
            const result = assetValidationSchema.safeParse({
                name: "a".repeat(101),
                categoryId: 1,
                description: "Laptop located in room 301",
                code: "QR-LAPTOP-001",
                status: "ACTIVE",
                location: "R".repeat(256)

            });

            expect(result.success).toBe(false);

        });

    })
})

