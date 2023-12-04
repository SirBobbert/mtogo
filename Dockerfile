FROM docker.io/library/openjdk:11

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY order-service/target/mtogo.jar /app/

# Define the command to run the application
CMD ["java", "-jar", "mtogo.jar"]
