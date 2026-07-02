import { createBookingValidationSchema } from '../../features/booking/validation';
import { describe, test, expect } from 'vitest';
import { testT } from '../testI18n';

const bookingValidationSchema = createBookingValidationSchema(testT);

describe("Booking Schema Validation", () => {
    test("should pass with valid data", () => {
        const result = bookingValidationSchema.safeParse({
            userId: 2,
            assetId: 1,
            status: "APPROVED",
            bookingStart: new Date("2026-01-04T09:00"),
            bookingEnd: new Date("2026-01-14T09:00"),
            note: "Some optional notes"

        });

        expect(result.success).toBe(true);
    });

    describe("UserId", () => {
        test("should fail when userId is null", () => {
            const result = bookingValidationSchema.safeParse({
                userId: null,
                assetId: 1,
                status: "APPROVED",
                bookingStart: new Date("2026-01-04T09:00"),
                bookingEnd: new Date("2026-01-14T09:00"),
                note: "Some optional notes"

            });

            expect(result.success).toBe(false);
        });
    })

    describe("AssetId", () => {
        test("should fail when assetId is null", () => {
            const result = bookingValidationSchema.safeParse({
                userId: 2,
                assetId: null,
                status: "APPROVED",
                bookingStart: new Date("2026-01-04T09:00"),
                bookingEnd: new Date("2026-01-14T09:00"),
                note: "Some optional notes"

            });

            expect(result.success).toBe(false);
        });
    })

    describe("Status", () => {
        test("should fail when status is null", () => {
            const result = bookingValidationSchema.safeParse({
                userId: 2,
                assetId: 1,
                status: null,
                bookingStart: new Date("2026-01-04T09:00"),
                bookingEnd: new Date("2026-01-14T09:00"),
                note: "Some optional notes"

            });

            expect(result.success).toBe(false);
        });
    })

    describe("BookingStart", () => {
        test("should fail when bookingStart is null", () => {
            const result = bookingValidationSchema.safeParse({
                userId: 2,
                assetId: 1,
                status: "APPROVED",
                bookingStart: null,
                bookingEnd: new Date("2026-01-14T09:00"),
                note: "Some optional notes"

            });

            expect(result.success).toBe(false);
        });
    })



    describe("BookingEnd", () => {
        test("should fail when bookingEnd is null", () => {
            const result = bookingValidationSchema.safeParse({
                userId: 2,
                assetId: 1,
                status: "APPROVED",
                bookingStart: new Date("2026-01-04T09:00"),
                bookingEnd: null,
                note: "Some optional notes"

            });

            expect(result.success).toBe(false);
        });
    })


    describe("Note", () => {
        test("should pass when note is empty", () => {
            const result = bookingValidationSchema.safeParse({
                userId: 2,
                assetId: 1,
                status: "APPROVED",
                bookingStart: new Date("2026-01-04T09:00"),
                bookingEnd: new Date("2026-01-14T09:00"),
                note: ""

            });

            expect(result.success).toBe(true);
        });

        test("should fail when notes is too long ", () => {
            const result = bookingValidationSchema.safeParse({
                userId: 2,
                assetId: 1,
                status: "APPROVED",
                bookingStart: new Date("2026-01-04T09:00"),
                bookingEnd: new Date("2026-01-14T09:00"),
                note: "a".repeat(1001)
            });

            expect(result.success).toBe(false);

        });

    })



})


