# About

## Asset Booking Management System

The **Asset Booking Management System** is a secure, maintainable internal platform for managing shared company resources such as meeting rooms, laptops, parking spaces, desks, and books. It provides a centralized solution for browsing assets, creating and approving bookings, preventing conflicts, and producing usage reports.

***

## Purpose

Organizations with shared physical resources need a reliable way to allocate them fairly, visibly, and efficiently. Without a dedicated solution, booking processes often become fragmented across spreadsheets, email, and informal agreements — increasing the risk of double booking, poor resource utilization, and weak auditability.

This system addresses that by centralizing:

- Asset discovery and availability checks
- Booking creation and full lifecycle management
- Category-specific booking rules and approval workflows
- Email notifications and calendar synchronization
- Utilization reporting

***

## User Roles

The platform supports three main roles:

| Role | Responsibilities |
|---|---|
| **Employee** | Browse assets, check availability, create/modify/cancel own bookings, use QR quick-booking, receive confirmations |
| **Manager** | Review booking approval requests for restricted asset categories, access usage reports |
| **Admin** | Manage users, assets, asset categories, booking rules, and operational data |

***

## Architecture

The solution is built on a **modular monolith (modulith)** architecture — a single Spring Boot deployable internally partitioned into domain-oriented modules. This approach provides simpler deployment and operations compared to microservices while preserving clean separation of concerns.

**Backend domain modules:**

- `user` — identities, roles, and profile management
- `asset-category` — category definitions and booking-rule configuration
- `asset` — individual assets, metadata, availability, and QR association
- `booking` — reservation lifecycle, approval state, recurrence, and conflict detection
- `report` — usage statistics and historical reporting
- `core` — shared configuration, error handling, and cross-cutting infrastructure

**Frontend** is a React 19 application written in TypeScript, built with Vite, following a feature-based folder structure.

***

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Java 21+, Spring Boot, Maven |
| Frontend | React 19, TypeScript, Vite |
| Database | PostgreSQL, Flyway migrations |
| API | REST/JSON, OpenAPI/Swagger |
| CI/CD | GitLab CI, Gitflow branching |
| Code Quality | SonarQube, JaCoCo, OWASP Dependency-Check |
| Containerization | Docker / Podman |

***

## Key Business Benefits

- Reduced administrative effort for asset coordination
- Improved employee experience when reserving shared resources
- Better visibility into asset usage and demand patterns
- Lower incidence of booking conflicts through transactional conflict detection
- Stronger governance through role-based access and auditable booking workflows

***

## Document Information

| Field | Value |
|---|---|
| Version | v1.1-draft |
| Authors | Project Team |
| Date | April 20, 2026 |
| Status | Draft |