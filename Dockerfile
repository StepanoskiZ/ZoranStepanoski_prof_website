# # ------------------------
# # Stage 1: Build with Maven
# # ------------------------
# FROM maven:3.9.9-eclipse-temurin-21 AS build
# WORKDIR /app
#
# # Copy everything (backend + frontend)
# COPY . .
#
# # Build backend + frontend (prod profile)
# RUN ./mvnw -Pprod -DskipTests package
#
# # ------------------------
# # Stage 2: Run with JRE
# # ------------------------
# FROM eclipse-temurin:21-jre-jammy
# WORKDIR /app
#
# # Copy the built JAR from the build stage
# COPY --from=build /app/target/*.jar app.jar
#
# # Expose app port
# EXPOSE 8080
#
# # Run the app
# ENTRYPOINT ["java", "-jar", "app.jar"]
# ------------------------
# Stage 1: Build frontend
# ------------------------
FROM node:20 AS frontend
WORKDIR /app

# Install dependencies
COPY src/main/webapp/package*.json ./
RUN npm install

# Build frontend
COPY src/main/webapp ./
RUN npm run build

# ------------------------
# Stage 2: Build backend with Maven
# ------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy backend source
COPY . .

# Copy built frontend into backend resources
COPY --from=frontend /app/dist ./src/main/resources/static

# Package Spring Boot app (skip tests for speed)
RUN ./mvnw -Pprod -DskipTests package

# ------------------------
# Stage 3: Run with JRE
# ------------------------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
