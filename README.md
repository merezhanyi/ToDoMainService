# Next Main Focus (service)

The main monolith service for the Todo List application.

## Initial Configuration

- Java 17
- Gradle 7.6 ([gradle-wrapper](gradle/wrapper/gradle-wrapper.properties))
- Spring Boot 3.0.2 ([gradle config](build.gradle))
- Packaging: Jar
- Database: Postgres 14 ([docker config](docker-compose.yml))
- Dependencies: ([gradle config](build.gradle))
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Postgres Driver

## Local Development

### via local installation

- Install on your local PC: Java and Gradle (prefer [sdkman](https://sdkman.io)), Postgres.
- Add these apps to your PATH and check from command line (optional):

```bash
java -version
gradle --version
postgres --version
```

### via docker installation

- Install [docker](https://www.docker.com)

```sh
docker --version
```

### configuration

- Review database related properties in [application-dev.yml](src/main/resources/application-dev.yml):

```yaml
spring:
  datasource:
    url: "jdbc:postgresql://localhost:5432/todolist"
    username: "postgres"
    password: "password"
```

## development locally

Start/stop the Postgres service:

- MacOS

```sh
brew services start postgresql
brew services stop postgresql
```

- Linux or WSL

```sh
sudo service postgresql start
sudo service postgresql stop
```

Run the API server.

```sh
gradle bootRun
```

## development via docker

- Start docker environment `docker compose up`

Available services:

- Adminer [localhost:9000](http://localhost:9000/)
- Mailhog [localhost:8025](http://localhost:9000/)

Adminer credentials:

- system: `PostgreSQL`
- server: `db` <- container's name
- username: `postgres`
- password: `password`
- database: `todolist`

## Cloud Development (SHOULD BE UPDATED)

### AWS instance usage

- To build the project use: `gradle clean build` (runs Unit Tests as well)
- Create .jar file `gradle fatJar`

- Log in AWS and navigate to `https://eu-central-1.console.aws.amazon.com/ec2/home?region=eu-central-1#Instances:`
- Click on `i-0cec339516372eee1` instance to open details.
- Click `Connect`, log in linux console and run:

```sh
java -jar mainService.jar
```

- Navigate back to the instance details.
- Click to copy `Public IPv4 DNS` url and create a new link for frontend.
- Check from your browser that the service is up and running by healthcheck endpoint (should be like `http://ec2-3-126-70-26.eu-central-1.compute.amazonaws.com:8080/healthcheck`).
The service should response back: `I'm good`
- Stop the instance after usage.

## Security

- default users:

```sh
user / user (role USER)
admin / admin (roles USER, ADMIN)
```

- to log in use url `http://Public_IPv4_DNS_url:8080/api/v1/login/`
- to log out use url `http://Public_IPv4_DNS_url:8080/api/v1/logout/`

## Acturator is activated

The link to all links: [localhost:8080/actuator](http://localhost:8080/actuator)
