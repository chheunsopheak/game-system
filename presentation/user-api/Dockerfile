# -------- Stage 1: Build --------
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only root pom and module poms to cache dependencies
COPY pom.xml .
COPY domain/domain/pom.xml domain/domain/pom.xml
COPY core/application/pom.xml core/application/pom.xml
COPY infrastructure/persistence/pom.xml infrastructure/persistence/pom.xml
COPY infrastructure/security/pom.xml infrastructure/security/pom.xml
COPY infrastructure/firebase/pom.xml infrastructure/firebase/pom.xml
COPY infrastructure/external/pom.xml infrastructure/external/pom.xml
COPY share/shared/pom.xml share/shared/pom.xml
COPY presentation/user-api/pom.xml presentation/user-api/pom.xml

# Pre-download dependencies (build cache friendly)
RUN mvn -B dependency:go-offline

# Copy full source for target module and its dependencies
COPY domain/domain domain/domain
COPY core/application core/application
COPY infrastructure/persistence infrastructure/persistence
COPY infrastructure/security infrastructure/security
COPY infrastructure/firebase infrastructure/firebase
COPY infrastructure/external infrastructure/external
COPY share/shared share/shared
COPY presentation/user-api presentation/user-api

# Build target module only
RUN mvn -pl presentation/user-api -am clean package -DskipTests

# -------- Stage 2: Runtime --------
FROM openjdk:21-jdk-slim

RUN useradd -m appuser
USER appuser

WORKDIR /app

COPY --from=build /app/presentation/user-api/target/user-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 5001

CMD ["java", "-jar", "app.jar"]
