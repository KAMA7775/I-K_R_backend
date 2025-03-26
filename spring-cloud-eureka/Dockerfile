FROM oraclelinux:8-slim
RUN microdnf install -y java-21-openjdk
COPY build/libs/*.jar spring-cloud-eureka.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "spring-cloud-eureka.jar"]
