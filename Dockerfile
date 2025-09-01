# Stage 1: Build the frontend
FROM node:22-slim AS frontend
WORKDIR /app

# Copy frontend package files and install dependencies
COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps

# Copy all frontend source files and build
COPY . .
RUN npm run webapp:prod

# Stage 2: Build the backend
FROM eclipse-temurin:21-jdk-jammy AS backend
WORKDIR /app

# Copy Maven wrapper and configuration
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Copy pre-built frontend static files from frontend stage
COPY --from=frontend /app/target/classes/static/ target/classes/static/

# Copy backend source files
COPY src/ src/

# Build backend JAR (skip tests)
RUN ./mvnw clean package -Pprod -DskipTests -Dmaven.test.skip=true

# Stage 3: Runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create non-root user for security
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
USER jhipster

# Copy backend JAR from previous stage
COPY --from=backend /app/target/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
