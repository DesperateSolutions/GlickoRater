FROM anapsix/alpine-java:8
WORKDIR /app

ADD ./build/libs/glicko-rater-*.jar /app

CMD java -jar glicko-rater-*.jar
