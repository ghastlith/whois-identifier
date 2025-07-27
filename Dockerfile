#Build
FROM gradle:8.10.1-jdk17 AS build

WORKDIR /usr/app/
COPY . .

RUN gradle build

# Package
FROM openjdk:17

ENV JAR_NAME=whois-identifier-1.0.0.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

COPY --from=build $APP_HOME .

SHELL [ "/bin/bash", "-c" ]

ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME --ip=$IP
