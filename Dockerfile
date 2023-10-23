FROM amazoncorretto:17.0.6-alpine3.17

LABEL br.com.morsesystems.image.authors="Diego Campos kamposdiego@outlook.com"

ENV LOG_DIR /logs

RUN apk add --no-cache bash

RUN mkdir app

ARG JAR_FILE

ADD /target/location-*.jar /app/location-app.jar

WORKDIR /app

ENTRYPOINT ["java","-XX:+UseSerialGC","-XX:MaxRAM=80m","-jar","location-app.jar", "--spring.profiles.active=dev"]