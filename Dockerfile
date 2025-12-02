# Multi-stage Dockerfile for Analyzer Service
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Create directories for repository and logs
RUN mkdir -p /app/repository /app/logs

# Copy Spring Boot JAR
COPY --from=build /workspace/target/*.jar /app/app.jar

# Expose port
EXPOSE 8090

# Set environment variables
ENV JAVA_OPTS="-Xmx2g -Xms512m"

# Run Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
