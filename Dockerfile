# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Argument for the service name
ARG SERVICE

# Copy JAR files from the specified service's target directory into the container at the working directory
COPY ${SERVICE}/target/*.jar ./

# Specify the command to run on container start
CMD ["java", "-jar", "${SERVICE}-1.0-SNAPSHOT.jar"]
