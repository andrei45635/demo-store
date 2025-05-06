# Store Management API

A RESTful Spring Boot application for managing store inventory, products and pricing, with JWT‑based authentication.

## Features

* Product CRUD (add, find, update, delete)
* Price management (change unit price)
* Inventory tracking
* JWT authentication and role‑based access control
* Centralised error handling & structured logging

## Technology Stack

| Layer        | Tech                            |
| ------------ | ------------------------------- |
| Runtime      | Java 17, Spring Boot 3.2.5      |
| Security     | Spring Security, JJWT           |
| Persistence  | Spring Data JPA, PostgreSQL     |
| Build / Test | Maven, JUnit 5, Mockito, Lombok |
| Local tests  | H2 in‑memory database           |

## Getting Started

### Prerequisites

* JDK 17+
* Maven 3.8+
* PostgreSQL 12+
* Git

### Installation

1. **Clone**

   ```bash
   git clone git@github.com:andrei45635/demo-store.git
   ```

2. **Create database**

    
Run the commands in the ```create_tables.sql``` file

3. **Configure connection**

   Edit `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/storedb
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```
   
   with your credentials and configuration 


4. **Build**

   ```bash
   mvn clean install
   ```

5. **Run**

   ```bash
   mvn spring-boot:run
   ```

The app starts on `http://localhost:8080`.

## API Documentation

Swagger: `http://localhost:8080/swagger-ui/index.html`  

## Authentication

1. **Login** – `POST /api/auth/login` with:

   ```json
   { "username": "admin", "password": "admin123" }
   ```

2. **Use token**

   ```text
   Authorization: Bearer <jwt>
   ```

## Roles

| Role       | Capabilities                           |
| ---------- | -------------------------------------- |
| `ADMIN`    | full access                            |
| `MANAGER`  | manage products & prices               |
| `EMPLOYEE` | basic product & inventory ops          |
| `CUSTOMER` | view catalogue|


## Testing

```bash
mvn test
```

* Unit tests mock repositories with Mockito.

## Error Handling

Errors return a JSON envelope like this:

```json
{
   "status": 404,
   "message": "Product not found",
   "path": "/api/products/99",
   "timestamp": "2025-05-06T12:34:56Z"
}
```

---

