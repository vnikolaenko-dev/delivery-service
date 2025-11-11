package ru.don_polesie.back_end.controller.order.staff;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.model.enums.OrderStatus;
import ru.don_polesie.back_end.service.order.WorkerOrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/order")
public class OrderInfoController {
    private final WorkerOrderService workOrderService;

    @Operation(
            summary = "Заказ по id",
            description = "Возвращает заказ по id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDtoRR> findById(@PathVariable @Min(1) Long id) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(workOrderService.findById(id));
    }

    @Operation(
            summary = "Список заказов",
            description = "Возвращает страницу заказов, требующих обработки сотрудниками"
    )
    @GetMapping
    public ResponseEntity<Page<OrderDtoRR>> findOrdersPage(@RequestParam @Min(0) Integer pageNumber) {
        Page<OrderDtoRR> ordersPage = workOrderService.findOrdersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(ordersPage);
    }

    @Operation(
            summary = "Список заказов по статусу",
            description = "Возвращает страницу заказов с определенным статусом"
    )
    @GetMapping("/status")
    public ResponseEntity<Page<OrderDtoRR>> findByStatus(@RequestParam @Min(0) Integer pageNumber, @RequestParam String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        Page<OrderDtoRR> ordersPage = workOrderService.findOrdersPageWithStatus(pageNumber, orderStatus);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(ordersPage);
    }
}
