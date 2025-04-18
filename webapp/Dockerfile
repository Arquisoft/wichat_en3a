# Using Amazon Coretto because other images have vulnerabilities
FROM maven:3.9.9-amazoncorretto-23 AS build
COPY . /webapp
WORKDIR /webapp
RUN mvn install:install-file -Dfile=lib/entities-1.0-SNAPSHOT.jar -DgroupId=com.uniovi.sdi -DartifactId=entities -Dversion=1.0-SNAPSHOT -Dpackaging=jar
RUN mvn install -DskipTests
RUN mvn clean package

FROM amazoncorretto:23 AS runtime
COPY --from=build /webapp/target/wichat-webapp-0.0.1-SNAPSHOT.jar webapp.jar
ENTRYPOINT ["java", "-jar", "webapp.jar"]

EXPOSE 8080
