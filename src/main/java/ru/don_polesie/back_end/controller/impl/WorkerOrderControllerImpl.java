package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.WorkerOrderController;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.dto.product.ProductDtoRR;
import ru.don_polesie.back_end.service.WorkOrderService;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class WorkerOrderControllerImpl implements WorkerOrderController {

    private final WorkOrderService workOrderService;

    @Override
    public ResponseEntity<OrderDtoRR> findById(Long id) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(workOrderService.findById(id));
    }

    @Override
    public ResponseEntity<Page<OrderDtoRR>> findOrdersPage(Integer pageNumber) {
        Page<OrderDtoRR> ordersPage = workOrderService.findOrdersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(ordersPage);
    }

    @Override
    public ResponseEntity<Page<OrderDtoRR>> findReadyToProccessOrdersPage(Integer pageNumber) {
        Page<OrderDtoRR> ordersPage = workOrderService.findMoneyReservaitedOrdersPage(pageNumber);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(ordersPage);
    }

    @Override
    public ResponseEntity<Void> processOrder(Long id, ProcessWeightsRequest req) {
        workOrderService.processOrder(id, req);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        // workOrderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> markShipped(Long id) {
        workOrderService.markShipped(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Long> getTotalSalesForProductByDate(Long productId, Instant date) {
        Long totalSalesForProduct = workOrderService.getTotalSalesForProductByDate(productId, date);
        return ResponseEntity
                .ok(totalSalesForProduct);
    }

    @Override
    public ResponseEntity<Long> getOrderCountForProductByDate(Long productId, Instant date) {
        Long orderCount = workOrderService.getOrderCountForProductByDate(productId, date);
        return ResponseEntity
                .ok(orderCount);
    }
}
