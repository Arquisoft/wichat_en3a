# Using Amazon Coretto because other images have vulnerabilities
FROM maven:3.9.9-amazoncorretto-23 AS build
COPY . /wikidataservice
WORKDIR /wikidataservice
RUN mvn install
RUN mvn clean package

FROM amazoncorretto:23 AS runtime
COPY --from=build /wikidataservice/target/wichat-webapp-0.0.1-SNAPSHOT.jar wikidataservice.jar
ENTRYPOINT ["java", "-jar", "wikidataservice.jar"]

EXPOSE 8101
