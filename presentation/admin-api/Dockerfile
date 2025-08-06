# syntax=docker/dockerfile:1.4

# Stage 1: The Build Stage
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy all project object model (pom) files
COPY pom.xml .
COPY presentation/admin-api/pom.xml ./presentation/admin-api/
COPY presentation/user-api/pom.xml ./presentation/user-api/
COPY share/shared/pom.xml ./share/shared/
COPY domain/domain/pom.xml ./domain/domain/
COPY core/application/pom.xml ./core/application/
COPY infrastructure/external/pom.xml ./infrastructure/external/
COPY infrastructure/firebase/pom.xml ./infrastructure/firebase/
COPY infrastructure/persistence/pom.xml ./infrastructure/persistence/
COPY infrastructure/security/pom.xml ./infrastructure/security/

# Preload Maven dependencies (will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy full source
COPY core ./core
COPY domain ./domain
COPY infrastructure ./infrastructure
COPY share ./shared
COPY presentation ./presentation

# Build everything so inter-module dependencies are resolved
RUN mvn clean install -DskipTests

# Stage 2: Runtime image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy just the final jar
COPY --from=builder /app/presentation/admin-api/target/admin-api-0.0.1-SNAPSHOT.jar .

EXPOSE 50001

CMD ["java", "-jar", "admin-api-0.0.1-SNAPSHOT.jar"]