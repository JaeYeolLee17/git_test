FROM gcr.io/distroless/java:8

EXPOSE 8092

VOLUME /logs

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

