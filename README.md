# ORBIT Lite

> Environment-aware configuration management platform built with React, Spring Boot, and PostgreSQL.

ORBIT Lite is a configuration management platform designed to centralize application configuration across projects and environments.

It provides authenticated configuration management, version history, rollback, API-key based runtime access, and audit logging through a REST API.

---

## Features

### Authentication

- User registration and login
- Stateless JWT authentication
- Protected API endpoints
- Automatic frontend handling of expired/invalid authentication

### Project & Environment Management

- Create and view projects
- Manage project environments
- Environment-scoped configurations

### Configuration Management

- Create and view configurations
- Update configuration values
- Environment-specific configuration storage
- Runtime configuration retrieval

### Configuration Versioning

- Automatic configuration version history
- View previous configuration versions
- Roll back a configuration to a previous version

### API Key Management

- Create environment-scoped API keys
- List API keys
- Revoke API keys
- Use API keys for runtime configuration access

### Audit Logging

- Track configuration-related operations
- Record important application actions
- Review audit history through the dashboard

---

## Architecture

```text
                         ORBIT Lite
                              │
             ┌────────────────┴────────────────┐
             │                                 │
       React Frontend                     Runtime Clients
       React + TypeScript                    API Key
             │                                 │
             │ JWT                             │ X-API-Key
             │                                 │
             └──────────────┬──────────────────┘
                            │
                            ▼
                  Spring Boot REST API
                            │
              ┌─────────────┼─────────────┐
              │             │             │
              ▼             ▼             ▼
         Controllers     Security      Services
                            │             │
                       JWT + API Key      │
                                          ▼
                                     Repositories
                                          │
                                          ▼
                                      PostgreSQL
```

### Backend layers

```text
controller/
    REST API endpoints

service/
    Business logic

repository/
    Database access through Spring Data JPA

entity/
    JPA persistence models

dto/
    API request and response models

security/
    JWT and API-key authentication

exception/
    Application exception handling

config/
    Spring and security configuration
```

---

## Runtime Configuration Flow

Applications can retrieve environment-specific configuration through the runtime API using an environment API key.

```text
Application
     │
     │ X-API-Key
     ▼
/api/v1/runtime/configurations
     │
     ▼
API Key Authentication
     │
     ▼
Environment Resolution
     │
     ▼
Configuration Service
     │
     ▼
PostgreSQL
     │
     ▼
Runtime Configuration
```

This separates human/admin access from application runtime access.

---

## Tech Stack

### Frontend

| Technology | Purpose |
|---|---|
| React 19 | User interface |
| TypeScript | Type-safe frontend development |
| Vite | Frontend build tooling |
| React Router | Client-side routing |
| Axios | REST API communication |

### Backend

| Technology | Purpose |
|---|---|
| Java 21 | Backend language |
| Spring Boot 4.1.1 | REST API framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Persistence layer |
| PostgreSQL | Relational database |
| JJWT 0.12.6 | JWT generation and validation |
| Bean Validation | Request validation |
| Spring Cache | Caching support |

---

## API Overview

### Authentication

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
```

### Projects

```text
GET  /api/v1/projects
POST /api/v1/projects
GET  /api/v1/projects/{projectId}
```

### Environments

```text
GET  /api/v1/projects/{projectId}/environments
POST /api/v1/projects/{projectId}/environments
```

### Configurations

```text
GET  /api/v1/projects/{projectId}/environments/{environmentId}/configurations

POST /api/v1/projects/{projectId}/environments/{environmentId}/configurations

PUT /api/v1/projects/{projectId}/environments/{environmentId}/configurations/{configurationId}

GET /api/v1/projects/{projectId}/environments/{environmentId}/configurations/runtime
```

### Configuration Versions

```text
GET  /api/v1/projects/{projectId}/environments/{environmentId}/configurations/{configurationId}/versions

POST /api/v1/projects/{projectId}/environments/{environmentId}/configurations/{configurationId}/rollback
```

### API Keys

```text
GET    /api/v1/projects/{projectId}/environments/{environmentId}/api-keys

