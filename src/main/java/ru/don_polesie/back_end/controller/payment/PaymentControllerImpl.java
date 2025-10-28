package ru.don_polesie.back_end.controller.payment;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.dto.payment.CreatePaymentRequest;
import ru.don_polesie.back_end.service.system.YooKassaService;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentControllerImpl implements PaymentController {

    private final YooKassaService yooKassaServiceImpl;

    @Override
    @Deprecated
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest req) throws Exception {
        JsonNode payment = yooKassaServiceImpl.createPayment(req.getOrderId());
        return ResponseEntity.ok().body(payment);
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
    public ResponseEntity<?> getPayment(Long id) throws Exception {
        JsonNode payment = yooKassaServiceImpl.getPayment(id);
        return ResponseEntity.ok(payment);
    }

    private String extractClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
