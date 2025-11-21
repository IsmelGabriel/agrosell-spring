# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Si usas mvnw, dale permiso antes de build (a veces necesario localmente)
RUN chmod +x mvnw || true
RUN ./mvnw -B -DskipTests clean package

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
# copia el jar generado (comodín para cualquier nombre)
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
