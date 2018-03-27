FROM anapsix/alpine-java:8

ADD build/libs/glicko-rater-1.0.jar/ /

CMD java -jar glicko-rater-1.0.jar
