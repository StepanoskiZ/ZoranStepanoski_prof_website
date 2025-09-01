# ===============================
# Stage 1: Build the frontend
# ===============================
FROM node:22-slim AS frontend
WORKDIR /app

# Install dependencies
COPY package.json package-lock.json ./
RUN npm ci --legacy-peer-deps

# Copy frontend source and build
COPY . .
RUN npm run webapp:prod

# ===============================
# Stage 2: Build the backend
# ===============================
FROM eclipse-temurin:21-jdk-jammy AS backend
WORKDIR /app

# Copy Maven wrapper and configuration
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Copy pre-built frontend static files
COPY --from=frontend /app/target/classes/static/ target/classes/static/

# Copy backend source code
COPY src/ src/

# Build the backend JAR (skip tests)
RUN ./mvnw clean package -Pprod -DskipTests -Dmaven.test.skip=true

# ===============================
# Stage 3: Production runtime
# ===============================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create non-root user
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
USER jhipster

# Copy the backend JAR from the build stage
COPY --from=backend /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
