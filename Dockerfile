# Use an official Maven image to build the app
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files into the container
COPY demo/pom.xml .
COPY demo/src ./src

# Package the app using Maven
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the app
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Define the command to run the app
CMD ["java", "-jar", "app.jar"]
