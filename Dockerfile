#Build
FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime
FROM amazoncorretto:17
#define
ARG APP_VERSION=0.0.1-SNAPSHOT

WORKDIR /app
COPY --from=build /build/target/Backend-Development-*.jar /app/

EXPOSE 8080

ENV DB_URL=jdbc:postgresql://postgres-sql-ph:5432/medical_app
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.datasource.url=${DB_URL} Backend-Development-${JAR_VERSION}.jar