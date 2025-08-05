# syntax=docker/dockerfile:1.4

# Stage 1: The Build Stage
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy all project object model (pom) files=
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

# Download all project dependencies. This layer is highly cacheable.
RUN mvn dependency:go-offline -B

# Copy the rest of the source code.
COPY . .

# Build and package the specific application.
RUN mvn clean package -pl presentation/admin-api -am -DskipTests

# Stage 2: The Runtime Stage
FROM eclipse-temurin:21-jre-alpine

# Set the working directory for the runtime container
WORKDIR /app

# Install 'curl' for the health check. Using Alpine's package manager 'apk'.
RUN apk update && apk add --no-cache curl

# Create a non-root user for security best practices
RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser

# Copy the final JAR file from the 'builder' stage.
COPY --from=builder /app/presentation/admin-api/target/admin-api-0.0.1-SNAPSHOT.jar /app/app.jar

# Define the port the container will listen on.
EXPOSE 5001

# Command to run the application.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]