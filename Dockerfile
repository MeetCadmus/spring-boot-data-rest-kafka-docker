FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.kafka.bootstrap-servers=kafka:9093","/app.jar"]