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
FROM cimg/openjdk:21.0.3-node

# Set the working directory
WORKDIR /app

# Copy all project files into the container
COPY . .

# Grant execute permissions to the Maven Wrapper
RUN chmod +x ./mvnw

# Run the full production build. This command builds the frontend and backend together.
# The frontend-maven-plugin will run 'npm install' and 'npm run webapp:prod' automatically.
RUN ./mvnw package -Pprod -DskipTests

# Expose the port the application will run on
EXPOSE 8080

# The final command to start the Java application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "./target/*.jar"]
