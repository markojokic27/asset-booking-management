ALTER TABLE booking
DROP CONSTRAINT IF EXISTS no_overlapping_bookings;

ALTER TABLE booking
    ADD CONSTRAINT no_overlapping_bookings
    EXCLUDE USING gist (
    asset_id WITH =,
    tstzrange(booking_start, booking_end) WITH &&
)
WHERE (status IN ('PENDING', 'APPROVED'));