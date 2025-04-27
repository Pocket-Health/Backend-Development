# Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
#RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime
FROM amazoncorretto:17
ARG APP_VERSION=0.0.1-SNAPSHOT

WORKDIR /app

# Используем `COPY` с wildcard
COPY --from=build /build/target/*.jar /app/app.jar

EXPOSE 8080

ENV DB_URL=jdbc:postgresql://postgres-sql-ph:5432/medical_app

CMD java -jar -Dspring.datasource.url=${DB_URL} app.jar
