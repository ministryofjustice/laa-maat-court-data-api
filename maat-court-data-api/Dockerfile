FROM openjdk:9-jdk

VOLUME /tmp
ADD ./build/libs/maat-courtdata-api.jar app.jar
EXPOSE 8090 8091
CMD java -jar app.jar
