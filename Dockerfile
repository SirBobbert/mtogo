# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the application JAR file into the container at the working directory
COPY target/my-app.jar .

# Specify the command to run on container start
CMD ["java", "-jar", "my-app.jar"]
