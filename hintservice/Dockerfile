# Using Amazon Coretto because other images have vulnerabilities
FROM maven:3.9.9-amazoncorretto-23 AS build
COPY . /hintservice
WORKDIR /hintservice
RUN mvn install
RUN mvn clean package

FROM amazoncorretto:23 AS runtime
COPY --from=build /hintservice/target/hintservice-0.0.1-SNAPSHOT.jar hintservice.jar
ENTRYPOINT ["java", "-jar", "hintservice.jar"]

EXPOSE 8200
