package ru.don_polesie.back_end.controller.order.staff;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.request.ProcessWeightsDtoRequest;
import ru.don_polesie.back_end.service.order.WorkerOrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/order")
public class OrderProcessController {

    private final WorkerOrderService workOrderService;

    @Operation(
            summary = "Обработать заказ",
            description = "Обновление весов и статуса заказа в процессе сборки"
    )
    @PostMapping("/{id}/process")
    public ResponseEntity<Void> process(@PathVariable @Min(value = 1) Long id,
                                        @RequestBody @Valid ProcessWeightsDtoRequest req) {
        workOrderService.processOrder(id, req);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить заказ",
            description = "Полное удаление заказа из системы (административная функция)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(value = 1) Long id) {
        // workOrderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить заказ",
            description = "Полное удаление заказа из системы (административная функция)"
    )
    @DeleteMapping("/{order_id}/{product_id}")
    public ResponseEntity<Void> deleteProductFromOrder(@PathVariable @Min(value = 1) Long order_id,
                                       @PathVariable @Min(value = 1) Long product_id) {
        workOrderService.deleteProductFromOrder(order_id, product_id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Отметить как отправленный",
            description = "Изменение статуса заказа на 'отправлен' после комплектации"
    )
    @PutMapping("/ship/{id}")
    public ResponseEntity<Void> markShipped(@PathVariable @Min(value = 1) Long id) {
        workOrderService.markShipped(id);
        return ResponseEntity.ok().build();
    }
}
