FROM openjdk:17-oracle

# Meta-informação como autor e descrição (opcional)
LABEL maintainer="ricardo@ricardo.net"
LABEL version="1.0"
LABEL description="Host Application"
LABEL name="host"

# Porta que o Spring Boot vai expor (padrão é 8080, mas ajuste se necessário)
EXPOSE 8080

# Copiando o JAR empacotado para a imagem
COPY target/host-0.0.1-SNAPSHOT.jar app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","/app.jar"]