package ru.don_polesie.back_end.controller.basket.publish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springdoc.core.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.model.basket.Basket;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.basket.BasketService;

@RequestMapping("/api/basket")
@AllArgsConstructor
@RestController
public class BasketController {
    private final BasketService basketService;
    private final SecurityUtils securityUtils;

    @GetMapping("/add")
    public ResponseEntity<Basket> addProduct(
            @RequestParam @Min(value = 1) Long productId,
            @RequestParam @Min(value = 1) int quantity) {
        User user = securityUtils.getCurrentUser();
        var resp = basketService.addProduct(user.getPhoneNumber(), productId, quantity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
    }

    @Operation(
            summary = "Обновить количество товара в заказу",
            description = "Обновление заказа"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные заказа"),
            @ApiResponse(responseCode = "404", description = "Товар или адрес не найден")
    })
    @GetMapping("/change-quantity")
    public ResponseEntity<Basket> changeProductQuantity(@RequestParam @Min(value = 1) Long productId,
                                                        @RequestParam @Min(value = 1) int quantity) {
        User user = securityUtils.getCurrentUser();
        var resp = basketService.changeQuantityProduct(user.getPhoneNumber(), productId, quantity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
    }

    @Operation(
            summary = "Удалить товар из заказа",
            description = "Удаляет конкретный товар из заказа (до момента его обработки)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успешно удален из заказа"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Заказ или товар не найден")
    })
    // @PreAuthorize("@orderAccess.canModify(#orderId, authentication)")
    @DeleteMapping("/product")
    public ResponseEntity<Basket> deleteProductFromOrder(@RequestParam @Min(value = 1) Long productId) {
        User user = securityUtils.getCurrentUser();
        var resp = basketService.deleteProductFromBasket(user.getPhoneNumber(), productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
    }
}
