FROM openjdk:11-jdk
VOLUME /tmp
COPY ./build/libs/maat-courtdata-api.jar app.jar
EXPOSE 8090
CMD ["java","-jar","app.jar"]
