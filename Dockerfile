# Stage 1: Build using Maven on JDK 21
FROM eclipse-temurin:21-jdk-focal AS builder
WORKDIR /app

# If you use the Maven Wrapper, copy it and .mvn first for caching
COPY mvnw .
COPY .mvn .mvn

# Copy pom and download dependencies first to leverage Docker cache
COPY pom.xml .

# If you have settings.xml or other build files, copy them here
# COPY settings.xml .

# Make mvnw executable if present
RUN if [ -f ./mvnw ]; then chmod +x ./mvnw; fi

# If mvnw exists use it, otherwise rely on system mvn (image includes mvn if you prefer maven:... base)
# Run a dependency download step to cache dependencies
RUN if [ -f ./mvnw ]; then ./mvnw -B -ntp dependency:go-offline; else mvn -B -ntp dependency:go-offline; fi

# Copy the source code
COPY src src

# Build the project and skip tests to speed up image builds
RUN if [ -f ./mvnw ]; then ./mvnw -B package -DskipTests; else mvn -B package -DskipTests; fi

# Stage 2: Runtime image using Temurin JRE 21
FROM eclipse-temurin:21-jre-focal
WORKDIR /app

# Copy the built jar from the builder stage. Use a glob to avoid hardcoding artifact name.
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Recommended: run as non-root user (optional)
# RUN addgroup --system app && adduser --system --ingroup app app
# USER app

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]