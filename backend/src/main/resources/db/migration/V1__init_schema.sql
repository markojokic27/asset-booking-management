
-- ==========================================================
-- SCHEMA
-- ==========================================================

CREATE SCHEMA IF NOT EXISTS asset_booking_mgm;

-- Set search path
SET search_path TO asset_booking_mgm;

-- ==========================================================
-- TABLES
-- ==========================================================

-- user table
CREATE TABLE IF NOT EXISTS asset_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(100) NOT NULL,
    -- FK to department.id (added below)
    department_id BIGINT NOT NULL,
    role VARCHAR(100) NOT NULL,
    -- additional user info
    notes VARCHAR(255),
    -- user booking benefits
    benefit VARCHAR(100) NOT NULL,
    -- email to send notification for approval
    manager_email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- department table
CREATE TABLE IF NOT EXISTS department (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    -- FK to asset_user.id (added below)
    -- UNIQUE constraint, each manager can lead only one department 
    manager_id BIGINT UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- asset category table
CREATE TABLE IF NOT EXISTS asset_category (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    -- additional asset category info
    description VARCHAR(255),
    booking_period VARCHAR(50) NOT NULL,
    -- whether booking requires approval
    approval BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- asset table
CREATE TABLE IF NOT EXISTS asset (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    -- FK to asset_category.id
    category_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    -- additional asset info
    description VARCHAR(255),
    --QR/ISBN/barcode, must be unique if present 
    code VARCHAR(100) UNIQUE, 
    -- physical location of asset
    location VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    -- Constraints
    CONSTRAINT fk_asset_category FOREIGN KEY (category_id) REFERENCES asset_category(id) ON DELETE RESTRICT
);

-- booking table
CREATE TABLE IF NOT EXISTS booking (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    -- FK to asset_user.id
    user_id BIGINT NOT NULL,
    -- FK to asset.id
    asset_id BIGINT NOT NULL,
    -- booking start
    booking_start TIMESTAMP WITH TIME ZONE NOT NULL,
    -- booking end
    booking_end TIMESTAMP WITH TIME ZONE NOT NULL,
    -- lifecycle status
    status VARCHAR(50) NOT NULL,
    -- additional booking info, especially for recurring bookings
    notes VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    -- constraints
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES asset_user(id) ON DELETE RESTRICT,
    CONSTRAINT fk_booking_asset FOREIGN KEY (asset_id) REFERENCES asset(id) ON DELETE RESTRICT,
    -- ensure valid time range
    CONSTRAINT chk_booking_time CHECK (booking_end > booking_start)
);

-- ==========================================================
-- Foreign key constraints
-- Added separately to handle circular references between 
-- user and department
-- ==========================================================

ALTER TABLE asset_user
    ADD CONSTRAINT fk_department 
    FOREIGN KEY (department_id) 
    REFERENCES department(id)
    ON DELETE RESTRICT;

ALTER TABLE department
    ADD CONSTRAINT fk_manager_user 
    FOREIGN KEY (manager_id) 
    REFERENCES asset_user(id)
    ON DELETE RESTRICT;

-- ==========================================================
-- INDEXES
-- ==========================================================

-- find users by username
CREATE UNIQUE INDEX idx_user_username ON asset_user(username);

-- search assets by category
CREATE INDEX idx_asset_category ON asset(category_id);

-- search assets by status
CREATE INDEX idx_asset_status ON asset(status);

-- search bookings by user
CREATE INDEX idx_booking_user ON booking(user_id);

-- search bookings by asset 
CREATE INDEX idx_booking_asset ON booking(asset_id);

-- ==========================================================
-- STORED PROCEDURES
-- ==========================================================

-- ==========================================================
-- TRIGGERS 
-- ==========================================================


