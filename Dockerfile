# Multi-stage Dockerfile for Autovulcan_core
# Build stage: uses Maven to build the fat-jar
FROM maven:3.8.8-openjdk-17 AS build
WORKDIR /workspace

# Copy pom and source to use Docker cache effectively
COPY pom.xml ./
COPY src ./src

# Build the project (skip tests to speed up builds). Produces a jar in target/.
RUN mvn -B -DskipTests package

# Runtime stage: small JRE image
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Copy the built jar (prefer jar-with-dependencies)
COPY --from=build /workspace/target/*-jar-with-dependencies.jar /app/app.jar
COPY --from=build /workspace/target/*.jar /app/app.jar

# Expose default analyzer server port
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
