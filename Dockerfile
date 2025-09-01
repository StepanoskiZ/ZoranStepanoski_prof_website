# ===============================
# Stage 1: Build frontend
# ===============================
FROM node:22-slim as frontend
WORKDIR /app

# Copy frontend package files
COPY package.json package-lock.json ./
RUN npm install

# Copy frontend source and build
COPY src/main/webapp/ ./  # adjust if your frontend files are elsewhere
RUN npm run build  # or your prod build command, e.g., npm run webapp:prod

# ===============================
# Stage 2: Build backend
# ===============================
FROM eclipse-temurin:21-jdk-jammy as backend
WORKDIR /app

# Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

# Download dependencies first for caching
RUN ./mvnw dependency:go-offline

# Copy backend source
COPY src/ src/

# Copy frontend build into backend static resources
COPY --from=frontend /app/dist/ ./src/main/resources/static/ # adjust path if needed

# Build the backend JAR (skip tests)
RUN ./mvnw clean package -Pprod -DskipTests -Dmaven.test.skip=true

# ===============================
# Stage 3: Runtime image
# ===============================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create non-root user
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
USER jhipster

# Copy the built JAR
COPY --from=backend /app/target/*.jar app.jar

# Expose port for Render (default 10000 or your choice)
EXPOSE 10000

# Command to run the app
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
