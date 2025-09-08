# # Use a lightweight Java Runtime Environment as the final image
# FROM eclipse-temurin:21-jre-jammy
# WORKDIR /app
#
# # Create a non-root user for security
# RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
# USER jhipster
#
# # Copy the pre-built JAR file from your local 'target' directory
# # The JAR file must be in the same directory as this Dockerfile when you run 'flyctl deploy'
# COPY target/*.jar app.jar
#
# # Expose the port the app runs on
# EXPOSE 8080
#
# # Command to run the application
# ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]clear
# ------------------------
# Stage 1: Build with Maven
# ------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml and resolve dependencies first (to use Docker cache)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the actual project files and build
COPY src ./src
RUN mvn -B -DskipTests package

# ------------------------
# Stage 2: Run with JRE
# ------------------------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose app port (adjust if needed)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
