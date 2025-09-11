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
# Stage 1: Build with Maven
# ------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy everything (backend + frontend)
COPY . .

# Build backend + frontend (prod profile)
RUN ./mvnw -Pprod -DskipTests package

# ------------------------
# Stage 2: Run with JRE
# ------------------------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
