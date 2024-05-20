# set workdir & dependepcy
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app

# copy files for maven build
COPY pom.xml .
COPY src ./src

# run build & package with mvn
RUN mvn clean package

# run .jar file
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/uploader-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]