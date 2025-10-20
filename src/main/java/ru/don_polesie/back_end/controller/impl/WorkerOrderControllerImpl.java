package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.WorkerOrderController;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.service.WorkOrderService;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class WorkerOrderControllerImpl implements WorkerOrderController {

    private final WorkOrderService workOrderService;

    @Override
    public ResponseEntity<Page<OrderDtoRR>> findOrdersPage(Integer pageNumber) {
        return ResponseEntity.ok().body(workOrderService.findOrdersPage(pageNumber));
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
        return new ResponseEntity<>(
                workOrderService.getTotalSalesForProductByDate(productId, date), HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Long> getOrderCountForProductByDate(Long productId, Instant date) {
        return new ResponseEntity<>(
                workOrderService.getOrderCountForProductByDate(productId, date), HttpStatus.OK
        );
    }
}
