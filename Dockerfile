FROM openjdk:11-jre-slim
WORKDIR /app

ADD ./build/libs/glicko-rater-*.jar /app

CMD java -jar glicko-rater-*.jar
