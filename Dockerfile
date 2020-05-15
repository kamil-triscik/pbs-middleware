#
# Build stage
#
FROM maven:3.6.3-adoptopenjdk-14 AS build

COPY pbs-middleware-api/ ./pbs-middleware-api/
COPY pbs-middleware-common/ ./pbs-middleware-common/
COPY pbs-middleware-it/ ./pbs-middleware-it/
COPY pbs-middleware-server/ ./pbs-middleware-server/
COPY pbs-middleware-client/ ./pbs-middleware-client/
COPY pbs-middleware-ui/ ./pbs-middleware-ui/
COPY .git ./.git
COPY pmd_config.xml ./pmd_config.xml
COPY pom.xml ./

RUN ls -l
RUN mvn -pl '!pbs-middleware-it' package

#
# Package stage
#
FROM adoptopenjdk/openjdk14:alpine-slim
RUN ls -l
RUN ls *
ARG JAR_FILE=./pbs-middleware-server/target/pbs-middleware-server-1.0-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} pbs-middleware-server-1.0-SNAPSHOT.jar
ENTRYPOINT ["sh","-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /pbs-middleware-server-1.0-SNAPSHOT.jar"]
#ENTRYPOINT ["sh","-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 /LLMiddlewareServer.jar"]
