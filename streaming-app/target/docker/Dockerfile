FROM anapsix/alpine-java:latest
ADD 0/streaming-app-assembly-0.1.jar /project/streaming-app-assembly-0.1.jar
COPY 1/scripts /project/
ENTRYPOINT ["\/project\/start.sh"]
CMD ["\/project\/", "streaming-app", "0.1"]