FROM gcr.io/distroless/java:8

EXPOSE 8082

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

