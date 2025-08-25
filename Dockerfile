# Етап 1: збірка
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Етап 2: запуск
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/herzberg-test-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
