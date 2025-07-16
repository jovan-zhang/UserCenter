FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY ./user-center-0.0.1-SNAPSHOT.jar /app/user-center-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/user-center-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]