POST   /api/v1/projects/{projectId}/environments/{environmentId}/api-keys

DELETE /api/v1/projects/{projectId}/environments/{environmentId}/api-keys/{apiKeyId}
```

### Audit Logs

```text
GET /api/v1/projects/{projectId}/environments/{environmentId}/audit-logs
```

### Runtime API

```text
GET /api/v1/runtime/configurations
```

### Current User

```text
GET /api/v1/users/me
```

### Health

```text
GET /api/v1/health
```

---

## Project Structure

```text
ORBIT/
│
├── orbit-backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/orbit/backend/
│   │   │   │   ├── config/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── entity/
│   │   │   │   ├── exception/
│   │   │   │   ├── repository/
│   │   │   │   ├── security/
│   │   │   │   └── service/
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── application-example.properties
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   └── mvnw.cmd
│
├── orbit-frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.ts
│   └── .env.example
│
└── README.md
```

---

## Prerequisites

Make sure the following are installed:

- Java 21
- PostgreSQL
- Node.js
- npm

---

## Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE orbit;
```

The backend uses PostgreSQL for persistence.

---

## Backend Configuration

Sensitive configuration is provided through environment variables.

Example:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/orbit}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

An example configuration is provided at:

```text
orbit-backend/src/main/resources/application-example.properties
```

Set the required environment variables before starting the backend.

### Windows PowerShell

```powershell
$env:DB_PASSWORD="your_database_password"
$env:JWT_SECRET="your_long_random_jwt_secret"
```

Optional database variables:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/orbit"
$env:DB_USERNAME="postgres"
```

---

## Running the Backend

Navigate to:

```powershell
cd orbit-backend
```

Run:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

---

## Running the Frontend

Navigate to:

```powershell
cd orbit-frontend
```

Install dependencies:

```powershell
npm install
```

Start the development server:

```powershell
npm run dev
```

The frontend runs on the Vite development server, typically:

```text
http://localhost:5173
```

---

## Frontend Environment

Create a local `.env` file based on:

```text
orbit-frontend/.env.example
```

Do not commit local environment files containing sensitive or machine-specific configuration.

---

## Testing

### Backend

Run the Spring Boot test suite:

```powershell
cd orbit-backend
.\mvnw.cmd test
```

### Frontend

Build the production frontend:

```powershell
cd orbit-frontend
npm run build
```

The production build performs TypeScript compilation followed by the Vite production build.

---

## Security

ORBIT uses multiple authentication mechanisms depending on the type of access.

### JWT Authentication

JWTs are used for authenticated dashboard and user-facing API requests.

```text
User
 │
 │ Login
 ▼
/api/v1/auth/login
 │
 ▼
JWT
 │
 ▼
Protected API endpoints
```

### API Key Authentication

Environment API keys provide application-level access to runtime configuration.

```text
Application
 │
 │ X-API-Key
 ▼
Runtime API
 │
 ▼
Environment configuration
```

Database credentials and JWT secrets are supplied through environment variables rather than being stored directly in the application configuration.

---

## Configuration Versioning

Configuration updates create version history, allowing previous values to be reviewed and restored.

```text
Configuration
      │
      ├── Version 1
      ├── Version 2
      ├── Version 3
      └── Version 4
              │
              ▼
           Rollback
              │
              ▼
       Previous configuration
```

This provides a basic safety mechanism for configuration changes.

---

## Development Status

ORBIT Lite currently includes:

- Authentication
- Project management
- Environment management
- Configuration management
- Configuration versioning
- Configuration rollback
- API-key management
- Runtime configuration API
- Audit logging
- Frontend authentication handling
- Environment-based backend secrets

---

## Future Improvements

Potential future improvements include:

- Role-based access control
- Configuration encryption at rest
- Secret rotation
- Deployment automation
- Docker-based development environment
- Automated integration tests
- Rate limiting for runtime API access
- Configuration change approval workflows
- Production deployment

---

## License

This project is currently developed as a personal engineering project.