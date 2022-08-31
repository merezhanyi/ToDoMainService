# Main service

Main monolith service for ToDo application.

## Initial Configuration

- Java 11
- Gradle
- Spring Boot 2.7.3
- Packaging: Jar
- Database: MariaDB 10.9.2
- Dependencies: MariaDB Driver, Spring Web, Spring Data JPA, Spring Security

## Preparation and configuration

- Install on your local PC: Java, Gradle, MariaDB
- Add these apps to your PATH and check from command line (optional):
```bash
java -version
gradle --version
mariadb --version
```
- Update database related properties in `resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/YOUR_DATABASE
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

## Build and start

- To build the project use: `gradle build` (runs Unit Tests as well)
- To run the service use: `gradle bootRun` 
