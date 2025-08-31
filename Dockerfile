# # Stage 1: Build the frontend
# FROM node:22-slim as frontend
# WORKDIR /app
# COPY package.json package-lock.json ./
# RUN npm install
# COPY . .
# RUN npm run webapp:prod
#
# # Stage 2: Build the backend
# FROM eclipse-temurin:21-jdk-jammy as backend
# WORKDIR /app
# COPY --from=frontend /app/target/classes/static/ /app/target/classes/static/
# COPY .mvn/ .mvn/
# COPY mvnw pom.xml ./
#
# # Copy the frontend files needed by the frontend-maven-plugin
# COPY package.json package-lock.json ./
# COPY angular.json tsconfig*.json ./
# COPY src/main/webapp/ src/main/webapp/
#
# RUN chmod +x ./mvnw
# RUN ./mvnw dependency:go-offline
# COPY src/ src/
# RUN ./mvnw package -Pprod -DskipTests
#
# # Final Stage: Create the lightweight production image
# FROM eclipse-temurin:21-jre-jammy
# WORKDIR /app
# # Give the right to the user to create folders and files
# RUN chown -R 1001:0 /app
# # The folders and files should be owned by the jhipster user
# USER 1001
# COPY --from=backend /app/target/*.jar app.jar
# ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
# Use a single, comprehensive image that has both Java and Node.js
# Use a valid, official CircleCI image containing Java 21 and Node.js 22
FROM cimg/openjdk:21.0-node

# Set the working directory for the application
WORKDIR /app

# Copy all the project files into the Docker image
COPY . .

# Grant execute permissions to the Maven Wrapper script
RUN chmod +x ./mvnw

# Run the full production build using the Maven Wrapper.
# This single command builds the frontend (npm install, ng build) and the backend (java compile, package).
# The -Pprod flag activates the production profile.
# -DskipTests skips running unit tests, which is standard for a deployment build.
RUN ./mvnw package -Pprod -DskipTests

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# The final command to start the Java application when the container starts
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "./target/*.jar"]
