# FROM eclipse-temurin:21-jre
# WORKDIR /app
# COPY target/demo-0.0.1-SNAPSHOT.jar budget-buddy-v1.0.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "budget-buddy-v1.0.jar"]

# Build Stage
FROM maven:3.9.4-eclipse-temurin-20 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Run Stage
FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar budget-buddy-v1.0.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/budget-buddy-v1.0.jar"]