# Main service

Main monolith service for ToDo application.

## Initial Configuration

- Java 11
- Maven
- Spring Boot 2.7.4
- Packaging: Jar
- Database: MariaDB 10.9.2
- Dependencies: Spring Web, MariaDB Driver, Spring Data JPA, Spring Security

## Preparation and configuration

- Install on your local PC: Java, MariaDB, Maven (Gradle deprecated)
- Add these apps to your PATH and check from command line (optional):
```bash
java -version
mvn -v
mariadb --version
```
- Update database related properties in `resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/YOUR_DATABASE
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```
_!! Don't commit your local changes for this file to GitHub !!_

## Build, start and deploy
- `mvn compile` - to build the project
- `mvn spring-boot:run` - to run the service
- `mvn package` (to build, run unit tests and create .jar file)

## AWS instance usage
- Log in AWS and navigate to `https://eu-central-1.console.aws.amazon.com/ec2/home?region=eu-central-1#Instances:`
- Start instance `i-0cec339516372eee1`
- Connect to the instance (linux console) and run:
```
java -jar mainService.jar
```
- Open in your browser url: `http://ec2-3-126-70-26.eu-central-1.compute.amazonaws.com:8080/healthcheck`.
The service should response back: `I'm good`
- Stop the instance after usage.  

## Security

- log in: user / password1 
- to log out use url `http://localhost:8080/logout`
