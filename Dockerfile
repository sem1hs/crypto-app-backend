FROM openjdk:25-jdk

# Çalışma dizini
WORKDIR /app

# Gradle build sonrası jar dosyasını kopyala
COPY build/libs/crypto-app-0.0.1-SNAPSHOT.jar app.jar

# Container portu
EXPOSE 8080

# Container çalıştırma komutu
ENTRYPOINT ["java", "-jar", "app.jar"]
