FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY src/main/resources/application-example.properties application.properties
EXPOSE 8421
ENTRYPOINT [\
  "java",\
  "-XX:+UnlockExperimentalVMOptions",\
  "-XX:+UseCGroupMemoryLimitForHeap",\
  "-XX:MaxRAMFraction=1",\
  "-XshowSettings:vm",\
  "-cp",\
  "app:app/lib/*",\
  "ca.levimiller.smsbridge.SmsBridgeApplication"\
]
