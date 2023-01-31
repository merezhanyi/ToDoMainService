FROM gradle:7.6.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jre-alpine

# create non-root user and group
# -l and static IDs assigned to avoid delay in lookups and system logging
# ARG THE_USER_ID=1001
# ARG THE_GROUP_ID=1001
# RUN DEBIAN_FRONTEND=noninteractive && \
#     /usr/sbin/groupadd -g $THE_GROUP_ID spring && \
#     /usr/sbin/useradd -l -u $THE_USER_ID -G spring -g $THE_GROUP_ID spring && \
#     mkdir logs && chgrp spring logs && chmod ug+rwx logs

# run as non-root
# USER spring:spring

# main REST service and OpenAPI /swagger-ui/index.html
EXPOSE 8080
# actuator metrics /actuator/health, /actuator/prometheus
EXPOSE 8081

COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
