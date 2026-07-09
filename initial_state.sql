--
-- PostgreSQL database dump
--

\restrict AQIWbnUcrPCfyMIbHYep2xiBfGyIYLbRG8rcX8q8Meq8yUGkqiacQXQieYcZQZo

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY asset_booking_mgm.department DROP CONSTRAINT IF EXISTS fk_manager_user;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset_user DROP CONSTRAINT IF EXISTS fk_department;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.booking DROP CONSTRAINT IF EXISTS fk_booking_user;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.booking DROP CONSTRAINT IF EXISTS fk_booking_asset;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset DROP CONSTRAINT IF EXISTS fk_asset_category;
DROP INDEX IF EXISTS asset_booking_mgm.idx_user_username;
DROP INDEX IF EXISTS asset_booking_mgm.idx_booking_user;
DROP INDEX IF EXISTS asset_booking_mgm.idx_booking_asset;
DROP INDEX IF EXISTS asset_booking_mgm.idx_asset_status;
DROP INDEX IF EXISTS asset_booking_mgm.idx_asset_category;
DROP INDEX IF EXISTS asset_booking_mgm.flyway_schema_history_s_idx;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.booking DROP CONSTRAINT IF EXISTS no_overlapping_bookings;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.flyway_schema_history DROP CONSTRAINT IF EXISTS flyway_schema_history_pk;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.department DROP CONSTRAINT IF EXISTS department_pkey;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.department DROP CONSTRAINT IF EXISTS department_name_key;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.department DROP CONSTRAINT IF EXISTS department_manager_id_key;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.booking DROP CONSTRAINT IF EXISTS booking_pkey;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset_user DROP CONSTRAINT IF EXISTS asset_user_username_key;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset_user DROP CONSTRAINT IF EXISTS asset_user_pkey;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset_user DROP CONSTRAINT IF EXISTS asset_user_email_key;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset DROP CONSTRAINT IF EXISTS asset_pkey;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset DROP CONSTRAINT IF EXISTS asset_code_key;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset_category DROP CONSTRAINT IF EXISTS asset_category_pkey;
ALTER TABLE IF EXISTS ONLY asset_booking_mgm.asset_category DROP CONSTRAINT IF EXISTS asset_category_name_key;
DROP TABLE IF EXISTS asset_booking_mgm.flyway_schema_history;
DROP TABLE IF EXISTS asset_booking_mgm.department;
DROP TABLE IF EXISTS asset_booking_mgm.booking;
DROP TABLE IF EXISTS asset_booking_mgm.asset_user;
DROP TABLE IF EXISTS asset_booking_mgm.asset_category;
DROP TABLE IF EXISTS asset_booking_mgm.asset;
DROP EXTENSION IF EXISTS btree_gist;
DROP SCHEMA IF EXISTS asset_booking_mgm;
--
-- Name: asset_booking_mgm; Type: SCHEMA; Schema: -; Owner: asset_mgm_user
--

CREATE SCHEMA asset_booking_mgm;


ALTER SCHEMA asset_booking_mgm OWNER TO asset_mgm_user;

--
-- Name: SCHEMA asset_booking_mgm; Type: COMMENT; Schema: -; Owner: asset_mgm_user
--

COMMENT ON SCHEMA asset_booking_mgm IS 'Schema for Asset Booking Management system';


--
-- Name: btree_gist; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA asset_booking_mgm;


