# Stage 1: Build the Spring Boot JAR
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for caching dependencies)
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn -B -q -e dependency:go-offline

# Copy source code and build JAR
COPY src ./src

# Build
RUN mvn -B -q package

# Stage 2: Run the Spring Boot application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Add curl for healthcheck
RUN apk add --no-cache curl

# Expose Spring Boot default port
EXPOSE 8080

# Switch to non root user for security
RUN addgroup -S appuser && adduser -S appuser -G appuser
RUN mkdir -p /app/logs
RUN chown -R appuser:appuser /app

USER appuser

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]