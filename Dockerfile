
# Primeira etapa: Construir a aplicação
FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /workspace

# Copie o pom.xml e baixe as dependências, isso melhora o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copie o código fonte e construa o JAR
COPY src src
RUN mvn clean package

# Segunda etapa: Rodar a aplicação
FROM eclipse-temurin:17-jdk

LABEL maintainer="ricardo@ricardo.net"
LABEL version="1.0"
LABEL description="Host Application"
LABEL name="host"

EXPOSE 8080

# Copie o JAR da primeira etapa
COPY --from=build /workspace/target/host-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
