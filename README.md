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

- Install [Docker](https://www.docker.com) 4.12+

```sh
docker --version
```

### configuration

- Review database related properties in [application-dev.yml](src/main/resources/application-dev.yml):

```yaml
spring:
  datasource:
    url: "jdbc:postgresql://db:5432/todolist"
    username: "postgres"
    password: "password"
```

- Seed initial SQL data from `resources` first
  - [schema.sql](src/main/resources/database/schema.sql) - DB Schema
  - [seed.sql](src/main/resources/database/seed.sql) - initial data

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

*Start DB server and seed [the initial data](src/main/resources/database/) to the `todolist` database first.*

Run the API server:

```sh
gradle bootRun
```

Test production run:

```sh
SPRING_PROFILES_ACTIVE=prod gradle bootRun
```

## development via docker

- First run `docker compose up db adminer --build --remove-orphans` and seed [the initial data](src/main/resources/database/) to the `todolist` database
- Stop docker (Ctrl+C or `docker compose down`)
- Build docker environment `docker compose up --build --remove-orphans`
- Stop docker (Ctrl+C or `docker compose down`)
- Then Start normal docker environment `docker compose up`
- After all stop docker (Ctrl+C or `docker compose down`)

OR with *local* Java build

- Start only DB `docker compose up db` (or DB and services `docker compose up db adminer mailhog`)
- Start local Java server `gradle bootRun`

Available services:

- Adminer - DB admin panel [localhost:9000](http://localhost:9000/)
- Mailhog - local mail server/client `[localhost:8025](http://localhost:8025/)

Adminer credentials:

- system: `PostgreSQL`
- server: `db` <- container's name
- username: `postgres`
- password: `password`
- database: `todolist`

## Cloud Deployment (SHOULD BE UPDATED)

### AWS instance usage

- To build the project: `gradle clean build` (runs Unit Tests as well)
- ??? Create .jar file `gradle fatJar` ???

- Setup environment variables: (SHOULD BE UPDATED FOR DB, etc.)

```sh
SPRING_PROFILES_ACTIVE=prod
```

- Log in AWS and navigate to `https://eu-central-1.console.aws.amazon.com/ec2/home?region=eu-central-1#Instances:`
- Click on `i-0cec339516372eee1` instance to open details.
- Click `Connect`, log in linux console and run:

```sh
java -jar nextmainfocus-0.0.1.jar
```

or (SHOULD BE VALIDATED)

```sh
java -jar -Dspring.profiles.active=prod mainService.jar
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

## Monitoring

Acturator is available

### Local

[localhost:8081/actuator](http://localhost:8081/actuator)

### Cloud (SHOULD BE UPDATED)

[link to actuator](https://link.to/actuator)
