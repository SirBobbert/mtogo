# Use an official OpenJDK runtime as a base image
FROM openjdk:11

# Set the working directory
WORKDIR /app

# Copy the application JAR file into the container
COPY mtogo.jar /app/

# Define the command to run the application
CMD ["java", "-jar", "mtogo.jar"]
