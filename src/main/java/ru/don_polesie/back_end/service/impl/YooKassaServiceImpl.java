package ru.don_polesie.back_end.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.enums.OrderStatus;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;
import ru.don_polesie.back_end.model.Order;
import ru.don_polesie.back_end.model.OrderProduct;
import ru.don_polesie.back_end.repository.OrderRepository;
import ru.don_polesie.back_end.service.YooKassaService;
import ru.don_polesie.back_end.utils.CidrUtils;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;



@Service
public class YooKassaServiceImpl implements YooKassaService {

    @Value("${security.YooKassa.shopId}")
    private String shopId;

    @Value("${security.YooKassa.secretKey}")
    private String secretKey;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final List<CidrUtils> allowedCidrs;
    private final OrderRepository orderRepository;

    @Autowired
    public YooKassaServiceImpl(OrderRepository orderRepository) throws Exception {
        this.orderRepository = orderRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();

        // Инициализация whitelist IP
        String[] cidrs = new String[]{
                "185.71.76.0/27",
                "185.71.77.0/27",
                "77.75.153.0/25",
                "77.75.156.11/32",
                "77.75.156.35/32",
                "77.75.154.128/25",
                "2a02:5180::/32"
        };
        List<CidrUtils> list = new ArrayList<>();
        for (String cidr : cidrs) {
            list.add(new CidrUtils(cidr));
        }
        this.allowedCidrs = Collections.unmodifiableList(list);
    }

    @Override
    @Transactional
    public JsonNode createPayment(String orderId) throws Exception {
        var order = orderRepository
                .findById(Long.valueOf(orderId))
                .orElseThrow(() -> new ObjectNotFoundException(""));

        var payload = mapper.createObjectNode();
        var amount = mapper.createObjectNode();
        amount.put("value", order.getTotalAmount());
        amount.put("currency", "RUB");
        payload.set("amount", amount);

        var paymentMethodData = mapper.createObjectNode();
        paymentMethodData.put("type", "bank_card");
        payload.set("payment_method_data", paymentMethodData);

        payload.put("capture", true);

        var confirmation = mapper.createObjectNode();
        confirmation.put("type", "redirect");
        confirmation.put("return_url", "http://localhost:3000/components");
        payload.set("confirmation", confirmation);

        var metadata = mapper.createObjectNode();
        metadata.put("orderId", orderId);
        metadata.put("userId", 0);
        payload.set("metadata", metadata);

        String body = mapper.writeValueAsString(payload);

        String credentials = shopId + ":" + secretKey;
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.yookassa.ru/v3/payments"))
                .header("Authorization", "Basic " + basicAuth)
                .header("Idempotence-Key", UUID.randomUUID().toString())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            JsonNode json = mapper.readTree(response.body());
            String paymentId = json.get("id").asText();
            order.setStatus(OrderStatus.PAYING);
            return json;
        } else {
            throw new RuntimeException("YooKassa createPayment error: " + response.statusCode() + " " + response.body());
        }
    }

    @Override
    public JsonNode getPayment(String paymentId) throws Exception {
        String credentials = shopId + ":" + secretKey;
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.yookassa.ru/v3/payments/" + paymentId))
                .header("Authorization", "Basic " + basicAuth)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            JsonNode json = mapper.readTree(response.body());
            return json;
        } else {
            throw new RuntimeException("YooKassa getPayment error: " + response.statusCode() + " " + response.body());
        }
    }

    @Override
    @Transactional
    public void storeNotification(JsonNode notification) {
        var orderId = extractOrderId(notification);
        var order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException(""));

        if (notification.get("event").asText().equals("payment.succeeded")) {
            order.setStatus(OrderStatus.PAID);

            for (OrderProduct orderProduct : order.getOrderProducts()) {
                var currentAmount = orderProduct.getProduct().getAmount();
                var subtractedAmount = (int) Double.parseDouble(orderProduct.getQuantity().split(" ")[0]);
                orderProduct.getProduct().setAmount(currentAmount - subtractedAmount);
            }

        }
    }

    /**
     * Проверка нотификации по документации:
     * 1. IP-адрес в whitelist.
     * 2. Тело — тип notification, есть поле event и object.
     * 3. Статус объекта актуален (сравнение с повторным запросом).
     */
    @Override
    public boolean verifyNotification(String remoteIp, JsonNode notification) {
        try {
            // 1. IP
            boolean ipAllowed = allowedCidrs.stream().anyMatch(c -> c.contains(remoteIp));
            if (!ipAllowed) {
                return false;
            }

            // 2. Структура
            if (!notification.has("type") || !notification.get("type").asText().equals("notification")) {
                return false;
            }
            if (!notification.has("event") || !notification.has("object")) {
                return false;
            }

            String event = notification.get("event").asText(); // e.g., payment.succeeded
            JsonNode object = notification.get("object");
            if (!object.has("id") || !object.has("status")) {
                return false;
            }
            String objectId = object.get("id").asText();
            String notifiedStatus = object.get("status").asText();

            // 3. Fetch current from YooKassa
            JsonNode current = getPayment(objectId);
            if (current == null || !current.has("status")) {
                return false;
            }
            String currentStatus = current.get("status").asText();

            // Логика согласованности: для payment.waiting_for_capture допустимо, если текущий уже succeeded
            if (event.equals("payment.waiting_for_capture")) {
                if (!currentStatus.equals("waiting_for_capture") && !currentStatus.equals("succeeded")) {
                    return false;
                }
            } else {
                // для остальных — точное совпадение
                if (!currentStatus.equals(notifiedStatus)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            // при ошибке — отвергаем
            return false;
        }
    }

    private Long extractOrderId(JsonNode notification) {
        JsonNode node = notification.at("/object/metadata/orderId");
        if (node.isMissingNode() || node.asText().isBlank()) throw new ObjectNotFoundException("");
        try {
            return Long.parseLong(node.asText());
        } catch (NumberFormatException e) {
            throw new ObjectNotFoundException("");
        }
    }
}
