FROM openjdk:21-jdk-slim

# Устанавливаем curl для healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Создаем пользователя и группу для безопасности
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

# Копируем JAR файл
COPY back-end-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env

# Создаем директории для логов и импорта и даем права пользователю spring
RUN mkdir -p /app/logs /data/import && chown -R spring:spring /app /data

# Переключаемся на пользователя после настройки прав
USER spring

EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
