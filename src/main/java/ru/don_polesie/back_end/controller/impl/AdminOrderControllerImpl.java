package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.AdminOrdersController;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.service.OrderService;

@RestController
@RequiredArgsConstructor
public class AdminOrderControllerImpl implements AdminOrdersController {

    private final OrderService orderServiceImpl;

    @Override
    public ResponseEntity<Page<OrderDtoRR>> findOrdersPage(Integer pageNumber) {
        return ResponseEntity.ok().body(orderServiceImpl.findOrdersPage(pageNumber));
    }

    @Override
    public ResponseEntity<Void> processOrder(Long id, ProcessWeightsRequest req) {
        orderServiceImpl.processOrder(id, req);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        orderServiceImpl.deleteOrder(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> markShipped(Long id) {
        orderServiceImpl.markShipped(id);
        return ResponseEntity.ok().build();
    }
}
