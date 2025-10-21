package ru.don_polesie.back_end.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;

@Tag(
        name = "Заказы пользователя",
        description = "API для управления заказами текущего пользователя"
)
@RequestMapping("/api/orders")
public interface UserOrderController {

    @Operation(
            summary = "Все заказы",
            description = "Возвращает все заказы пользователя"
    )
    @GetMapping("")
    ResponseEntity<Page<OrderDtoRR>> findOrdersPage(@RequestParam @Min(value = 1) Integer pageNumber);

    @Operation(
            summary = "История доставленных заказов",
            description = "Возвращает историю заказов текущего пользователя с пагинацией"
    )
    @GetMapping("")
    ResponseEntity<Page<OrderDtoRR>> findShippedUserOrdersPage(@RequestParam @Min(value = 1) Integer pageNumber);


    @Operation(
            summary = "Получить детали заказа",
            description = "Возвращает полную информацию о конкретном заказе пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден или недоступен")
    })
    @GetMapping("/{id}")
    ResponseEntity<OrderDtoRR> findById(@PathVariable Long id);

    @Operation(
            summary = "Создать заказ",
            description = "Создание нового заказа на основе выбранных товаров и адреса доставки"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные заказа"),
            @ApiResponse(responseCode = "404", description = "Товар или адрес не найден")
    })
    @PostMapping("")
    ResponseEntity<OrderCreateResponse> save(@RequestBody OrderDtoRR orderDtoRR);


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
    @DeleteMapping("/{orderId}/product/{productId}")
    ResponseEntity<Void> deleteProductFromOrder(@PathVariable Long orderId,
                                                @PathVariable Long productId);

}
