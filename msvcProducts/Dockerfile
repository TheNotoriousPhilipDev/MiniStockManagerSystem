FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 8006
ADD ./target/msvcProducts-0.0.1-SNAPSHOT.jar msvc-products.jar
ENTRYPOINT ["java", "-jar", "msvc-products.jar"]
