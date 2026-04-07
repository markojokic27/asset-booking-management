-- Set search path
SET search_path TO asset_booking_mgm;

-- Increase QR/ISBN/etc code length
ALTER TABLE asset
    ALTER COLUMN code TYPE VARCHAR(2000);

-- Make location NOT NULL
ALTER TABLE asset
    ALTER COLUMN location SET NOT NULL;