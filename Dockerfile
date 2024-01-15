# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy JAR files from each service's target directory into the container at the working directory
COPY customer-service/target/*.jar dashboard-service/target/*.jar delivery-service/target/*.jar order-service/target/*.jar restaurant-service/target/*.jar ./

# Specify the command to run on container start
CMD ["java", "-jar", "order-service-1.0-SNAPSHOT.jar"]
