# =========================================================================
# ESTÁGIO 1: BUILD (Compila o código Java e gera o arquivo .jar)
# =========================================================================
# Usa uma imagem que já vem com Maven + Java 21 instalados.
# (Atenção: se no seu pom.xml o <java.version> for 17, troque "21" por "17" aqui e no Estágio 2)
FROM maven:3.9-eclipse-temurin-21 AS build

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia primeiro apenas o pom.xml e baixa as dependências.
# Truque de cache do Docker: se você não mudar o pom.xml, os próximos deploys ficam muito mais rápidos!
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Agora copia o resto do código-fonte (pasta src/)
COPY src ./src

# Compila o projeto e gera o arquivo .jar dentro de /app/target/
# -DskipTests pula os testes durante o build para não tentar conectar no banco nessa etapa
RUN mvn clean package -DskipTests

# =========================================================================
# ESTÁGIO 2: RUNTIME (Imagem final super leve só para rodar a aplicação)
# =========================================================================
# Em produção não precisamos do Maven nem do código-fonte, só do Java (JRE) para rodar o .jar
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia APENAS o .jar gerado no Estágio 1 para dentro desta imagem limpa
COPY --from=build /app/target/*.jar app.jar

# Informa que a aplicação escuta na porta 8080 dentro do container
EXPOSE 8080

# Comando que sobe o Spring Boot quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]