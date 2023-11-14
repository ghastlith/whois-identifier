#Build
FROM gradle:latest AS BUILD

WORKDIR /usr/app/
COPY . .

RUN gradle build

# Package
FROM openjdk:latest

ENV JAR_NAME=whois-identifier-1.0.0.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

COPY --from=BUILD $APP_HOME .

ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME --ip=$IP
