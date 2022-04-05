FROM gcr.io/distroless/java:8

EXPOSE 8081

VOLUME /logs 

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

