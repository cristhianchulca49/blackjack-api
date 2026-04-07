# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar el pom.xml y descargar dependencias (para aprovechar la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y construir el jar
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Crear un usuario no root para mayor seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar solo el FAT JAR desde el stage de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Configurar límites de memoria y ejecución (optimizado para Render Free Tier)
ENTRYPOINT ["java", "-Xmx256m", "-jar", "app.jar"]
