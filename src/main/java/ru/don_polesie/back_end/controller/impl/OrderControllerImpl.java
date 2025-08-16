package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.OrderController;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderServiceImpl;

    @Override
    public ResponseEntity<OrderDtoRR> findById(Long id) {
        return ResponseEntity.ok().body(orderServiceImpl.findById(id));
    }

    @Override
    public ResponseEntity<OrderCreateResponse> save(OrderDtoRR orderDtoRR) {
        var resp = orderServiceImpl.save(orderDtoRR);
        return ResponseEntity.ok()
                .body(resp);
    }

    @Override
    public ResponseEntity<Void> deleteProductFromOrder(Long orderId, Long productId) {
        orderServiceImpl.deleteProductFromOrder(orderId, productId);
        return ResponseEntity.ok().build();
    }
}
