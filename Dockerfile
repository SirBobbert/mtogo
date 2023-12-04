# Use the official OpenJDK 11 base image
FROM openjdk:11

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY order-service/target/order-service-1.0-SNAPSHOT.jar /app/

# Define the command to run the application
CMD ["java", "-jar", "order-service-1.0-SNAPSHOT.jar"]
