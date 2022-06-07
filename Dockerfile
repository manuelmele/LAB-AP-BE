FROM openjdk:16
EXPOSE 8080
ADD target/we-fix.jar we-fix.jar
ENTRYPOINT ["java","-jar","/we-fix.jar"]