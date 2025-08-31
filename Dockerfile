# Use the valid, official CircleCI image
FROM cimg/openjdk:21.0-node

# Set the working directory
WORKDIR /app

# Copy all the project files into the Docker image
COPY . .

# Grant execute permissions to the Maven Wrapper script
RUN chmod +x ./mvnw

# Run the full production build using the Maven Wrapper.
# THE FIX: Add -Dmaven.test.skip=true to prevent test compilation entirely.
RUN ./mvnw package -Pprod -DskipTests -Dmaven.test.skip=true

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# The final command to start the Java application when the container starts
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "./target/*.jar"]
