package ru.don_polesie.back_end.controller.payment;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Платежи",
        description = "API для работы с платежами и интеграцией с платежными системами"
)
@RequestMapping("/api/payment")
public interface PaymentController {

    @Operation(
            summary = "Обработка уведомлений",
            description = "Webhook для приема уведомлений от платежной системы о статусе платежей"
    )
    @PostMapping("/notifications")
    ResponseEntity<?> notifications(@RequestBody JsonNode body, HttpServletRequest servletRequest);

    @Operation(
            summary = "Получить информацию о платеже",
            description = "Возвращает детальную информацию о платеже по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о платеже получена"),
            @ApiResponse(responseCode = "404", description = "Платеж не найден")
    })
    @GetMapping("/{id}")
    ResponseEntity<?> getPayment(@PathVariable Long id) throws Exception;
}
