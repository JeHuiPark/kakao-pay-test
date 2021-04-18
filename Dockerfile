FROM openjdk:11-slim
EXPOSE 8080
COPY build/libs/kakao-pay-test.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
