# Stage 1: Build using the Maven Wrapper
FROM eclipse-temurin:17-jdk-focal AS builder
WORKDIR /app

# Copy wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Make wrapper executable
RUN chmod +x mvnw

# Build the Spring Boot application
RUN ./mvnw -B package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]