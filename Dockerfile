FROM adoptopenjdk:8-jdk-hotspot AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM adoptopenjdk:8-jdk-hotspot
COPY --from=builder build/libs/songstagram-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/app.jar"]