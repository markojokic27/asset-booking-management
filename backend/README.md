# Backend — Asset Booking Management

**Spring Boot 4.1 · Java 21 · Maven · PostgreSQL 18**

## Prerequisites

- **Docker + Docker Compose** (easiest path)
- *or* **JDK 21 + Maven 3.9+** (for local dev without containers)

## Quick start with Docker

Start the backend standalone (no frontend, no observability stack):

```bash
docker compose -f compose.backend.dev.yaml up --build
```

From the repo root, run the full stack (backend + frontend + observability):

```bash
make dev
```

The API is available at `http://localhost:8080/`.

## Local development without Docker

Requires a running PostgreSQL 18 instance. Copy `../sample.env` to `../.env`, adjust credentials, then:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The `dev` profile starts an embedded LDAP server and enables Swagger UI.

## Build & test

```bash
mvn verify              # compile + test + JaCoCo coverage report
mvn test                # tests only (skips coverage report)
mvn clean install -DskipTests   # build jar without tests
```

Tests use **Testcontainers** (needs Docker). The base test class is `AbstractIntegrationTest` with `@ServiceConnection` for automatic PostgreSQL container setup.

JaCoCo coverage report: `target/site/jacoco/index.html`

## Spring profiles

| Profile | Swagger | LDAP | Use case |
|---------|---------|------|----------|
| `dev`   | on      | embedded | local development |
| `prod`  | off     | external | production |
| `e2e`   | on      | embedded | end-to-end tests |
| `test`  | off     | embedded | unit/integration tests |

## Project structure

```
backend/
├── src/main/java/de/bdr/asset/management/
│   ├── AssetBookingApplication.java     # entry point (@SpringBootApplication)
│   ├── asset/                           # Asset CRUD + QR codes
│   ├── assetcategory/                   # Asset categories
│   ├── booking/                         # Booking CRUD + scheduling
│   ├── user/                            # Users + departments
│   ├── report/                          # Reporting endpoints
│   └── core/
│       ├── config/                      # CORS, OpenAPI, async, time
│       ├── security/                    # JWT auth, user details, benefits
│       ├── ldap/                        # LDAP sync
│       ├── ratelimit/                   # Bucket4j rate limiting
│       ├── aspect/                      # Logging, soft-delete AOP
│       └── exception/                   # GlobalExceptionHandler
├── src/main/resources/
│   ├── application.yaml                 # main config
│   ├── application-dev.yaml
│   ├── application-prod.yaml
│   ├── application-e2e.yaml
│   └── db/migration/                    # Flyway migrations (V1–V21)
└── src/test/java/                       # tests mirror main structure
```

## API

- Base path: `v1/*`
- Swagger UI: `http://localhost:8080/swagger-ui.html` (dev/e2e profiles)
- OpenAPI spec: `../openapi/assetBookingManagementOpenAPISpec.yaml`
- Actuator: `http://localhost:8081/actuator/health` (port 8081)

## Environment variables

Copy `../sample.env` to `../.env`. Key variables:

| Variable | Description |
|----------|-------------|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` | PostgreSQL connection |
| `JWT_SECRET` | JWT signing key (min 32 chars) |
| `JWT_EXPIRY_SECONDS`, `JWT_REFRESH_SECONDS` | Token lifetimes |
| `SPRING_PROFILES_ACTIVE` | Active profile (default: `dev`) |
| `CORS_ALLOWED_ORIGINS` | Frontend URL for CORS |
| `MAIL_USERNAME`, `MAIL_PASSWORD` | Mailtrap SMTP credentials |

## Database

- PostgreSQL 18 with **Flyway** migrations (auto-applied on startup)
- Schema: `asset_booking_mgm`
- Hibernate DDL: `none` (schema managed by Flyway)
- Seed data: `../initial_state.sql`
- Exclusion constraint `no_overlapping_bookings` prevents double-booking via GiST index on `tstzrange`

## Key dependencies

- **JWT auth**: JJWT 0.12.6, access + refresh token flow
- **LDAP**: Spring Security LDAP + embedded UnboundID server (dev)
- **Mapping**: MapStruct 1.6.3 + Lombok 1.18.38
- **Doc**: springdoc-openapi 3.0.3 + Swagger UI 5.32.8
- **QR**: ZXing 3.5.4
- **Rate limiting**: Bucket4j 8.10.1
- **Observability**: Micrometer + OpenTelemetry + Prometheus + Logstash
- **Testing**: JUnit 5, Testcontainers 1.20.4, MockMvc

## Debugging

When running via `compose.backend.dev.yaml`, a JDWP debug port is available on `localhost:5005`. Attach your IDE's remote debugger to that port.

## Security scanning

```bash
# OWASP dependency check (requires NVD_API_KEY in .env)
make dep-check

# With SonarQube (requires SONAR_TOKEN)
mvn verify -Psonar
```
