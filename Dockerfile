FROM openjdk:8-jdk-alpine
ADD target/*.jar sofa.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/sofa.jar"]