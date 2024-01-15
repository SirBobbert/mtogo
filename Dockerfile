# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the placeholder file into the container at the working directory
COPY placeholder.txt .

# Specify the command to run on container start
CMD ["echo", "No application JAR found"]
