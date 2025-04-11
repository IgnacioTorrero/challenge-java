# Usa una imagen de Maven para compilar la app
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Usa una imagen de Java para correr la app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/challenge-java-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
