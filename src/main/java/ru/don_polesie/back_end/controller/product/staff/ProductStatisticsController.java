package ru.don_polesie.back_end.controller.product.staff;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.service.inf.WorkerOrderService;

import java.time.Instant;

@Tag(
        name = "Управление заказами (Работник)",
        description = "API для обработки и управления заказами сотрудниками"
)
@RequestMapping("/api/staff/order")
@RestController
@RequiredArgsConstructor
public class ProductStatisticsController {

    private final WorkerOrderService workOrderService;


    @Operation(
            summary = "Продажи товара за день",
            description = "Возвращает общее количество проданных единиц конкретного товара за указанную дату"
    )
    @GetMapping("/products/{productId}/sales")
    public ResponseEntity<Long> getTotalSalesForProductByDate(@PathVariable Long productId, @RequestParam Instant date) {
        Long totalSalesForProduct = workOrderService.getTotalSalesForProductByDate(productId, date);
        return ResponseEntity
                .ok(totalSalesForProduct);
    }

    @Operation(
            summary = "Количество заказов с товаром",
            description = "Возвращает количество заказов, содержащих указанный товар, за определенную дату"
    )
    @GetMapping("/{productId}/sales")
    public ResponseEntity<Long> getOrderCountForProductByDate(@PathVariable Long productId, @RequestParam Instant date) {
        Long orderCount = workOrderService.getOrderCountForProductByDate(productId, date);
        return ResponseEntity
                .ok(orderCount);
    }
}
