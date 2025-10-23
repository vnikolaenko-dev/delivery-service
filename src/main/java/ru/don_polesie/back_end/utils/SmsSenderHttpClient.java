package ru.don_polesie.back_end.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class SmsSenderHttpClient {
    private static final Dotenv dotenv = Dotenv.load();
    private static String token = dotenv.get("MTS_EXOLVE_API_TOKEN");
    private static String senderNumber = dotenv.get("MTS_NUMBER");


    public static void sendSms(String recipientNumber, String text) {
        try {
            // Правильное экранирование всех специальных символов JSON
            String escapedText = text
                    .replace("\\", "\\\\")  // сначала экранируем обратные слеши
                    .replace("\"", "\\\"")  // затем кавычки
                    .replace("\b", "\\b")   // backspace
                    .replace("\f", "\\f")   // form feed
                    .replace("\n", "\\n")   // новая строка
                    .replace("\r", "\\r")   // carriage return
                    .replace("\t", "\\t");  // табуляция

            String jsonBody = String.format("""
                {
                "number": "%s",
                "destination": "%s",
                "text": "%s"
                }
                """, senderNumber, recipientNumber, escapedText);

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            String authHeader = "Bearer " + token;

            System.out.println("JSON Body:\n" + jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.exolve.ru/messaging/v1/SendSMS"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Origin", "https://dev.exolve.ru")
                    .header("User-Agent", "Mozilla/5.0 (Linux; Android) AppleWebKit/537.36")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Вывод отладочной информации ДО отправки запроса
            System.out.println("=== Debug Information ===");
            System.out.println("URL: " + request.uri());
            System.out.println("Method: POST");
            System.out.println("Headers: " + request.headers().map());
            System.out.println("Body: " + jsonBody);
            System.out.println("=========================");

            /*
            чтобы не тратить деньги

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response: " + response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("API returned error: " + response.body());
            }

            */


        } catch (Exception e) {
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

}
