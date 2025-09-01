# Stage 1: Build the frontend
FROM node:22-slim AS frontend
WORKDIR /app

# Copy frontend package files first
COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps

# Copy all frontend source files and build
COPY . .
RUN npm run webapp:prod

# Stage 2: Build the backend
FROM eclipse-temurin:21-jdk-jammy AS backend
WORKDIR /app

# Copy Maven wrapper and config
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Copy pre-built frontend static files
COPY --from=frontend /app/target/classes/static/ target/classes/static/

# Copy backend source files
COPY src/ src/

# Build backend JAR (skip tests)
RUN ./mvnw clean package -Pprod -DskipTests -Dmaven.test.skip=true

# Stage 3: Runtime image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Non-root user
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
USER jhipster

# Copy backend JAR
COPY --from=backend /app/target/*.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
