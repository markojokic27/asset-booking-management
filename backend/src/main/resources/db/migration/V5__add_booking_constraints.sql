CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE booking
    ADD CONSTRAINT no_overlapping_bookings
    EXCLUDE USING gist (
    asset_id WITH =,
    tstzrange(booking_start, booking_end) WITH &&
);

ALTER TABLE booking
    ADD CONSTRAINT check_booking_dates_valid
        CHECK (booking_end > booking_start);