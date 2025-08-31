# Stage 1: Build the frontend using a lightweight Node.js image
FROM node:22-slim as frontend
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run webapp:prod

# Stage 2: Build the backend using a Java Development Kit (JDK) image
FROM eclipse-temurin:21-jdk-jammy as backend
WORKDIR /app
# Copy the pre-built frontend from the 'frontend' stage
COPY --from=frontend /app/target/classes/static/ /app/target/classes/static/
# Copy Maven configuration and give it execute permissions
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
# This will print the contents of the pom.xml to the build log
RUN cat pom.xml
# Download dependencies, then copy source to leverage Docker layer caching
RUN ./mvnw dependency:go-offline
COPY src/ src/
# Build the final JAR, skipping BOTH test execution AND compilation
RUN ./mvnw package -Pprod -DskipTests -Dmaven.test.skip=true

# Final Stage: Create the final, lightweight production image with only a Java Runtime (JRE)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Create a non-root user for security
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
USER jhipster
# Copy the final application JAR from the 'backend' stage
COPY --from=backend /app/target/*.jar app.jar
# The command to run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
