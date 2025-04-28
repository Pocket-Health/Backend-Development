# Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
