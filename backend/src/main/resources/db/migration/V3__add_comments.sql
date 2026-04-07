-- ==========================================================
-- SCHEMA COMMENT
-- ==========================================================
COMMENT ON SCHEMA asset_booking_mgm IS 'Schema for Asset Booking Management system';

-- ==========================================================
-- TABLE COMMENTS
-- ==========================================================
COMMENT ON TABLE asset_user IS 'Stores system users';
COMMENT ON TABLE department IS 'Represents company departments';
COMMENT ON TABLE asset_category IS 'Defines categories of assets';
COMMENT ON TABLE asset IS 'Stores physical or digital assets';
COMMENT ON TABLE booking IS 'Tracks asset bookings by users';

-- ==========================================================
-- COLUMN COMMENTS - asset_user
-- ==========================================================
COMMENT ON COLUMN asset_user.id IS 'Primary key';
COMMENT ON COLUMN asset_user.username IS 'Unique username for login';
COMMENT ON COLUMN asset_user.password IS 'Hashed password';
COMMENT ON COLUMN asset_user.name IS 'First name';
COMMENT ON COLUMN asset_user.surname IS 'Last name';
COMMENT ON COLUMN asset_user.email IS 'Unique email address';
COMMENT ON COLUMN asset_user.status IS 'User status (ACTIVE, INACTIVE, etc.)';
COMMENT ON COLUMN asset_user.department_id IS 'Reference to department';
COMMENT ON COLUMN asset_user.role IS 'User role (ADMIN, EMPLOYEE, etc.)';
COMMENT ON COLUMN asset_user.notes IS 'Additional notes about user';
COMMENT ON COLUMN asset_user.benefit IS 'User booking benefits';
COMMENT ON COLUMN asset_user.manager_email IS 'Email of manager for approvals';
COMMENT ON COLUMN asset_user.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN asset_user.last_modified_at IS 'Timestamp of last update';

-- ==========================================================
-- COLUMN COMMENTS - department
-- ==========================================================
COMMENT ON COLUMN department.id IS 'Primary key';
COMMENT ON COLUMN department.name IS 'Unique department name';
COMMENT ON COLUMN department.manager_id IS 'Reference to user acting as manager';
COMMENT ON COLUMN department.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN department.last_modified_at IS 'Timestamp of last update';

-- ==========================================================
-- COLUMN COMMENTS - asset_category
-- ==========================================================
COMMENT ON COLUMN asset_category.id IS 'Primary key';
COMMENT ON COLUMN asset_category.name IS 'Unique category name';
COMMENT ON COLUMN asset_category.description IS 'Description of category';
COMMENT ON COLUMN asset_category.booking_period IS 'Allowed booking duration (HOUR, DAY, etc...)';
COMMENT ON COLUMN asset_category.approval IS 'Whether booking requires approval';
COMMENT ON COLUMN asset_category.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN asset_category.last_modified_at IS 'Timestamp of last update';

-- ==========================================================
-- COLUMN COMMENTS - asset
-- ==========================================================
COMMENT ON COLUMN asset.id IS 'Primary key';
COMMENT ON COLUMN asset.name IS 'Asset name';
COMMENT ON COLUMN asset.category_id IS 'Reference to asset category';
COMMENT ON COLUMN asset.status IS 'Asset status (AVAILABLE, RESERVED, etc.)';
COMMENT ON COLUMN asset.description IS 'Additional description';
COMMENT ON COLUMN asset.code IS 'Unique QR/ISBN/barcode';
COMMENT ON COLUMN asset.location IS 'Physical location of asset';
COMMENT ON COLUMN asset.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN asset.last_modified_at IS 'Timestamp of last update';

-- ==========================================================
-- COLUMN COMMENTS - booking
-- ==========================================================
COMMENT ON COLUMN booking.id IS 'Primary key';
COMMENT ON COLUMN booking.user_id IS 'Reference to user';
COMMENT ON COLUMN booking.asset_id IS 'Reference to asset';
COMMENT ON COLUMN booking.booking_start IS 'Start time of booking';
COMMENT ON COLUMN booking.booking_end IS 'End time of booking';
COMMENT ON COLUMN booking.status IS 'Booking lifecycle status';
COMMENT ON COLUMN booking.notes IS 'Additional booking notes';
COMMENT ON COLUMN booking.created_at IS 'Timestamp of creation';
COMMENT ON COLUMN booking.last_modified_at IS 'Timestamp of last update';

-- ==========================================================
-- FOREIGN KEY CONSTRAINT COMMENTS
-- ==========================================================

COMMENT ON CONSTRAINT fk_asset_category ON asset
IS 'Links asset to its category (asset.category_id → asset_category.id)';

COMMENT ON CONSTRAINT fk_booking_user ON booking
IS 'Links booking to user (booking.user_id → asset_user.id)';

COMMENT ON CONSTRAINT fk_booking_asset ON booking
IS 'Links booking to asset (booking.asset_id → asset.id)';

COMMENT ON CONSTRAINT fk_department ON asset_user
IS 'Links user to department (asset_user.department_id → department.id)';

COMMENT ON CONSTRAINT fk_manager_user ON department
IS 'Links department manager to user (department.manager_id → asset_user.id)';

-- ==========================================================
-- CHECK CONSTRAINT COMMENTS
-- ==========================================================

COMMENT ON CONSTRAINT chk_booking_time ON booking
IS 'Ensures booking_end is after booking_start';

-- ==========================================================
-- UNIQUE CONSTRAINT COMMENTS
-- ==========================================================

COMMENT ON CONSTRAINT department_name_key ON department
IS 'Ensures department name is unique';

COMMENT ON CONSTRAINT asset_category_name_key ON asset_category
IS 'Ensures asset category name is unique';

COMMENT ON CONSTRAINT asset_user_username_key ON asset_user
IS 'Ensures username is unique';

COMMENT ON CONSTRAINT asset_user_email_key ON asset_user
IS 'Ensures email is unique';

-- ==========================================================
-- INDEX COMMENTS
-- ==========================================================

COMMENT ON INDEX idx_user_username IS 'Ensures unique usernames and speeds up lookup by username';

COMMENT ON INDEX idx_asset_category IS 'Speeds up queries filtering assets by category';

COMMENT ON INDEX idx_asset_status IS 'Speeds up queries filtering assets by status';

COMMENT ON INDEX idx_booking_user IS 'Speeds up queries retrieving bookings by user';

COMMENT ON INDEX idx_booking_asset IS 'Speeds up queries retrieving bookings by asset';