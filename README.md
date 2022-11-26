# Main service

Main monolith service for ToDo application.

## Initial Configuration

- Java 11
- Gradle 7.6
- Spring Boot 2.7.4
- Packaging: Jar
- Database: MariaDB 10.9.2
- Dependencies: Spring Web, MariaDB Driver, Spring Data JPA, Spring Security

## Preparation and configuration

- Install on your local PC: Java, Gradle, MariaDB.
- Add these apps to your PATH and check from command line (optional):
```bash
java -version
gradle -v
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
- To build the project use: `gradle build` (runs Unit Tests as well)
- To run the service use: `gradle bootRun` 
- Create .jar file `gradle fatJar`

## AWS instance usage
- Log in AWS and navigate to `https://eu-central-1.console.aws.amazon.com/ec2/home?region=eu-central-1#Instances:`
- Click on `i-0cec339516372eee1` instance to open details.
- Click `Connect`, log in linux console and run:
```
java -jar mainService.jar
```
- Navigate back to the instance details.
- Click to copy `Public IPv4 DNS` url and create a new link for frontend.
- Check from your browser that the service is up and running by healthcheck endpoint (should be like `http://ec2-3-126-70-26.eu-central-1.compute.amazonaws.com:8080/healthcheck`).
The service should response back: `I'm good`
- Stop the instance after usage.  

## Security

- default users:
```
user / user (role USER)
admin / admin (roles USER, ADMIN)
``` 
- to log out use url `http://Public_IPv4_DNS_url:8080/logout`
