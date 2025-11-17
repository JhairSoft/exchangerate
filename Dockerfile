# --- Etapa de Construcción(Build Stage) ---
# Usamos la imagen de Maven 3.9.11 con OpenJDK 17 (Eclipse Temurin)
FROM maven:3.9.11-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copiamos el pom.xml para descargar las dependecias.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del códgo fuente
COPY src ./src

# Compilamos la aplicación y la empaquetamos en un JAR, omitiendo los tests
RUN mvn package -DskipTests

# --- Etapa de Eejcución (Run Stage) ---
# Usamo una imagen ligera de OpenJDK 17 solo con el JRE
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo.
WORKDIR /app

# Copiamos el JAR compilado desde la etapa de construcción.
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 7070, que es el que la aplicación utiliza.
EXPOSE 7070

# Comando para ejecutar la plicación cuando se inicie el contenedor.
ENTRYPOINT [ "java", "-jar", "app.jar"]
