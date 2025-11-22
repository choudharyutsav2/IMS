# multi-stage Dockerfile: build with Maven, run with JRE
# Stage 1: build the jar
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and download dependencies first (cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw || true
RUN mvn -B -ntp dependency:go-offline

# Copy full source and build jar
COPY . .
RUN mvn -B -ntp -DskipTests clean package

# Stage 2: runtime image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the jar produced in stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose port (optional; Render handles routing)
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
