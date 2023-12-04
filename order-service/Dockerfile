# Use an official OpenJDK runtime as a parent image
FROM openjdk:20-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/order-service-1.0-SNAPSHOT.jar .

# Expose the port that your Spring Boot application is running on
EXPOSE 8081

// TODO: Make sure that the database can be reach from the container

# Specify the command to run on container start
CMD ["java", "-jar", "order-service-1.0-SNAPSHOT.jar"]
