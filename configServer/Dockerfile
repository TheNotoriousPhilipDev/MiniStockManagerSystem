FROM amazoncorretto:21-alpine-jdk

WORKDIR /app
EXPOSE 8888

# Use the correct jar filename
COPY ./target/configServer-0.0.1-SNAPSHOT.jar config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]
