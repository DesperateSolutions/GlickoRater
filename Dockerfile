FROM gradle:jdk8-alpine as builder

WORKDIR /build
ADD . .
CMD ./gradlew shadowJar
CMD ls

FROM anapsix/alpine-java:8
WORKDIR /app

COPY --from=builder /build/build/libs/glicko-rater-*.jar /app

CMD java -jar glicko-rater-*.jar
