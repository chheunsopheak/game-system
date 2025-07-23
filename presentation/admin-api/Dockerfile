# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY presentation/admin-api/pom.xml ./presentation/admin-api/
COPY presentation/user-api/pom.xml ./presentation/user-api/
COPY share/shared/pom.xml ./share/shared/
COPY domain/domain/pom.xml ./domain/domain/
COPY core/application/pom.xml ./core/application/
COPY infrastructure/external/pom.xml ./infrastructure/external/
COPY infrastructure/persistence/pom.xml ./infrastructure/persistence/
COPY infrastructure/security/pom.xml ./infrastructure/security/
RUN mvn dependency:go-offline -B
COPY presentation/admin-api/ ./presentation/admin-api/
COPY presentation/user-api/ ./presentation/user-api/
COPY share/ ./share/
COPY domain/ ./domain/
COPY core/ ./core/
COPY infrastructure/ ./infrastructure/
RUN mvn clean package -pl presentation/admin-api -am -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
RUN groupadd -r appuser && useradd -r -g appuser appuser
COPY --from=builder /app/presentation/admin-api/target/*.jar /app/app.jar
USER appuser
ENTRYPOINT ["java", "-jar", "/app/app.jar"]