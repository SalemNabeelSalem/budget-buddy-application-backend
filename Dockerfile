# Stage 1: Build using Maven with Temurin 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy only the pom first to leverage Docker layer caching for dependencies
COPY pom.xml .

# If you use a settings.xml or .mvn directory, copy them here for reproducible builds
# COPY settings.xml .
# COPY .mvn .mvn

# Download dependencies to cache them
RUN mvn -B -ntp dependency:go-offline

# Copy source and resources
COPY src src

# If you have other resources (e.g., application.yml), copy them too:
# COPY src/main/resources src/main/resources

# Build the project and skip tests to speed up image builds
RUN mvn -B package -DskipTests -DskipITs

# Stage 2: Runtime image using Temurin JRE 21 (jammy)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built jar from the builder stage. Uses a glob to avoid hardcoding artifact name.
COPY --from=builder /app/target/*.jar budget-buddy-v1.0.jar

# Expose the application port (adjust if your app uses a different port)
EXPOSE 8080

# Optional: run as non-root user (uncomment if desired)
# RUN addgroup --system app && adduser --system --ingroup app app
# USER app

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "budget-buddy-v1.0.jar"]