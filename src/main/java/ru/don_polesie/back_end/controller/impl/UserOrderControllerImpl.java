package ru.don_polesie.back_end.controller.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.UserOrderController;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.UserOrderService;
import ru.don_polesie.back_end.service.YooKassaService;


@RestController
@RequiredArgsConstructor
public class UserOrderControllerImpl implements UserOrderController {
    private final UserOrderService orderServiceImpl;
    private final SecurityUtils securityUtils;


    @Override
    public ResponseEntity<Page<OrderDtoRR>> findOrdersPage(Integer pageNumber) {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity.ok(orderServiceImpl.findUserOrdersPage(pageNumber, username));
    }

    @Override
    public ResponseEntity<OrderDtoRR> findById(Long id) {
        return ResponseEntity.ok().body(orderServiceImpl.findById(id));
    }

    @Override
    public ResponseEntity<OrderCreateResponse> save(OrderDtoRR orderDtoRR) {
        User user = securityUtils.getCurrentUser();
        var resp = orderServiceImpl.save(orderDtoRR, user);
        return ResponseEntity.ok()
                .body(resp);
    }

    @Override
    public ResponseEntity<Void> deleteProductFromOrder(Long orderId, Long productId) {
        orderServiceImpl.deleteProductFromOrder(orderId, productId);
        return ResponseEntity.ok().build();
    }

}
