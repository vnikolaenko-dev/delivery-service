package ru.don_polesie.back_end.service;

import org.springframework.data.domain.Page;
import ru.don_polesie.back_end.dto.order.OrderCreateResponse;
import ru.don_polesie.back_end.dto.order.OrderDtoRR;
import ru.don_polesie.back_end.dto.order.ProcessWeightsRequest;
import ru.don_polesie.back_end.model.User;

import java.time.Instant;

public interface UserOrderService {

    Page<OrderDtoRR> findUserOrdersPage(Integer pageNumber, String username);

    Page<OrderDtoRR> findShippedUserOrdersPage(Integer pageNumber, String username);

    OrderCreateResponse save(OrderDtoRR orderDtoRR, User user);

    void deleteOrder(Long orderId);

    void deleteProductFromOrder(Long orderId, Long productId);

    OrderDtoRR findById(Long id);
}
