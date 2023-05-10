FROM openjdk:17

LABEL author="david.vancl@tul.cz"
LABEL version=1.0.0

COPY meteo/target/meteo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]