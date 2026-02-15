<img alt="Spring Boot" src="/src/main/resources/images/springboot-logo.png"/>

# Budget Buddy Application

A concise, production-ready Spring Boot application scaffold with Maven for building, testing, and running.

## Features
- REST API starter with Spring Boot
- Configuration via `application.properties` / `application.yml`
- Unit and integration test setup (JUnit + Mockito)
- Maven build and lifecycle scripts

## Tech Stack
- Java 17+
- Spring Boot
- Maven
- JUnit 5

## Prerequisites
- Java 17 or later
- Maven 3.6+
- Git

## Quick Start

1. Clone the repo
   ```bash
   git clone https://github.com/SalemNabeelSalem/your-repo.git
   cd your-repo

2. Build
   ```bash
   mvn clean package
   ```

3. Run (development)
   ```bash
   mvn spring-boot:run
   ```

4. Run (jar)
   ```bash
   java -jar target/*.jar
   ```

## Running Tests
```bash
mvn test
```

## Configuration
- Application properties live in `src/main/resources/application.properties` (or `application.yml`)
- Use profiles: `-Dspring.profiles.active=dev` or set the `SPRING_PROFILES_ACTIVE` environment variable


## Project Structure
- `src/main/java` — application code
- `src/main/resources` — config and static resources
- `src/test/java` — tests
- `pom.xml` — Maven build file

## Contributing
- Fork the repo, create a feature branch, open a PR.
- Keep commit messages clear and tests green.

## License
Specify your license in `LICENSE`.

## Author
GitHub `SalemNabeelSalem`.