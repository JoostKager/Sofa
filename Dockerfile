FROM openjdk:14-jdk-alpine
VOLUME /tmp
COPY target/*.jar sofa.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/sofa.jar"]