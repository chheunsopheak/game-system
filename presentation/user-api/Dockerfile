# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml files to cache dependencies
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

# Preload dependencies
RUN mvn dependency:go-offline -B

# Copy full source
COPY core ./core
COPY domain ./domain
COPY infrastructure ./infrastructure
COPY share ./share
COPY presentation ./presentation

# Build the entire project
RUN mvn clean install -DskipTests

# Stage 2: Runtime
FROM openjdk:21-jdk-slim

RUN useradd -m appuser
USER appuser

WORKDIR /app

COPY --from=build /app/presentation/user-api/target/user-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 5001

CMD ["java", "-jar", "app.jar"]
