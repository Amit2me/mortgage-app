# Mortgage App

A production-grade Spring Boot application for mortgage interest rate management and eligibility checks, inspired by real-world ING bank business rules. Built for reliability, scalability, and modern API standards.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

## Features

- **Interest Rate API** - Retrieve up-to-date mortgage interest rates by maturity period  
- **Mortgage Eligibility** - Check user eligibility and calculate monthly payments  
- **Robust Validation** - Comprehensive input validation (boundary, business, type safety)  
- **Custom Error Handling** - Standardized API error responses for 400/404/500 scenarios  
- **OpenAPI/Swagger UI** - Interactive API documentation for easy integration  
- **Test Coverage** - Comprehensive JUnit tests for controllers, services, and edge cases  
- **YAML Configuration** - Interest rates loaded from YAML for maintainability

## Tech Stack

- **Java**: 17+
- **Framework**: Spring Boot 3.5+
- **Validation**: Jakarta Validation (Bean Validation)
- **Code Generation**: Lombok & Java Records
- **Documentation**: Swagger (OpenAPI 3) via springdoc-openapi
- **Build Tool**: Maven
- **Testing**: JUnit 5 & Mockito

## Prerequisites

Before you begin, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6 or higher
- Git

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Amit2me/mortgage-app.git
   cd mortgage-app
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080` by default.

## Configuration

### Interest Rates Setup

Configure interest rates in `src/main/resources/interest-rates.yml` following banking standards:

```yaml
interest-rates:
  - maturityPeriod: 10
    interestRate: 3.55
    lastUpdate: "2025-07-16T22:11:21Z"
  - maturityPeriod: 20
    interestRate: 4.25
    lastUpdate: "2025-07-16T22:11:21Z"
```

### Application Configuration

Customize application settings in `src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  application:
    name: mortgage-app
```

## Usage

Once the application is running, you can:

1. Access the Swagger UI at `http://localhost:8080/swagger-ui.html`
2. Use the REST endpoints directly
3. View API documentation at `http://localhost:8080/v3/api-docs`

## API Documentation

Interactive Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Get Interest Rates

**GET** `/api/interest-rates`

Returns a list of all mortgage interest rates by maturity period.

**Response Example:**
```json
[
  {
    "maturityPeriod": 10,
    "interestRate": "3.55%",
    "lastUpdate": "2025-07-16T22:11:21Z"
  },
  {
    "maturityPeriod": 20,
    "interestRate": "4.25%",
    "lastUpdate": "2025-07-16T22:11:21Z"
  }
]
```

### Check Mortgage Eligibility

**POST** `/api/mortgage-check`

Checks mortgage eligibility and calculates monthly payment.

**Request Example:**
```json
{
  "income": 70000,
  "maturityPeriod": 20,
  "loanValue": 250000,
  "homeValue": 350000,
  "firstName": "Amit",
  "lastName": "Jha",
  "dateOfBirth": "1985-01-20",
  "gender": "M"
}
```

**Response Example:**
```json
{
  "feasible": true,
  "monthlyCost": 1322.19
}
```

### Error Handling

The API provides standardized error responses:

- **400 Bad Request**: Invalid input parameters
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server-side errors

**Error Response Format:**
```json
{
  "timestamp": "2025-07-16T15:41:38.910Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for request.",
  "path": "/api/mortgage-check",
  "validationErrors": [
    "income: must be greater than 0"
  ]
}
```

## Testing

Run all tests:
```bash
mvn test
```

Run with coverage report:
```bash
mvn test jacoco:report
```

The test suite includes:
- Unit tests for controllers and services
- Integration tests for API endpoints
- Validation and error handling tests
- Edge case scenarios

## Project Structure

```
src/
└── main/
    ├── java/com/ing/assessment/mortgage/
    │   ├── common/                     # Common Constants
    │   ├── controller/                 # REST controllers
    │   ├── dto/                        # Data Transfer Objects
    │   ├── exception/                  # Custom exceptions and handlers
    │   ├── mapper/                     # Object mappers
    │   ├── model/                      # Domain models
    │   ├── service/                    # Business logic services
    │   └── config/                     # Configuration classes
    └── resources/
        ├── application.yml             # Application configuration
        └── interest-rates.yml          # Interest rates data

└── test/
    └── java/com/ing/assessment/mortgage/
        ├── controller/            # Controller tests
        ├── exception/             # Exception tests
        ├── mapper/                # Mapping tests
        └── service/               # Service tests
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request



