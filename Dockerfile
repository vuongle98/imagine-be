FROM eclipse-temurin:17

WORKDIR /app

COPY build/libs/imagine-0.0.1-SNAPSHOT.jar /app/imagine.jar

ENTRYPOINT ["java", "-jar", "imagine.jar"]
