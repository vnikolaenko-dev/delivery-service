package ru.don_polesie.back_end.controller.worker;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;

import java.time.Instant;

@Tag(
        name = "Управление заказами (Работник)",
        description = "API для обработки и управления заказами сотрудниками"
)
@RequestMapping("/api/worker/orders")
public interface WorkerOrderController {

    @Operation(
            summary = "Заказ по id",
            description = "Возвращает заказ по id"
    )
    @GetMapping("/find")
    ResponseEntity<OrderDtoRR> findById(@RequestParam @Min(value = 1) Long id);

    @Operation(
            summary = "Список заказов",
            description = "Возвращает страницу заказов, требующих обработки сотрудниками"
    )
    @GetMapping("")
    ResponseEntity<Page<OrderDtoRR>> findOrdersPage(@RequestParam @Min(value = 1) Integer pageNumber);


    @Operation(
            summary = "Список заказов для обработки",
            description = "Возвращает страницу заказов, требующих обработки сотрудниками"
    )
    @GetMapping("/ready-to-process")
    ResponseEntity<Page<OrderDtoRR>> findReadyToProccessOrdersPage(@RequestParam @Min(value = 1) Integer pageNumber);

    @Operation(
            summary = "Обработать заказ",
            description = "Обновление весов и статуса заказа в процессе сборки"
    )
    @PostMapping("/{id}/process")
    ResponseEntity<Void> processOrder(@PathVariable @Min(value = 1) Long id,
                                      @RequestBody @Valid ProcessWeightsRequest req);

    @Operation(
            summary = "Отметить как отправленный",
            description = "Изменение статуса заказа на 'отправлен' после комплектации"
    )
    @PutMapping("/{id}/ship")
    ResponseEntity<Void> markShipped(@PathVariable @Min(value = 1) Long id);

    @Operation(
            summary = "Удалить заказ",
            description = "Полное удаление заказа из системы (административная функция)"
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteOrder(@PathVariable @Min(value = 1) Long id);

    @Operation(
            summary = "Продажи товара за день",
            description = "Возвращает общее количество проданных единиц конкретного товара за указанную дату"
    )
    @GetMapping("/products/{productId}/sales")
    ResponseEntity<Long> getTotalSalesForProductByDate(@PathVariable Long productId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant date);


    @Operation(
            summary = "Количество заказов с товаром",
            description = "Возвращает количество заказов, содержащих указанный товар, за определенную дату"
    )
    @GetMapping("/{productId}/sales")
    ResponseEntity<Long> getOrderCountForProductByDate(@PathVariable Long productId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant date);
}
