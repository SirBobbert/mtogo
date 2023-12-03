# Dockerfile for the main project
FROM openjdk:11

ONBUILD COPY . /app/src
ONBUILD RUN mvn -f /app/src/pom.xml clean package

WORKDIR /app

# Copy the application JAR file into the container
COPY target/my-app.jar /app/

CMD ["java", "-jar", "my-app.jar"]
