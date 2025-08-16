package ru.don_polesie.back_end.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.payment.CreatePaymentRequest;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;

@RequestMapping("/api/payment")
public interface PaymentController {

    @Operation(
            summary = "Создание оплаты",
            description = "Создать данные, включая ссылку, для оплаты заказа"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Оплата успешно создана"),
            @ApiResponse(responseCode = "400", description = "Данные для создания оплаты введены неверно",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @PostMapping("")
    ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest req);

    @PostMapping("/notifications")
    ResponseEntity<?> notifications(@RequestBody JsonNode body, HttpServletRequest servletRequest);

    @GetMapping("/{id}")
    ResponseEntity<?> getPayment(@PathVariable String id);
}
