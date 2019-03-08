FROM anapsix/alpine-java:latest
ADD 0/weather-provider-assembly-0.1.jar /project/weather-provider-assembly-0.1.jar
COPY 1/scripts /project/
ENTRYPOINT ["\/project\/start.sh"]
CMD ["\/project\/", "weather-provider", "0.1"]