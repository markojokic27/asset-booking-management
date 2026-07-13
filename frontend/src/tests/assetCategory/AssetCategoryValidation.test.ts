import { createAssetCategoryValidationSchema } from '../../features/asset-category/validation';
import { describe, test, expect } from 'vitest';
import { testT } from '../testI18n';

const assetCategoryValidationSchema = createAssetCategoryValidationSchema(testT);

describe("Asset category schema validation", () => {
    test("should pass with valid data", () => {
        const result = assetCategoryValidationSchema.safeParse({
            name: "Books",
            description: "A collection of books available for borrowing within the company library.",
            bookingPeriod: 'DAY',
            approval: true
        });

        expect(result.success).toBe(true);

    });

    describe("Name validation", () => {
        test("should fail when name is empty", () => {
            const result = assetCategoryValidationSchema.safeParse({
                name: "",
                description: "A collection of books available for borrowing within the company library.",
                bookingPeriod: 'DAY',
                approval: true
            });

            expect(result.success).toBe(false);

        });

        test("should fail when name too long", () => {
            const result = assetCategoryValidationSchema.safeParse({
                name: "b".repeat(101),
                description: "A collection of books available for borrowing within the company library.",
                bookingPeriod: 'DAY',
                approval: true
            });

            expect(result.success).toBe(false);

        });

    })

    describe("Description validation", () => {
        test("should pass when description is empty", () => {
            const result = assetCategoryValidationSchema.safeParse({
                name: "Books",
                description: "",
                bookingPeriod: 'DAY',
                approval: true
            });

            expect(result.success).toBe(true);

        });

        test("should fail when name too long", () => {
            const result = assetCategoryValidationSchema.safeParse({
                name: "Books",
                description: "b".repeat(256),
                bookingPeriod: 'DAY',
                approval: true
            });

            expect(result.success).toBe(false);

        });

    })


    describe("BookingPeriod validation", () => {
        test("should fail when bookingPeriod is null", () => {
            const result = assetCategoryValidationSchema.safeParse({
                name: "Books",
                description: "A collection of books available for borrowing within the company library.",
                bookingPeriod: null,
                approval: true
            });

            expect(result.success).toBe(false);

        });

        test("should fail when bookingPeriod is not a valid enum value", () => {
            const result = assetCategoryValidationSchema.safeParse({
                name: "Books",
                description: "A collection of books available for borrowing within the company library.",
                bookingPeriod: 1,
                approval: true
            });

            expect(result.success).toBe(false);

        });

    })

    describe("Approval validation", () => {
        test("should fail when approval is null", () => {
            const result = assetCategoryValidationSchema.safeParse({
                name: "Books",
                description: "A collection of books available for borrowing within the company library.",
                bookingPeriod: 'DAY',
                approval: null
            });

            expect(result.success).toBe(false);

        });

    })
})
