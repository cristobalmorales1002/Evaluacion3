# Usamos una imagen base con Java 21 y Maven para construir
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Construimos el .jar saltando los tests para agilizar la construcción en docker
RUN mvn clean package -DskipTests

# Usamos una imagen ligera de Java 21 para ejecutar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos el .jar generado en la etapa anterior
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]