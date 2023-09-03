FROM gradle:jdk17 AS build
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN gradle clean shadowJar --no-daemon

FROM openjdk:17-jdk-slim
COPY build/libs/cleverbank-application-1.0-SNAPSHOT-all.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cleverbank-application-1.0-SNAPSHOT-all.jar"]