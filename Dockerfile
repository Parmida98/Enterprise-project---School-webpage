# Steg 1: Bygg jar-filen med Gradle (build stage)
FROM gradle:8-jdk21-alpine AS build
WORKDIR /app

# Kopiera allt projekt-innehåll in i containern
COPY . .

# Bygg Spring Boot-jar
RUN ./gradlew bootJar --no-daemon

# Steg 2: Liten runtime-bild med bara JRE + jar (run stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Kopiera jar från build-steget
COPY --from=build /app/build/libs/*.jar app.jar

# Exponera porten som Spring kör på
EXPOSE 8080

# Starta appen
ENTRYPOINT ["java", "-jar", "app.jar"]
