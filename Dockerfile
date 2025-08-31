# Stage 1: Build the frontend
FROM node:22-slim as frontend
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run webapp:prod

# Stage 2: Build the backend
FROM eclipse-temurin:21-jdk-jammy as backend
WORKDIR /app
COPY --from=frontend /app/target/classes/static/ /app/target/classes/static/
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src/ src/
RUN ./mvnw package -Pprod -DskipTests

# Final Stage: Create the lightweight production image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Give the right to the user to create folders and files
RUN mkdir /app && chown -R 1001:0 /app
# The folders and files should be owned by the jhipster user
USER 1001
COPY --from=backend /app/target/*.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]