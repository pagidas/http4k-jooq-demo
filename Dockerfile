FROM openjdk:8-jre-alpine

ENV APPLICATION_USER http4k
RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/http4k-jooq-demo-1.0-SNAPSHOT.jar /app/http4k-jooq-demo-1.0-SNAPSHOT.jar
WORKDIR /app

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "http4k-jooq-demo-1.0-SNAPSHOT.jar"]
