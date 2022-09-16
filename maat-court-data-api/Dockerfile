FROM openjdk:11-jdk
VOLUME /tmp
COPY ./build/libs/maat-courtdata-api.jar app.jar
EXPOSE 8090 8091
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8091
ENTRYPOINT ["java","-jar","app.jar"]
