FROM openjdk:9-jdk

VOLUME /tmp
ADD ./build/libs/maat-courtdata-api.jar app.jar

EXPOSE 8080 8000

CMD java -jar app.jar
