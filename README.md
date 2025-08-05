# 🌱 Carbon Calculator - Action Labs Challenge

This repository contains the **backend implementation** of a Carbon Footprint Calculator, developed as part of a technical challenge. The application is built using **Java** and **Spring Boot**, following best practices for maintainability, robustness, and clean code.

---

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 3.3.4**
  - Spring Web
  - Spring Data MongoDB
  - Spring Security
  - Spring Boot Validation
- **MongoDB** (via Docker)
- **Docker & Docker Compose**
- **Lombok**
- **Springdoc OpenAPI** (Swagger)

---

## ⚙️ Prerequisites

Ensure you have the following installed on your machine:

- [JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Docker Compose](https://docs.docker.com/compose/install/) (bundled with Docker Desktop)

---

## 🚀 How to Run the Application

### 1. Clone the Repository

```bash
git clone https://github.com/wilian-moraes/al-carbon-calc-server.git
cd al-carbon-calc-server
```

### 2. Start MongoDB with Docker

The project uses Docker Compose to spin up MongoDB and auto-seeds it with emission factors using `init-mongo.js`.

```bash
docker compose up
```

### 3. Run the Spring Boot App

#### Option A: Using an IDE (Recommended)

- Import as a Gradle project.
- Run the `CarbonCalculatorApplication.java` main class.

#### Option B: Using the Command Line

```bash
# For Windows
./gradlew.bat bootRun

# For macOS/Linux
./gradlew bootRun
```

> The application will be available at: `http://localhost:8085`

---

## 📚 API Documentation

Interactive documentation is available via Swagger UI:

🔗 [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)

---

## 📌 Recommended Usage Flow

Use the following endpoints in this order:

1. **POST `/open/start-calc`**  
   Create a calculation record with initial user data. Returns an `id`.

2. **PUT `/open/info`**  
   Submit consumption data (energy, transportation, and waste) using the previously generated `id`.

3. **GET `/open/result/{id}`**  
   Retrieve the final carbon footprint result using the `id`.

---

## ✨ Improvements and Best Practices Implemented

- **Layered Architecture**  
  Clear separation between `Controller`, `Service`, and `Repository` layers.

- **Input Validation**  
  Bean Validation (`@Valid`) on DTOs ensures input data integrity.

- **Global Exception Handling**  
  `@ControllerAdvice` with a centralized `RestExceptionHandler` for clean error responses (`400`, `404`, `409` etc.).

- **Constructor-based Dependency Injection**  
  Using Lombok’s `@RequiredArgsConstructor` for cleaner and safer injection.

- **Logging**  
  SLF4J is used for tracking key events and errors.

- **Basic Security**  
  Public endpoints under `/open/**` are accessible using Spring Security with initial configuration.

---

## 👤 Author

Developed by [Wiliam Moraes](https://github.com/wilian-moraes)