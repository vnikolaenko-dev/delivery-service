package ru.don_polesie.back_end.service.inf;

import com.fasterxml.jackson.databind.JsonNode;



public interface YooKassaService {
    JsonNode createPayment(Long orderId) throws Exception;

    void storeNotification(JsonNode notification);

    JsonNode getPayment(Long id) throws Exception;

    boolean verifyNotification(String remoteIp, JsonNode notification);
}
