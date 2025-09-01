# Stage 1: Build everything (backend + frontend) with Maven
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper and pom
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

# Download dependencies first (caching layer)
RUN ./mvnw dependency:go-offline

# Copy all source code
COPY . .

# Build the full application (frontend included)
RUN ./mvnw clean package -Pprod -DskipTests -Dmaven.test.skip=true

# Stage 2: Create lightweight runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Non-root user for security
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
USER jhipster

# Copy final JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
