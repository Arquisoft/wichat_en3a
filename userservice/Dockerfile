# Using Amazon Coretto because other images have vulnerabilities
FROM maven:3.9.9-amazoncorretto-23 AS build
COPY . /userservice
WORKDIR /userservice
RUN mvn install
RUN mvn clean package

FROM amazoncorretto:23 AS runtime
COPY --from=build /userservice/target/userservice-0.0.1-SNAPSHOT.jar userservice.jar
ENTRYPOINT ["java", "-jar", "userservice.jar"]

EXPOSE 8100