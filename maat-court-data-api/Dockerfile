FROM amazoncorretto:21-alpine
RUN mkdir -p /opt/maat-courtdata-api/
WORKDIR /opt/maat-courtdata-api/
COPY ./build/libs/maat-courtdata-api.jar /opt/maat-courtdata-api/app.jar
RUN addgroup -S appgroup && adduser -u 1001 -S appuser -G appgroup
USER 1001
EXPOSE 8090 8100
CMD java -jar app.jar