# Use an official OpenJDK runtime as a parent image
FROM openjdk:16-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Argument for the service name
ARG SERVICE

# Copy JAR file from the specified service's target directory into the container at the working directory
COPY ${SERVICE}/target/${SERVICE}-*.jar app.jar

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar"]
