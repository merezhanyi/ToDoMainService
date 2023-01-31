FROM gradle:7.6.0-jdk17-alpine
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle build --no-daemon

# main REST service and OpenAPI /swagger-ui/index.html
EXPOSE 8080
# actuator metrics /actuator/health, /actuator/prometheus
EXPOSE 8081

CMD [ "gradle", "bootRun"]
