# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only pom.xml files first for better layer caching
COPY pom.xml .
COPY core/application/pom.xml ./core/application/
COPY domain/domain/pom.xml ./domain/domain/
COPY infrastructure/persistence/pom.xml ./infrastructure/persistence/
COPY infrastructure/security/pom.xml ./infrastructure/security/
COPY infrastructure/firebase/pom.xml ./infrastructure/firebase/
COPY infrastructure/external/pom.xml ./infrastructure/external/
COPY share/shared/pom.xml ./share/shared/
COPY presentation/admin-api/pom.xml ./presentation/admin-api/
COPY presentation/user-api/pom.xml ./presentation/user-api/

# Download all dependencies first (faster rebuilds if code changes)
RUN mvn dependency:go-offline -B

# Now copy all source files
COPY core ./core
COPY domain ./domain
COPY infrastructure ./infrastructure
COPY share ./share
COPY presentation ./presentation

# Build the full project
RUN mvn clean install -DskipTests

# Stage 2: Runtime
FROM openjdk:21-jdk-slim

# Use a non-root user (optional but recommended for security)
RUN useradd -m appuser
USER appuser

WORKDIR /app

# Copy only the built JAR
COPY --from=build /app/presentation/admin-api/target/admin-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 5002

# Run the JAR
CMD ["java", "-jar", "app.jar"]
