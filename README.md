# Main service

Main monolith service for ToDo application.

## Initial Configuration

- Java 11
- Maven (Gradle deprecated)
- Spring Boot 2.7.4
- Packaging: Jar
- Database: MariaDB 10.9.2
- Dependencies: Spring Web, MariaDB Driver, Spring Data JPA, Spring Security

## Preparation and configuration

- Install on your local PC: Java, MariaDB, Maven (Gradle deprecated)
- Add these apps to your PATH and check from command line (optional):
```bash
java -version
mvn -v (gradle --version deprecated)
mariadb --version
```
- Update database related properties in `resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/YOUR_DATABASE
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```
_!! Don't commit your local changes for this file to GitHub !!_

## Buld, start and deploy
mvn compile (to build the project)
mvn spring-boot:run (to run the setvice)
mvn package (to create .jar file)

(Gradle deprecated)
- To build the project use: `gradle build` (runs Unit Tests as well)
- To run the service use: `gradle bootRun` 
