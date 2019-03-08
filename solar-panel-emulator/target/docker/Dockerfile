FROM anapsix/alpine-java:latest
ADD 0/solar-panel-emulator-assembly-0.1.jar /project/solar-panel-emulator-assembly-0.1.jar
COPY 1/scripts /project/
ENTRYPOINT ["\/project\/start.sh"]
CMD ["\/project\/", "solar-panel-emulator", "0.1"]