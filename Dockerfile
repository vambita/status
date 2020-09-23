FROM openjdk:14
VOLUME /tmp
EXPOSE 9191 9191
ARG JAR_FILE=build/libs/status-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} status-app.jar
ENTRYPOINT ["java","-jar","/status-app.jar"]
HEALTHCHECK CMD curl -f http://localhost:9191/vmbt-status/actuator/health || exit 1