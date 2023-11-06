FROM amazoncorretto:17-alpine
VOLUME /tmp
COPY ./build/libs/maat-courtdata-api.jar app.jar
EXPOSE 8090
CMD java -jar app.jar
