# multi-stage Dockerfile: build with Maven, run with JRE

# Stage 1: build the jar
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies by copying pom first
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

# Copy the jar produced in stage 1 into the WORKDIR as app.jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the jar using relative path (inside WORKDIR)
ENTRYPOINT ["java","-jar","app.jar"]
