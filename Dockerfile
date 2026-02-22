#
# Build Stage
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean install

#
# Package Stage
#
FROM eclipse-temurin:17-jdk
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar budget-buddy-v1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "budget-buddy-v1.0.jar"]