--
-- Name: EXTENSION btree_gist; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION btree_gist IS 'support for indexing common datatypes in GiST';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: asset; Type: TABLE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE TABLE asset_booking_mgm.asset (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    category_id bigint CONSTRAINT asset_category_id_not_null1 NOT NULL,
    status character varying(50) NOT NULL,
    description character varying(255),
    code character varying(2000),
    location character varying(100) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    last_modified_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE asset_booking_mgm.asset OWNER TO asset_mgm_user;

--
-- Name: TABLE asset; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON TABLE asset_booking_mgm.asset IS 'Stores physical or digital assets';


--
-- Name: COLUMN asset.id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.id IS 'Primary key';


--
-- Name: COLUMN asset.name; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.name IS 'Asset name';


--
-- Name: COLUMN asset.category_id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.category_id IS 'Reference to asset category';


--
-- Name: COLUMN asset.status; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.status IS 'Asset status (AVAILABLE, RESERVED, etc.)';


--
-- Name: COLUMN asset.description; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.description IS 'Additional description';


--
-- Name: COLUMN asset.code; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.code IS 'Unique QR/ISBN/barcode';


--
-- Name: COLUMN asset.location; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.location IS 'Physical location of asset';


--
-- Name: COLUMN asset.created_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.created_at IS 'Timestamp of creation';


--
-- Name: COLUMN asset.last_modified_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset.last_modified_at IS 'Timestamp of last update';


--
-- Name: asset_category; Type: TABLE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE TABLE asset_booking_mgm.asset_category (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(255),
    booking_period character varying(50) NOT NULL,
    approval boolean DEFAULT false NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    last_modified_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE asset_booking_mgm.asset_category OWNER TO asset_mgm_user;

--
-- Name: TABLE asset_category; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON TABLE asset_booking_mgm.asset_category IS 'Defines categories of assets';


--
-- Name: COLUMN asset_category.id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_category.id IS 'Primary key';


--
-- Name: COLUMN asset_category.name; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_category.name IS 'Unique category name';


--
-- Name: COLUMN asset_category.description; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_category.description IS 'Description of category';


--
-- Name: COLUMN asset_category.booking_period; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_category.booking_period IS 'Allowed booking duration (HOUR, DAY, etc...)';


--
-- Name: COLUMN asset_category.approval; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_category.approval IS 'Whether booking requires approval';


--
-- Name: COLUMN asset_category.created_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_category.created_at IS 'Timestamp of creation';


--
-- Name: COLUMN asset_category.last_modified_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_category.last_modified_at IS 'Timestamp of last update';


--
-- Name: asset_category_id_seq; Type: SEQUENCE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE asset_booking_mgm.asset_category ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME asset_booking_mgm.asset_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: asset_id_seq; Type: SEQUENCE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE asset_booking_mgm.asset ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME asset_booking_mgm.asset_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: asset_user; Type: TABLE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE TABLE asset_booking_mgm.asset_user (
    id bigint NOT NULL,
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    name character varying(100) NOT NULL,
    surname character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    status character varying(100) NOT NULL,
    department_id bigint NOT NULL,
    role character varying(100) NOT NULL,
    notes character varying(255),
    benefit character varying(100) NOT NULL,
    manager_email character varying(100),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    last_modified_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE asset_booking_mgm.asset_user OWNER TO asset_mgm_user;

--
-- Name: TABLE asset_user; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON TABLE asset_booking_mgm.asset_user IS 'Stores system users';


--
-- Name: COLUMN asset_user.id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.id IS 'Primary key';


--
-- Name: COLUMN asset_user.username; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.username IS 'Unique username for login';


--
-- Name: COLUMN asset_user.password; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.password IS 'Hashed password';


--
-- Name: COLUMN asset_user.name; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.name IS 'First name';


--
-- Name: COLUMN asset_user.surname; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.surname IS 'Last name';


--
-- Name: COLUMN asset_user.email; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.email IS 'Unique email address';


--
-- Name: COLUMN asset_user.status; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.status IS 'User status (ACTIVE, INACTIVE, etc.)';


--
-- Name: COLUMN asset_user.department_id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.department_id IS 'Reference to department';


--
-- Name: COLUMN asset_user.role; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.role IS 'User role (ADMIN, EMPLOYEE, etc.)';


--
-- Name: COLUMN asset_user.notes; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.notes IS 'Additional notes about user';


--
-- Name: COLUMN asset_user.benefit; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.benefit IS 'User booking benefits';


--
-- Name: COLUMN asset_user.manager_email; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.manager_email IS 'Email of manager for approvals';


--
-- Name: COLUMN asset_user.created_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.created_at IS 'Timestamp of creation';


--
-- Name: COLUMN asset_user.last_modified_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.asset_user.last_modified_at IS 'Timestamp of last update';


--
-- Name: asset_user_id_seq; Type: SEQUENCE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE asset_booking_mgm.asset_user ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME asset_booking_mgm.asset_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: booking; Type: TABLE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE TABLE asset_booking_mgm.booking (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    asset_id bigint NOT NULL,
    booking_start timestamp with time zone NOT NULL,
    booking_end timestamp with time zone NOT NULL,
    status character varying(50) NOT NULL,
    notes character varying(255),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    last_modified_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_booking_dates_valid CHECK ((booking_end > booking_start)),
    CONSTRAINT chk_booking_time CHECK ((booking_end > booking_start))
);


ALTER TABLE asset_booking_mgm.booking OWNER TO asset_mgm_user;

--
-- Name: TABLE booking; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON TABLE asset_booking_mgm.booking IS 'Tracks asset bookings by users';


--
-- Name: COLUMN booking.id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.id IS 'Primary key';


--
-- Name: COLUMN booking.user_id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.user_id IS 'Reference to user';


--
-- Name: COLUMN booking.asset_id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.asset_id IS 'Reference to asset';


--
-- Name: COLUMN booking.booking_start; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.booking_start IS 'Start time of booking';


--
-- Name: COLUMN booking.booking_end; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.booking_end IS 'End time of booking';


--
-- Name: COLUMN booking.status; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.status IS 'Booking lifecycle status';


--
-- Name: COLUMN booking.notes; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.notes IS 'Additional booking notes';


--
-- Name: COLUMN booking.created_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.created_at IS 'Timestamp of creation';


--
-- Name: COLUMN booking.last_modified_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.booking.last_modified_at IS 'Timestamp of last update';


--
-- Name: CONSTRAINT chk_booking_time ON booking; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT chk_booking_time ON asset_booking_mgm.booking IS 'Ensures booking_end is after booking_start';


--
-- Name: booking_id_seq; Type: SEQUENCE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE asset_booking_mgm.booking ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME asset_booking_mgm.booking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: department; Type: TABLE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE TABLE asset_booking_mgm.department (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    manager_id bigint,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    last_modified_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE asset_booking_mgm.department OWNER TO asset_mgm_user;

--
-- Name: TABLE department; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON TABLE asset_booking_mgm.department IS 'Represents company departments';


--
-- Name: COLUMN department.id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.department.id IS 'Primary key';


--
-- Name: COLUMN department.name; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.department.name IS 'Unique department name';


--
-- Name: COLUMN department.manager_id; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.department.manager_id IS 'Reference to user acting as manager';


--
-- Name: COLUMN department.created_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.department.created_at IS 'Timestamp of creation';


--
-- Name: COLUMN department.last_modified_at; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON COLUMN asset_booking_mgm.department.last_modified_at IS 'Timestamp of last update';


--
-- Name: department_id_seq; Type: SEQUENCE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE asset_booking_mgm.department ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME asset_booking_mgm.department_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: flyway_schema_history; Type: TABLE; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE TABLE asset_booking_mgm.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE asset_booking_mgm.flyway_schema_history OWNER TO asset_mgm_user;

--
-- Data for Name: asset; Type: TABLE DATA; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COPY asset_booking_mgm.asset (id, name, category_id, status, description, code, location, created_at, last_modified_at) FROM stdin;
1	MacBook Air M2	1	ACTIVE	Lightweight laptop	LAP-004	Office 10	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
2	Lenovo ThinkPad	1	ACTIVE	Business laptop	LAP-003	Office 2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
3	Dell XPS 13	1	INACTIVE	Ultrabook for staff	LAP-002	Office 7	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
4	MacBook Pro 16	1	ACTIVE	Apple laptop for developers	LAP-001	Office 3	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
9	Refactoring	3	ACTIVE	Code improvement book	BOOK-003	Library	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
10	Design Patterns	3	INACTIVE	Software design book	BOOK-002	Library	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
11	Clean Code	3	ACTIVE	Programming book	BOOK-001	Library	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
12	Desk B1	4	ACTIVE	Corner desk	DESK-003	Floor 2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
13	Desk A2	4	INACTIVE	Standard desk	DESK-002	Floor 1	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
14	Desk A1	4	ACTIVE	Standing desk	DESK-001	Floor 1	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
15	Meeting Room 26	5	ACTIVE	Large meeting room	MR-003	Floor 3	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
16	Meeting Room 18	5	INACTIVE	Medium meeting room	MR-002	Floor 2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
17	Meeting Room 12	5	ACTIVE	Small meeting room	MR-001	Floor 2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
18	Router Mikrotik	6	ACTIVE	Office router	IT-003	Server room	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
19	Switch Cisco 24-port	6	INACTIVE	Network switch	IT-002	Server room	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
20	Projector Epson	6	ACTIVE	HD projector	IT-001	Room 7	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
21	Parking Spot 55	2	ACTIVE	Garage parking	PARK-055	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
22	Parking Spot 54	2	ACTIVE	Garage parking	PARK-054	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
23	Parking Spot 53	2	ACTIVE	Garage parking	PARK-053	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
24	Parking Spot 49	2	ACTIVE	Garage parking	PARK-049	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
25	Parking Spot 48	2	ACTIVE	Garage parking	PARK-048	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
26	Parking Spot 21	2	ACTIVE	Garage parking	PARK-021	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
27	Parking Spot 20	2	ACTIVE	Garage parking	PARK-020	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
28	Parking Spot 19	2	ACTIVE	Garage parking	PARK-019	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
29	Parking Spot 17	2	ACTIVE	Garage parking	PARK-017	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
30	Parking Spot 16	2	ACTIVE	Garage parking	PARK-016	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
31	Parking Spot 15	2	ACTIVE	Garage parking	PARK-015	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
32	Parking Spot 14	2	ACTIVE	Garage parking	PARK-014	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
33	Parking Spot 13	2	ACTIVE	Garage parking	PARK-013	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
34	Parking Spot 12	2	ACTIVE	Garage parking	PARK-012	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
35	Parking Spot 11	2	ACTIVE	Garage parking	PARK-011	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
36	Parking Spot 9	2	ACTIVE	Garage parking	PARK-009	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
37	Parking Spot 8	2	ACTIVE	Garage parking	PARK-008	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
38	Parking Spot 7	2	ACTIVE	Garage parking	PARK-007	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
39	Parking Spot 6	2	ACTIVE	Garage parking	PARK-006	Level -2	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
44	Parking Spot 128	2	ACTIVE	Garage parking	PARK-128	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
45	Parking Spot 127	2	ACTIVE	Garage parking	PARK-127	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
46	Parking Spot 126	2	ACTIVE	Garage parking	PARK-126	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
47	Parking Spot 125	2	ACTIVE	Garage parking	PARK-125	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
48	Parking Spot 124	2	ACTIVE	Garage parking	PARK-124	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
49	Parking Spot 123	2	ACTIVE	Garage parking	PARK-123	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
50	Parking Spot 122	2	ACTIVE	Garage parking	PARK-122	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
51	Parking Spot 121	2	ACTIVE	Garage parking	PARK-121	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
52	Parking Spot 120	2	ACTIVE	Garage parking	PARK-120	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
53	Parking Spot 119	2	ACTIVE	Garage parking	PARK-119	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
54	Parking Spot 118	2	ACTIVE	Garage parking	PARK-118	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
55	Parking Spot 117	2	ACTIVE	Garage parking	PARK-117	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
56	Parking Spot 114	2	ACTIVE	Garage parking	PARK-114	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
57	Parking Spot 113	2	ACTIVE	Garage parking	PARK-113	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
58	Parking Spot 112	2	ACTIVE	Garage parking	PARK-112	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
59	Parking Spot 109	2	ACTIVE	Garage parking	PARK-109	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
60	Parking Spot 108	2	ACTIVE	Garage parking	PARK-108	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
61	Parking Spot 68	2	ACTIVE	Garage parking	PARK-068	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
62	Parking Spot 67	2	ACTIVE	Garage parking	PARK-067	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
63	Parking Spot 66	2	ACTIVE	Garage parking	PARK-066	Level -1	2026-06-26 13:44:21.624484+00	2026-06-26 13:44:21.624484+00
6	Parking Spot 22	2	DELETED	Outdoor parking	PARK-003	Floor plan 2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
5	Parking Spot 17	2	DELETED	Garage parking	PARK-004	Level -2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
7	Parking Spot 10	2	DAMAGED	Garage parking	PARK-002	Level -2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
8	Parking Spot 5	2	ACTIVE	Garage parking	PARK-001	Level -2	2026-06-26 13:44:21.48651+00	2026-06-26 13:44:21.48651+00
\.


--
-- Data for Name: asset_category; Type: TABLE DATA; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COPY asset_booking_mgm.asset_category (id, name, description, booking_period, approval, created_at, last_modified_at) FROM stdin;
3	Book	All company books	DAY	f	2026-06-26 13:44:21.443301+00	2026-06-26 13:44:21.443301+00
5	Meeting room	All company meeting rooms	HOUR	f	2026-06-26 13:44:21.443301+00	2026-06-26 13:44:21.443301+00
2	Parking	All company parkings	DAY	f	2026-06-26 13:44:21.443301+00	2026-06-26 13:44:21.443301+00
6	IT equipment	All company IT equipments	DAY	t	2026-06-26 13:44:21.443301+00	2026-06-26 13:44:21.443301+00
4	Desk	All company desks	DAY	f	2026-06-26 13:44:21.443301+00	2026-06-26 13:44:21.443301+00
1	Laptop	All company laptops	DAY	t	2026-06-26 13:44:21.443301+00	2026-06-26 13:44:21.443301+00
\.


--
-- Data for Name: asset_user; Type: TABLE DATA; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COPY asset_booking_mgm.asset_user (id, username, password, name, surname, email, status, department_id, role, notes, benefit, manager_email, created_at, last_modified_at) FROM stdin;
1	user_admin	$2a$12$9qse.vAVHdnMYkiWeK156.pWo3LCkZjVK6pfBXG4z0Rm3tiD5NVHu	John	Doe	john.doe@example.com	ACTIVE	1	ADMIN	This is a dummy admin user	ALL	manager.doe@example.com	2026-06-26 13:44:21.139664+00	2026-06-26 13:44:21.139664+00
2	user_employee	$2a$12$GuQwFZyOKSOttzf.hsqnEuDRbX2fB7XudiQsmOqWwYKLzzDrQO9Uq	Jane	Smith	jane.smith@example.com	ACTIVE	1	EMPLOYEE	This is a dummy employee user	ALL	manager.doe@example.com	2026-06-26 13:44:21.139664+00	2026-06-26 13:44:21.139664+00
3	user_manager	$2a$12$SlU1fXn97HS1ozbdV8mNy.UBbG.bK3fpEigx2//27.4eFPD3bWCNy	Mark	Jones	mark.jones@example.com	ACTIVE	1	MANAGER	This is a dummy manager user	ALL	manager.3@example.com	2026-06-26 13:44:21.139664+00	2026-06-26 13:44:21.139664+00
4	mJokic	$2a$12$M3KtX92YKiGlTqLHWJY/DeblyrFjRAlAlcX/okYtJ7MYS4kh3Sf9q	Marko	Jokic	marko.jokic@example.com	STUDENT	1	EMPLOYEE	This is a dummy Marko account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
5	mBanovic	$2a$12$pMy0lnfG7RnzwDT3Nhf8/Oi2/BKoQadL6m0CJTOBCUaMekOuQ7KGW	Mladen	Banovic	mladen.banovic@example.com	ACTIVE	2	MANAGER	This is a dummy Mladen account	ALL	manager.doe@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
6	mPlavcic	$2a$12$4PbDsj5Xn6oC7Iot.xjy6.lSxa/K/cSUsOdvX1jj.noWH825LuOje	Mateo	Plavcic	mateo.plavcic@example.com	STUDENT	3	EMPLOYEE	This is a dummy Mateo account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
7	nCarevic	$2a$12$0fGQHVWkfbDPAejm.VjBNOJuXG56ViZdtNKtP8rjOdF2MEDcYzSqa	Nikolina	Carevic	nikolina.carevic@example.com	STUDENT	4	EMPLOYEE	This is a dummy Nikolina account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
8	kNovakovic	$2a$12$jVlkOO3nofN57OzKsYwaLeLMGwLTA/YqFTSnGvI/j6B5SNq7wNIK.	Katarina	Novakovic	katarina.novakovic@example.com	STUDENT	4	EMPLOYEE	This is a dummy Katarina account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
9	mMajic	$2a$12$7Ibo2k6msrAwFXTgxEmGjeQvZBtJvzPndx3ckvYuEq.MEesp2ctJ6	Monika	Majic	monika.majic@example.com	STUDENT	5	EMPLOYEE	This is a dummy Monika account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
10	aMustapic	$2a$12$l15.tTGmBmoHkc.vxmLTqObXY4zchgFL/ZFXdf457VuL8.csEwCx2	Andela	Mustapic	andela.mustapic@example.com	STUDENT	6	EMPLOYEE	This is a dummy Andela account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
11	jDiko	$2a$12$Y4Ok9FyLScMv1yJ9Soz/dexsAyBuwUAFVyRCApNCjB1WwP2K33vuS	Josko	Diko	josko.diko@example.com	STUDENT	7	EMPLOYEE	This is a dummy Josko account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
12	jPetric	$2a$12$hDfCN4E.M8QicBIo6p599uh1Ntxqx9mJdpNDDSqP6reoj4eQEacku	Jakov	Petric	jakov.petric@example.com	STUDENT	8	EMPLOYEE	This is a dummy Jakov account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
13	zKujundzic	$2a$12$J.3tuMZ76JsvQ4bmpkHpF.KXNJi1V6HAh077mGv8gShpVj1zcjYkq	Zvonimir	Kujundzic	zvone.kujundzic@example.com	STUDENT	9	ADMIN	This is a dummy Zvone account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
14	dJezidzic	$2a$12$ExyBg6kzEU1Q93liOwG4qO.sWrxGEMG6Gxajsjf273ERAkbYM3xZO	David	Jezidzic	david.jezidzic@example.com	STUDENT	9	ADMIN	This is a dummy David account	ALL	mladen.banovic@example.com	2026-06-26 13:44:21.386663+00	2026-06-26 13:44:21.386663+00
\.


--
-- Data for Name: booking; Type: TABLE DATA; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COPY asset_booking_mgm.booking (id, user_id, asset_id, booking_start, booking_end, status, notes, created_at, last_modified_at) FROM stdin;
26	1	1	2026-04-24 14:00:00+00	2026-04-25 14:00:00+00	APPROVED	MacBook Air approved	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
27	3	2	2026-04-25 08:00:00+00	2026-04-26 08:00:00+00	CANCELLED	ThinkPad booking cancelled	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
28	2	3	2026-04-24 13:00:00+00	2026-04-25 13:00:00+00	PENDING	Waiting approval for Dell XPS	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
29	1	4	2026-04-24 09:00:00+00	2026-04-25 09:00:00+00	APPROVED	MacBook Pro booking for dev work	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
30	3	9	2026-04-24 16:00:00+00	2026-04-25 16:00:00+00	PENDING	Waiting approval for refactoring book	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
31	2	10	2026-04-24 14:00:00+00	2026-04-25 14:00:00+00	APPROVED	Design Patterns reading session	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
32	1	11	2026-04-24 11:00:00+00	2026-04-25 11:00:00+00	COMPLETED	Book returned	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
33	3	12	2026-04-24 13:00:00+00	2026-04-24 14:00:00+00	APPROVED	Desk approved booking	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
34	2	13	2026-04-24 09:00:00+00	2026-04-24 10:00:00+00	CANCELLED	Desk cancelled	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
35	1	14	2026-04-24 09:00:00+00	2026-04-24 10:00:00+00	APPROVED	Desk A1 full day booking	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
36	3	15	2026-04-25 15:00:00+00	2026-04-25 16:00:00+00	APPROVED	Large meeting room approved	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
37	2	16	2026-04-25 12:00:00+00	2026-04-25 13:00:00+00	REJECTED	Meeting room rejected	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
38	1	17	2026-04-25 09:00:00+00	2026-04-25 10:00:00+00	APPROVED	Meeting Room booking	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
39	3	18	2026-04-24 12:00:00+00	2026-05-24 12:00:00+00	APPROVED	Router setup	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
40	2	19	2026-04-24 10:00:00+00	2026-05-24 10:00:00+00	COMPLETED	Switch maintenance done	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
41	1	20	2026-04-24 08:00:00+00	2026-05-24 08:00:00+00	APPROVED	Projector use	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
42	2	29	2026-04-24 16:00:00+00	2026-05-24 16:00:00+00	PENDING	Parking removed	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
43	3	6	2026-04-24 11:00:00+00	2026-05-24 11:00:00+00	APPROVED	Outdoor parking use	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
44	2	5	2026-04-24 16:00:00+00	2026-05-24 16:00:00+00	PENDING	Parking removed	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
45	2	7	2026-04-24 09:00:00+00	2026-05-24 09:00:00+00	REJECTED	Parking request rejected	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
46	1	8	2026-04-24 10:00:00+00	2026-05-24 10:00:00+00	APPROVED	Parking approved	2026-07-03 12:54:38.544661+00	2026-07-03 12:54:38.544661+00
\.


--
-- Data for Name: department; Type: TABLE DATA; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COPY asset_booking_mgm.department (id, name, manager_id, created_at, last_modified_at) FROM stdin;
1	ARCHITECTURE	3	2026-06-26 13:44:21.139664+00	2026-06-26 13:44:21.139664+00
2	ADVANCE_TECHNOLOGY	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
3	SECURE_SERVICES	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
4	FINANCE_AND_BUSINESS_ADMINISTRATION	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
5	MOBILE_AND_SECURITY	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
6	SYSTEM_TEST	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
7	HUMAN_RESOURCES	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
8	CLOUD_AND_DATA_MANAGEMENT	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
9	DEVOPS	\N	2026-06-26 13:44:21.327723+00	2026-06-26 13:44:21.327723+00
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COPY asset_booking_mgm.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
0	\N	<< Flyway Schema Creation >>	SCHEMA	"asset_booking_mgm"	\N	asset_mgm_user	2026-06-26 13:44:20.822242	0	t
1	1	init schema	SQL	V1__init_schema.sql	518446139	asset_mgm_user	2026-06-26 13:44:20.857	51	t
2	2	alter asset columns	SQL	V2__alter_asset_columns.sql	569631642	asset_mgm_user	2026-06-26 13:44:20.990002	6	t
3	3	add comments	SQL	V3__add_comments.sql	784976984	asset_mgm_user	2026-06-26 13:44:21.021021	63	t
4	4	insert initial department and users	SQL	V4__insert_initial_department_and_users.sql	-377052946	asset_mgm_user	2026-06-26 13:44:21.130525	11	t
5	5	add booking constraints	SQL	V5__add_booking_constraints.sql	-1923018818	asset_mgm_user	2026-06-26 13:44:21.163483	79	t
6	6	update booking constraint with status	SQL	V6__update_booking_constraint_with_status.sql	-1811324919	asset_mgm_user	2026-06-26 13:44:21.269498	5	t
7	7	insert missing departments	SQL	V7__insert_missing_departments.sql	472502092	asset_mgm_user	2026-06-26 13:44:21.301719	35	t
8	8	insert new users	SQL	V8__insert_new_users.sql	1148733299	asset_mgm_user	2026-06-26 13:44:21.363793	7	t
9	9	make manager email nullable	SQL	V9__make_manager_email_nullable.sql	-1689281873	asset_mgm_user	2026-06-26 13:44:21.407964	4	t
10	10	insert new asset categories	SQL	V10__insert_new_asset_categories.sql	1657202833	asset_mgm_user	2026-06-26 13:44:21.438032	8	t
11	11	insert new asset	SQL	V11__insert_new_asset.sql	-505184375	asset_mgm_user	2026-06-26 13:44:21.472615	8	t
12	12	insert new bookings	SQL	V12__insert_new_bookings.sql	-640633421	asset_mgm_user	2026-06-26 13:44:21.506861	6	t
13	15	change parking booking period	SQL	V15__change_parking_booking_period.sql	1307374295	asset_mgm_user	2026-06-26 13:44:21.538627	12	t
14	16	change it equipment period	SQL	V16__change_it_equipment_period.sql	-437440288	asset_mgm_user	2026-06-26 13:44:21.566242	10	t
15	17	change desk period	SQL	V17__change_desk_period.sql	946182305	asset_mgm_user	2026-06-26 13:44:21.589543	5	t
16	18	new assets for parking	SQL	V18__new_assets_for_parking.sql	-753117231	asset_mgm_user	2026-06-26 13:44:21.618268	8	t
17	19	delete parking 22	SQL	V19__delete_parking_22.sql	999034359	asset_mgm_user	2026-06-26 13:44:21.64649	4	t
18	20	update parking	SQL	V20__update_parking.sql	1052438072	asset_mgm_user	2026-06-26 13:44:21.661257	5	t
19	21	update asset category	SQL	V21__update_asset_category.sql	746880602	asset_mgm_user	2026-06-26 13:44:21.68445	7	t
\.


--
-- Name: asset_category_id_seq; Type: SEQUENCE SET; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

SELECT pg_catalog.setval('asset_booking_mgm.asset_category_id_seq', 6, true);


--
-- Name: asset_id_seq; Type: SEQUENCE SET; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

SELECT pg_catalog.setval('asset_booking_mgm.asset_id_seq', 63, true);


--
-- Name: asset_user_id_seq; Type: SEQUENCE SET; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

SELECT pg_catalog.setval('asset_booking_mgm.asset_user_id_seq', 14, true);


--
-- Name: booking_id_seq; Type: SEQUENCE SET; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

SELECT pg_catalog.setval('asset_booking_mgm.booking_id_seq', 46, true);


--
-- Name: department_id_seq; Type: SEQUENCE SET; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

SELECT pg_catalog.setval('asset_booking_mgm.department_id_seq', 9, true);


--
-- Name: asset_category asset_category_name_key; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset_category
    ADD CONSTRAINT asset_category_name_key UNIQUE (name);


--
-- Name: CONSTRAINT asset_category_name_key ON asset_category; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT asset_category_name_key ON asset_booking_mgm.asset_category IS 'Ensures asset category name is unique';


--
-- Name: asset_category asset_category_pkey; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset_category
    ADD CONSTRAINT asset_category_pkey PRIMARY KEY (id);


--
-- Name: asset asset_code_key; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset
    ADD CONSTRAINT asset_code_key UNIQUE (code);


--
-- Name: asset asset_pkey; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset
    ADD CONSTRAINT asset_pkey PRIMARY KEY (id);


--
-- Name: asset_user asset_user_email_key; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset_user
    ADD CONSTRAINT asset_user_email_key UNIQUE (email);


--
-- Name: CONSTRAINT asset_user_email_key ON asset_user; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT asset_user_email_key ON asset_booking_mgm.asset_user IS 'Ensures email is unique';


--
-- Name: asset_user asset_user_pkey; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset_user
    ADD CONSTRAINT asset_user_pkey PRIMARY KEY (id);


--
-- Name: asset_user asset_user_username_key; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset_user
    ADD CONSTRAINT asset_user_username_key UNIQUE (username);


--
-- Name: CONSTRAINT asset_user_username_key ON asset_user; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT asset_user_username_key ON asset_booking_mgm.asset_user IS 'Ensures username is unique';


--
-- Name: booking booking_pkey; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.booking
    ADD CONSTRAINT booking_pkey PRIMARY KEY (id);


--
-- Name: department department_manager_id_key; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.department
    ADD CONSTRAINT department_manager_id_key UNIQUE (manager_id);


--
-- Name: department department_name_key; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.department
    ADD CONSTRAINT department_name_key UNIQUE (name);


--
-- Name: CONSTRAINT department_name_key ON department; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT department_name_key ON asset_booking_mgm.department IS 'Ensures department name is unique';


--
-- Name: department department_pkey; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.department
    ADD CONSTRAINT department_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: booking no_overlapping_bookings; Type: CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.booking
    ADD CONSTRAINT no_overlapping_bookings EXCLUDE USING gist (asset_id WITH =, tstzrange(booking_start, booking_end) WITH &&) WHERE (((status)::text = ANY (ARRAY[('PENDING'::character varying)::text, ('APPROVED'::character varying)::text])));


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE INDEX flyway_schema_history_s_idx ON asset_booking_mgm.flyway_schema_history USING btree (success);


--
-- Name: idx_asset_category; Type: INDEX; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE INDEX idx_asset_category ON asset_booking_mgm.asset USING btree (category_id);


--
-- Name: INDEX idx_asset_category; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON INDEX asset_booking_mgm.idx_asset_category IS 'Speeds up queries filtering assets by category';


--
-- Name: idx_asset_status; Type: INDEX; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE INDEX idx_asset_status ON asset_booking_mgm.asset USING btree (status);


--
-- Name: INDEX idx_asset_status; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON INDEX asset_booking_mgm.idx_asset_status IS 'Speeds up queries filtering assets by status';


--
-- Name: idx_booking_asset; Type: INDEX; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE INDEX idx_booking_asset ON asset_booking_mgm.booking USING btree (asset_id);


--
-- Name: INDEX idx_booking_asset; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON INDEX asset_booking_mgm.idx_booking_asset IS 'Speeds up queries retrieving bookings by asset';


--
-- Name: idx_booking_user; Type: INDEX; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE INDEX idx_booking_user ON asset_booking_mgm.booking USING btree (user_id);


--
-- Name: INDEX idx_booking_user; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON INDEX asset_booking_mgm.idx_booking_user IS 'Speeds up queries retrieving bookings by user';


--
-- Name: idx_user_username; Type: INDEX; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

CREATE UNIQUE INDEX idx_user_username ON asset_booking_mgm.asset_user USING btree (username);


--
-- Name: INDEX idx_user_username; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON INDEX asset_booking_mgm.idx_user_username IS 'Ensures unique usernames and speeds up lookup by username';


--
-- Name: asset fk_asset_category; Type: FK CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset
    ADD CONSTRAINT fk_asset_category FOREIGN KEY (category_id) REFERENCES asset_booking_mgm.asset_category(id) ON DELETE RESTRICT;


--
-- Name: CONSTRAINT fk_asset_category ON asset; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT fk_asset_category ON asset_booking_mgm.asset IS 'Links asset to its category (asset.category_id → asset_category.id)';


--
-- Name: booking fk_booking_asset; Type: FK CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.booking
    ADD CONSTRAINT fk_booking_asset FOREIGN KEY (asset_id) REFERENCES asset_booking_mgm.asset(id) ON DELETE RESTRICT;


--
-- Name: CONSTRAINT fk_booking_asset ON booking; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT fk_booking_asset ON asset_booking_mgm.booking IS 'Links booking to asset (booking.asset_id → asset.id)';


--
-- Name: booking fk_booking_user; Type: FK CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.booking
    ADD CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES asset_booking_mgm.asset_user(id) ON DELETE RESTRICT;


--
-- Name: CONSTRAINT fk_booking_user ON booking; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT fk_booking_user ON asset_booking_mgm.booking IS 'Links booking to user (booking.user_id → asset_user.id)';


--
-- Name: asset_user fk_department; Type: FK CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.asset_user
    ADD CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES asset_booking_mgm.department(id) ON DELETE RESTRICT;


--
-- Name: CONSTRAINT fk_department ON asset_user; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT fk_department ON asset_booking_mgm.asset_user IS 'Links user to department (asset_user.department_id → department.id)';


--
-- Name: department fk_manager_user; Type: FK CONSTRAINT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

ALTER TABLE ONLY asset_booking_mgm.department
    ADD CONSTRAINT fk_manager_user FOREIGN KEY (manager_id) REFERENCES asset_booking_mgm.asset_user(id) ON DELETE RESTRICT;


--
-- Name: CONSTRAINT fk_manager_user ON department; Type: COMMENT; Schema: asset_booking_mgm; Owner: asset_mgm_user
--

COMMENT ON CONSTRAINT fk_manager_user ON asset_booking_mgm.department IS 'Links department manager to user (department.manager_id → asset_user.id)';


--
-- PostgreSQL database dump complete
--

\unrestrict AQIWbnUcrPCfyMIbHYep2xiBfGyIYLbRG8rcX8q8Meq8yUGkqiacQXQieYcZQZo

