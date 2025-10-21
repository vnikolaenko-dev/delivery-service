package ru.don_polesie.back_end.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.payment.CreatePaymentRequest;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;

@Tag(
        name = "Платежи",
        description = "API для работы с платежами и интеграцией с платежными системами"
)
@RequestMapping("/api/payment")
public interface PaymentController {

    @Operation(
            summary = "Создание платежа",
            description = "Инициализация платежа для заказа. Возвращает данные для проведения оплаты, включая платежную ссылку"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Платеж успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для создания платежа"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @PostMapping("")
    ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest req) throws Exception;

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
