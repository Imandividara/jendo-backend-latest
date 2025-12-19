# Jendo App

## Overview

Jendo is a healthcare management application built with Java Spring Boot. The system provides functionality for managing users, doctors, appointments, medical records, health parameters, notifications, and learning materials. It follows a domain-driven design approach with clear separation between different bounded contexts (user, medical record, etc.).

## User Preferences

Preferred communication style: Simple, everyday language.

## System Architecture

### Architectural Pattern
The application follows a **layered domain-driven design (DDD)** architecture with CQRS (Command Query Responsibility Segregation) patterns:

- **Entity Layer**: Domain models representing core business objects
- **Repository Layer**: Data access abstraction with generic CRUD operations via `IRepository` interface and `BaseRepositoryImpl`
- **Service Layer**: Business logic with interface-implementation pattern (`IUserService` / `UserServiceImpl`)
- **Controller Layer**: REST API endpoints
- **Mapper Layer**: Object transformation between entities and DTOs using Jackson ObjectMapper

### Domain Structure
Each domain (user, medicalrecord, etc.) is self-contained with:
- `entity/` - Domain models
- `repository/` - Data access (interface + implementation)
- `service/` - Business logic (interface + implementation)
- `command/` - Write operation DTOs (CQRS command side)
- `query/` - Read operation DTOs (CQRS query side)
- `mapper/` - Entity-DTO transformations

### Common Infrastructure
Shared components in `common/` package:
- **Exceptions**: Centralized error handling with `GlobalExceptionHandler`, `BadRequestException`, `NotFoundException`
- **DTOs**: Standardized API responses (`ApiResponse`, `PaginationResponse`)
- **Repository**: Generic CRUD interface and base implementation for code reuse
- **Mapper**: Jackson-based object mapping utilities

### Configuration
- `WebConfig.java` - Web MVC configuration
- `SecurityConfig.java` - Authentication and authorization setup
- `ObjectMapperConfig.java` - JSON serialization/deserialization settings

### Testing Strategy
Comprehensive controller-level integration tests exist for all major domains (User, Doctor, Appointment, HealthParameter, Notification, LearningMaterial, JendoTest). All tests are passing.

## External Dependencies

### Framework & Build
- **Spring Boot** - Core application framework
- **Maven** - Build tool and dependency management (evidenced by `target/surefire-reports/`)

### Libraries
- **Jackson** - JSON processing and object mapping
- **Spring Security** - Authentication and authorization

### Database
- Database technology not explicitly shown in provided files, but repository pattern suggests JPA/Hibernate with a relational database

### Testing
- **JUnit/Spring Test** - Controller integration testing via Surefire test runner