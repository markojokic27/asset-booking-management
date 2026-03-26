# Stage 1: Build the Spring Boot JAR
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for caching dependencies)

COPY pom.xml .


# Copy source code and build JAR
COPY src ./src
RUN mvn package

# Stage 2: Run the Spring Boot application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]