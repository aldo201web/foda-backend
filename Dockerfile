# ====== Build stage: compila el jar con Maven y Java 17 ======
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero el pom para aprovechar cache
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copiamos el código y compilamos
COPY src ./src
RUN mvn -q -DskipTests package

# ====== Run stage: imagen ligera para ejecutar ======
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/*.jar app.jar

# Render define PORT, Spring lo toma por application.properties
CMD ["sh","-c","java -jar app.jar"]