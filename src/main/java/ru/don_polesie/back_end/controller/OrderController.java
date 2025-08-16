package ru.don_polesie.back_end.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.exceptions.ObjectNotFoundException;

import java.util.List;

@RequestMapping("/api/orders")
public interface OrderController {


    @Operation(
            summary = "Поиск заказа по ID",
            description = "Возвращает один заказ по заданному ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ по заданному ID успешно найден",
                    content = @Content(schema = @Schema(implementation = OrderDtoRR.class))),
            @ApiResponse(responseCode = "404", description = "Заказ по заданному ID не найден",
                    content = @Content(schema = @Schema(implementation = ObjectNotFoundException.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<OrderDtoRR> findById(@PathVariable Long id);

    @Operation(
            summary = "Создание заказа",
            description = "Создание нового заказа пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ успешно создан",
                    content = @Content(schema = @Schema(implementation = OrderDtoRR.class))),
            @ApiResponse(responseCode = "400", description = "Данные для создания заказа указаны неверно",
                    content = @Content(schema = @Schema(implementation = HttpClientErrorException.BadRequest.class)))
    })
    @PostMapping("")
    ResponseEntity<OrderCreateResponse> save(@RequestBody OrderDtoRR orderDtoRR);


    @Operation(
            summary = "Удаление товара из заказа",
            description = "Удалаяет заданный товар из указанного заказа"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успешно удален"),
            @ApiResponse(responseCode = "400", description = "Данные для удаления указаны неверно",
                    content = @Content(schema = @Schema(implementation = HttpClientErrorException.BadRequest.class))),
            @ApiResponse(responseCode = "404", description = "Товар или заказ указанные в запросе не найдены",
                    content = @Content(schema = @Schema(implementation = HttpClientErrorException.BadRequest.class)))
    })
    @PreAuthorize("@orderAccess.canModify(#orderId, authentication)")
    @DeleteMapping("/{orderId}/product/{productId}")
    ResponseEntity<Void> deleteProductFromOrder(@PathVariable Long orderId,
                                                @PathVariable Long productId);
}
