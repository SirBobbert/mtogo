# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the Spring Boot executable JAR into the container at the working directory
COPY order-service/target/order-service-1.0-SNAPSHOT.jar .

# Specify the command to run on container start
CMD ["java", "-jar", "order-service-1.0-SNAPSHOT.jar"]
