package ru.don_polesie.back_end.service;

import com.fasterxml.jackson.databind.JsonNode;



public interface YooKassaService {
    JsonNode createPayment(String orderId) throws Exception;

    void storeNotification(JsonNode notification);

    JsonNode getPayment(String paymentId) throws Exception;

    boolean verifyNotification(String remoteIp, JsonNode notification);
}
