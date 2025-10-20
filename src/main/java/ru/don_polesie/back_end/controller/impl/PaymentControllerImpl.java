package ru.don_polesie.back_end.controller.impl;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.PaymentController;
import ru.don_polesie.back_end.dto.payment.CreatePaymentRequest;
import ru.don_polesie.back_end.service.YooKassaService;
import ru.don_polesie.back_end.utils.JsonNodeFactoryWrapperImpl;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentControllerImpl implements PaymentController {

    private final YooKassaService yooKassaServiceImpl;

    @Override
    @Deprecated
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest req) {
        try {
            JsonNode payment = yooKassaServiceImpl.createPayment(req.getOrderId());
            return ResponseEntity.ok().body(payment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    JsonNodeFactoryWrapperImpl.error("error", e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<?> notifications(
            @RequestBody JsonNode body,
            HttpServletRequest servletRequest) {
        String remoteIp = extractClientIp(servletRequest);
        if (!yooKassaServiceImpl.verifyNotification(remoteIp, body)) {
            log.warn("Отказано в обработке уведомления: ip={} body={}", remoteIp, body);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid notification"));
        }

        yooKassaServiceImpl.storeNotification(body);
        return ResponseEntity.ok(Map.of("status", "OK"));
    }

    @Override
    public ResponseEntity<?> getPayment(Long id) {
        try {
            JsonNode payment = yooKassaServiceImpl.getPayment(id);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            log.error("getPayment failed", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Не удалось получить платёж", "detail", e.getMessage()));
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
