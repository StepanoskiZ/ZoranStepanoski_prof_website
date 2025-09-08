# Use a lightweight Java Runtime Environment as the final image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root user for security
RUN groupadd -r jhipster && useradd -r -g jhipster jhipster
USER jhipster

# Copy the pre-built JAR file from your local 'target' directory
# The JAR file must be in the same directory as this Dockerfile when you run 'flyctl deploy'
COPY target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]clear
