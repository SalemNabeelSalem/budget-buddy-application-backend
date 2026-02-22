FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar budget-buddy-v1.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "budget-buddy-v1.0.jar"]