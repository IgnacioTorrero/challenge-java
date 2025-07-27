# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# 🔽 Instala netcat
RUN apt-get update && apt-get install -y netcat && apt-get clean

COPY --from=builder /app/target/sales-point-manager-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080
ENTRYPOINT ["/wait-for-it.sh", "mysql:3306", "--", "java", "-jar", "app.jar"]