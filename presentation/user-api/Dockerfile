# Stage 1: The Build Stage
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory for the build
WORKDIR /app

# Copy all pom.xml
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

# Download all dependencies. This step is highly cacheable.
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY . .

# Build the application, specifying the target module and its dependencies.=
RUN mvn clean package -pl presentation/user-api -am -DskipTests

# Stage 2: The Runtime Stage
# Use a lightweight JRE image for the final container.
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the final image
WORKDIR /app

# Install curl for health checks (using Alpine's package manager)
RUN apk add --no-cache curl

# Create a dedicated user for security
RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser

# Copy the final JAR from the build stage to the runtime image.
COPY --from=builder /app/presentation/user-api/target/user-api-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the application's port
EXPOSE 5002

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]