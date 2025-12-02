# Multi-stage Dockerfile for Autovulcan_core
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Copy ONLY the fat-jar — DO NOT overwrite it
COPY --from=build /workspace/target/*-jar-with-dependencies.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
