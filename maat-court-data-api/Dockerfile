FROM openjdk:11-jdk
VOLUME /tmp
COPY ./build/libs/maat-courtdata-api.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
