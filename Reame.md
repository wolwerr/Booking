# Spring Boot Project for Bookings and Blocks

This project allows users to manage bookings and date blocks for properties. It uses a RESTful service for creating, updating, and deleting these entries.

## Libraries and Tools Used

### 1. Spring Boot
- **Description:** Framework for building stand-alone, production-grade Spring-based applications.
- **Version:** 3.1.5
- [Official Documentation](https://spring.io/projects/spring-boot)

### 2. Spring Data JPA
- **Description:** Spring framework for data persistence and ORM.
- [Official Documentation](https://spring.io/projects/spring-data-jpa)

### 3. H2 Database
- **Description:** In-memory database written in Java.
- [Official Documentation](https://www.h2database.com/html/main.html)

### 4. Springdoc OpenAPI (Swagger)
- **Description:** Library for automatic API documentation generation using the OpenAPI standard.
- **Version:** 2.2.0
- [Official Documentation](https://springdoc.org/)

### 5. Lombok
- **Description:** Lombok is a Java library that automatically plugs into your editor and build tools, making Java code more concise by reducing boilerplate code for model/data objects, such as getters, setters, equals, hashCode, toString, and more.
- [Official Documentation](https://projectlombok.org/)

### Docker
- **Description:** Docker is a platform designed to make it easier to create, deploy, and run applications by using containers. Containers allow developers to package up an application with all the parts it needs, including libraries and other dependencies, and ship it all out as one package.
- [Official Documentation](https://docs.docker.com/)

### Docker Compose
- **Description:** Docker Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a `docker-compose.yml` file to configure your application's services, and then start and run the entire app with a single `docker-compose up` command.
- [Official Documentation](https://docs.docker.com/compose/)

## How to Run the Project

1. Clone the repository.
2. Navigate to the project folder and run `mvn spring-boot:run` (assuming you have Maven installed).
3. Access the API through the URL `http://localhost:8080/`.
4. To view the Swagger documentation, visit `http://localhost:8080/swagger-ui.html`.