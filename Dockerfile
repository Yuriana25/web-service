# Вказуємо базовий образ
FROM openjdk:21-jdk-slim

# Встановлюємо робочу директорію
WORKDIR /app

# Копіюємо jar файл з вашого проекту в контейнер
COPY target/web-service-0.0.1-SNAPSHOT.jar app.jar

# Вказуємо, що порт 8080 буде відкритий
EXPOSE 8080

# Команда для запуску вашого додатку
ENTRYPOINT ["java", "-jar", "app.jar"]
