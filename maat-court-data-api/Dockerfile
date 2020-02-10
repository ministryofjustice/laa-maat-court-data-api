FROM openjdk:8-jdk

VOLUME /tmp
ADD ./build/libs/maat-courtdata-api.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
