#Build a JAR file
FROM gradle:7.3.3-jdk11-alpine as builder1
WORKDIR /repo/poc
COPY . /repo/poc
RUN gradle -i build


# the second stage of our build will extract the layers
FROM openjdk:11 as builder2
WORKDIR /repo/poc
ARG JAR_FILE=/repo/poc/build/libs/poc-0.0.1-SNAPSHOT.jar
COPY --from=builder1 ${JAR_FILE} poc.jar
RUN java -Djarmode=layertools -jar poc.jar extract

# the third stage of our build will copy the extracted layers
FROM openjdk:11
WORKDIR /repo/poc
COPY --from=builder2 /repo/poc/dependencies/ ./
COPY --from=builder2 /repo/poc/spring-boot-loader/ ./
COPY --from=builder2 /repo/poc/snapshot-dependencies/ ./
COPY --from=builder2 /repo/poc/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]