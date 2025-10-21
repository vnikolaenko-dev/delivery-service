package ru.don_polesie.back_end.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.don_polesie.back_end.controller.UserOrderController;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.security.SecurityUtils;
import ru.don_polesie.back_end.service.UserOrderService;


@RestController
@RequiredArgsConstructor
public class UserOrderControllerImpl implements UserOrderController {
    private final UserOrderService orderServiceImpl;
    private final SecurityUtils securityUtils;

    @Override
    public ResponseEntity<Page<OrderDtoRR>> findOrdersPage(Integer pageNumber) {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(orderServiceImpl.findUserOrdersPage(pageNumber, username));
    }

    @Override
    public ResponseEntity<Page<OrderDtoRR>> findShippedUserOrdersPage(Integer pageNumber) {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(orderServiceImpl.findShippedUserOrdersPage(pageNumber, username));
    }

    @Override
    public ResponseEntity<OrderDtoRR> findById(Long id) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(orderServiceImpl.findById(id));
    }

    @Override
    public ResponseEntity<OrderCreateResponse> save(OrderDtoRR orderDtoRR) {
        User user = securityUtils.getCurrentUser();
        var resp = orderServiceImpl.save(orderDtoRR, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resp);
    }

    @Override
    public ResponseEntity<Void> deleteProductFromOrder(Long orderId, Long productId) {
        orderServiceImpl.deleteProductFromOrder(orderId, productId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
