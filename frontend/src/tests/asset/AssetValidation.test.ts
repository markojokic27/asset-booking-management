import { createAssetValidationSchema } from '../../features/asset/validation';
import { describe, test, expect } from 'vitest';
import { testT } from '../testI18n';

const assetValidationSchema = createAssetValidationSchema(testT);

const validAsset = {
    name: "Hp 15",
    categoryId: 1,
    description: "Laptop located in room 301",
    status: "ACTIVE",
    location: "Room 301",
} as const;

describe("Asset schema validation", () => {
    test("should pass with valid data", () => {
        const result = assetValidationSchema.safeParse(validAsset);

        expect(result.success).toBe(true);

    });

    describe("Name", () => {
        test("should fail when name is empty", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                name: "",
            });

            expect(result.success).toBe(false);

        });

        test("should fail when name too long", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                name: "a".repeat(101),
            });

            expect(result.success).toBe(false);

        });

    })

    describe("CategoryId", () => {
        test("should fail when categoryId is null", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                categoryId: null,
            });

            expect(result.success).toBe(false);

        });

    })


    describe("Descirption", () => {
        test("should pass when description is null", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                description: "",
            });

            expect(result.success).toBe(true);

        });

        test("should fail when name too long", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                name: "a".repeat(101),
                description: "L".repeat(256),
            });

            expect(result.success).toBe(false);

        });

    })

    describe("Status", () => {
        test("should fail when status is null", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                status: null,
            });

            expect(result.success).toBe(false);

        });

    })

    describe("Location", () => {
        test("should fail when location is empty", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                location: "",
            });

            expect(result.success).toBe(false);

        });

        test("should fail when location too long", () => {
            const result = assetValidationSchema.safeParse({
                ...validAsset,
                location: "R".repeat(256),
            });

            expect(result.success).toBe(false);

        });

    })
})
