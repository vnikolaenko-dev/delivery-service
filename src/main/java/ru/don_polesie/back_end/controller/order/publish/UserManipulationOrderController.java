package ru.don_polesie.back_end.controller.order.publish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.model.Address;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.impl.UserOrderServiceImpl;
import ru.don_polesie.back_end.service.inf.UserOrderService;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Заказы пользователя",
        description = "API для управления заказами текущего пользователя"
)
@RequestMapping("/api/order")
public class UserManipulationOrderController {
    private final UserOrderServiceImpl orderServiceImpl;
    private final SecurityUtils securityUtils;

    @Operation(
            summary = "Создать заказ",
            description = "Создание нового заказа на основе выбранных товаров и адреса доставки"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные заказа"),
            @ApiResponse(responseCode = "404", description = "Товар или адрес не найден")
    })
    @PostMapping
    public ResponseEntity<OrderCreateResponse> save(@RequestBody Address address) {
        User user = securityUtils.getCurrentUser();
        var resp = orderServiceImpl.save(user, address);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp);
    }
}
