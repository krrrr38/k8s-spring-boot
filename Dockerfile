FROM openjdk:8-jdk-alpine
ADD build/libs/k8s-spring-boot-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-server", "-jar", "app.jar" ]
